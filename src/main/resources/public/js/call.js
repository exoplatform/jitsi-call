require(["SHARED/jquery", "SHARED/webConferencing", "SHARED/webConferencing_jitsi"], function($, webconferencing, provider) {

  var MeetApp = function() {

    var callId;
    var isStopping = false;
    var isGuest = false;
    var authToken;
    var isStopped = false;
    var api;
    var isModerator;
    
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
        cache: false
      });
    };
    // Request userinfo of guest via Gateway
    var getGuestUserInfo = function(inviteId) {
      return $.get({
        type: "GET",
        url: "/jitsi/api/v1/userinfo/" + inviteId,
        cache: false
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
        cache: false
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
        cache: false
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
        cache: false
      });
    };
    
    // Request provider settings
    var getJitsiToken = function(username) {
      return $.get({
        type: "GET",
        url: "/jitsi/api/v1/token/" + username,
        cache: false
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
    
    /**
     * Generate room title
     * TODO: add i18n
     */
    var getRoomTitle = function(call) {
      if (call.owner.group) {
        return "Call: " + call.owner.title;
      } 
      // 1-1 call
      var subject = "Call: ";
      call.participants.forEach((participant) => {
        subject += participant.title + " ";
      });
      return subject.trim();
    };

    var subscribeCall = function(userId) {
      // Subscribe to user updates (incoming calls will be notified here)
      webconferencing.onUserUpdate(userId, function(update) {
        // This connector cares only about own provider events
        if (update.providerType == "jitsi") {
          if (update.eventType == "call_state" && update.callId == callId) {
            if (update.callState == "stopped" && !isStopping) {
                isStopped = true;
                api.dispose();
                window.close();
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
        var displayName = userinfo.firstName + " " + userinfo.lastName;
        var roomTitle = getRoomTitle(call);
        window.document.title = roomTitle;
        const options = {
            roomName: callId,
            width: '100%',
            jwt : token,
            height: window.innerHeight,
            parentNode: document.querySelector("#meet"),
            configOverwrite: { 
              subject: roomTitle, 
              prejoinPageEnabled: true
            },
            interfaceConfigOverwrite: {
              TOOLBAR_BUTTONS: ['microphone', 'chat', 'camera', 'desktop', 'fullscreen',
                'fodeviceselection', 'hangup', 'profile', 'sharedvideo', 
                'settings', 'videoquality', 'tileview', 'videobackgroundblur', 'mute-everyone'
              ],
              JITSI_WATERMARK_LINK: ""
            },
            userInfo: {
              displayName : displayName
            }
          };
          api = new JitsiMeetExternalAPI(domain, options);
          webconferencing.updateCall(callId, "joined");
          console.log("Joined to the call " + callId);
          subscribeCall(userinfo.id);
          api.on("readyToClose", function(event) {
            isStopped = true;
            webconferencing.updateCall(callId, "leaved").done(function(){
              api.dispose();
              window.close();
            });
           });

          api.addEventListener('participantRoleChanged', function(event) {
            api.executeCommand('displayName', displayName);
            // For recording feature
            if (event.role === "moderator") {
             isModerator = true;
             saveCallInfo(callId, {
               owner: call.owner.id,
               group: call.owner.group,
               moderator: userinfo.id
             });
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
                  if (call.owner.group && Object.keys(call.owner.members).length != 0) {
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