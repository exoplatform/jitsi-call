require(["SHARED/bootstrap", "SHARED/jquery", "SHARED/webConferencing", "SHARED/webConferencing_jitsi"], function(bootstrap, $, webconferencing, provider) {

  var MeetApp = function() {

    var callId;
    var isStopping = false;
    var isGuest = false;
    var authToken;
    var isStopped = false;
    var api;
    var getUrlParameter = function(sParam) {
      var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName, i;

      for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
          return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
      }
    };

    // Request userinfo of exo user via Gateway
    var getExoUserInfo = function() {
      return $.get({
        type: "GET",
        url: "/jitsi/portal/rest/jitsi/userinfo",
      });
    };
    // Request userinfo of guest via Gateway
    var getGuestUserInfo = function(inviteId) {
      return $.get({
        type: "GET",
        url: "/jitsi/api/v1/userinfo/" + inviteId,
      });
    };
    
    // Save call info via Gateway
    // TODO: Secure
    var saveCallInfo = function(callId, callInfo) {
      return $.post({
        url: "/jitsi/api/v1/calls/" + callId,
        data : JSON.stringify(callInfo),
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
      });
    };

    // Request contextinfo
    var getContextInfo = function(userId) {
      return $.get({
        type: "GET",
        beforeSend: function(request) {
          request.setRequestHeader("X-Exoplatform-Auth", authToken);
        },
        url: "/portal/rest/jitsi/context/" + userId,
      });
    };

    // Request provider settings
    var getSettings = function() {
      return $.get({
        type: "GET",
        beforeSend: function(request) {
          request.setRequestHeader("X-Exoplatform-Auth", authToken);
        },
        url: "/portal/rest/jitsi/settings",
      });
    };
    
    // Request provider settings
    var getJitsiToken = function(username) {
      return $.get({
        type: "GET",
        url: "/jitsi/api/v1/token/" + username,
      });
    };

    var beforeunloadListener = function() {
      if (!isStopped) {
        isStopping = true;
        webconferencing.updateCall(callId, 'leaved');
      }
      if (api) {
        api.dispose();
      }
     
    };

    var getCallId = function() {
      var currentURL = window.location.href;
      if (currentURL.indexOf("?") !== -1) {
        return currentURL.substring(currentURL.lastIndexOf("/") + 1, currentURL.indexOf("?"));
      } else {
        return currentURL.substring(currentURL.lastIndexOf("/") + 1);
      }
    };

    var subscribeCall = function(userId) {
      // Subscribe to user updates (incoming calls will be notified here)
      webconferencing.onUserUpdate(userId, function(update) {
        // This connector cares only about own provider events
        if (update.providerType == "jitsi") {
          var callId = update.callId;
          if (update.eventType == "call_state") {
            if (update.callState == "stopped" && !isStopping) {
              isStopped = true;
              $('body').html('<h2 style="margin:50px">Call has been stopped</h2>');
            }
          }
        } // it's other provider type - skip it
      }, function(err) {
        log.error("Failed to listen on user updates", err);
      });
    };

    var initCall = function(userinfo, call) {
      console.log("Init call with userInfo: " + JSON.stringify(userinfo));
      var apiUrl = document.getElementById("jitsi-api").getAttribute("src");
      const domain = apiUrl.substring(apiUrl.indexOf("://") + 3, apiUrl.lastIndexOf("/external_api.js"));
      var name = userinfo.firstName + " " + userinfo.lastName;
      getJitsiToken(name).then(function(token){        
        var room = "Meet";
        var callParticipants = callId.substring(2, callId.length).split("-");
        callParticipants.forEach(function(elem){ room+=elem.replace(/^./, elem[0].toUpperCase()); });
        const options = {
            roomName: callId,
            width: '100%',
            jwt : token,
            height: window.innerHeight,
            parentNode: document.querySelector("#meet"),
            configOverwrite: { subject: room },
            interfaceConfigOverwrite: {
              TOOLBAR_BUTTONS: ['microphone', 'camera', 'desktop', 'fullscreen',
                'recording', 'fodeviceselection', 'hangup', 'profile', 'sharedvideo', 
                'settings', 'videoquality', 'tileview', 'videobackgroundblur', 'mute-everyone'
              ]
            }
          };
          api = new JitsiMeetExternalAPI(domain, options);
          webconferencing.updateCall(callId, "joined");
          console.log("Joined to the call " + callId);
          subscribeCall(userinfo.id);
          api.on("readyToClose", function(event) {
            webconferencing.updateCall(callId, "leaved");
              isStopped = true;
              $("body").html('<h2 style="margin:50px">You have left the call.</h2>');
           });
          
          api.addEventListener('participantRoleChanged', function(event) {
            
           if (event.role === "moderator") {
             saveCallInfo(callId, {
               owner: call.owner.id,
               group: call.owner.group,
               moderator: userinfo.id
             })
            }
          });

      });
      
    };


    /**
     * Inits current user and context
     */
    this.init = function() {
        callId = getCallId();
        var $initUser = $.Deferred();
        var inviteId = getUrlParameter("inviteId");
        if (inviteId) {
          let trimmedUrl = window.location.href.substring(0, window.location.href.indexOf("?"));
          window.history.pushState({}, "", trimmedUrl);
          getGuestUserInfo(inviteId).then(function(data) {
            isGuest = true;
            $initUser.resolve(data.userInfo, data.authToken);
          }).catch(function(err) {
            console.log("Cannot get guest user info: " + JSON.stringify(err));
            $initUser.fail(err);
            // TODO: show user-friendly error?
          });
        } else {
          getExoUserInfo().then(function(data) {
            $initUser.resolve(data.userInfo, data.authToken);
          }).catch(function(err) {
            console.log("Cannot get exo user info: " + JSON.stringify(err));
            // $initUser.fail(err);
            // redirect to login page
            window.document.location.href = "/portal/login?initialURI=/jitsi/meet/" + callId
          });
        }

        $initUser.then(function(userinfo, token) {
          authToken = token;
          getContextInfo(userinfo.id).then(function(contextInfo) {
            getSettings().then(function(settings) {
              eXo.env.portal.profileOwner = userinfo.id;
              webconferencing.init(userinfo, contextInfo);
              provider.configure(settings);
              webconferencing.addProvider(provider);
              webconferencing.update();
              webconferencing.getCall(callId).then(function(call) {
                // Check if user allowed
                if (!isGuest) {
                  var user = [];
                  if (call.owner.group) {
                    user = Object.keys(call.owner.members).filter(function(id) {
                      return id === userinfo.id;
                    });
                  } else {
                    user = call.participants.filter(function(participant) {
                      return participant.id === userinfo.id;
                    });
                  }
                  if (user.length == 0) {
                    alert("User is not allowed for this call");
                    return;
                  }
                }
                initCall(userinfo, call);
              }).catch(function(err) {
                console.log("Cannot init call:" + JSON.stringify(err));
                alert("Error occured while initializing the call.");
              });
            });
          });
        });
        window.addEventListener('beforeunload', beforeunloadListener);
    };
  }
  var meetApp = new MeetApp();
  meetApp.init();
});