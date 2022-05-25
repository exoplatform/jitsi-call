require([
  "SHARED/jquery",
  "SHARED/webConferencing",
  "SHARED/webConferencing_jitsi",
  "app",
], function($, webConferencing, provider, app) {

  /** For debug logging. */
  const log = webConferencing.getLog("jitsi").prefix("call");

  function MeetApp() {
    var callId;
    var isStopping = false;
    var isGuest = false;
    //var inviteId;
    var authToken;
    var isStopped = false;
    var api;

    var getUrlParameter = function(sParam) {
      var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split("&"),
        sParameterName,
        i;
      for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split("=");
        if (sParameterName[0] === sParam) {
          return sParameterName[1] === undefined
            ? true
            : sParameterName[1];
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

    // Save call info via Gateway
    // TODO: Secure
    var saveCallInfo = function(callId, callInfo) {
      return $.post({
        url: "/jitsi/api/v1/calls/" + callId,
        data: JSON.stringify(callInfo),
        dataType: "json",
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

    // Request internal auth token for guests
    var getInternalToken = function() {
      return $.get({
        type: "GET",
        url: "/jitsi/portal/rest/jitsi/token",
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
      if (callId && !isStopped) {
        isStopping = true;
        webConferencing.updateCall(callId, "leaved");
      }
      if (api) {
        closeInviteLink();
        api.dispose();
      }
    };

    var getCallId = function() {
      var currentURL = window.location.href;
      if (currentURL.indexOf("?") !== -1) {
        return currentURL.substring(
          currentURL.lastIndexOf("/") + 1,
          currentURL.indexOf("?")
        );
      } else {
        return currentURL.substring(currentURL.lastIndexOf("/") + 1);
      }
    };

    /**
     * Generate room title TODO: add i18n
     */
    var getRoomTitle = function(call) {
      if (call.owner.group) {
        let groupTitle;
        if (call.title) {
          groupTitle = call.title;
        } else {
          groupTitle = call.owner.title;
        }
        return groupTitle;
      }
      // 1-1 call
      var subject = "";
      call.participants.forEach((participant) => {
        subject += participant.title + " - ";
      });
      if (subject.length > 3) {
        subject = subject.slice(0, -3);
      }
      return subject;
    };

    /**
     * Generate tab title TODO: add i18n
     */
    var getTabTitle = function(call, userId) {
      if (call.owner.group) {
        let groupTitle;
        if (call.title) {
          groupTitle = call.title;
        } else {
          groupTitle = call.owner.title;
        }
        return "Call: " + groupTitle;
      }
      // 1-1 call
      var title = "Call: ";
      call.participants.forEach((participant) => {
        if (participant.id != userId) {
          title += participant.title + " - ";
        }
      });
      if (title.length > 3) {
        title = title.slice(0, -3);
      }
      return title;
    };

    /**
     * Inits loader screen. TODO: add i18n
     */
    var initLoaderScreen = function(userId, call) {
      var $loader = $("#loader .content");
      var join = false;
      call.participants.forEach(part => {
        if (part.state == "joined" && userId != part.id) {
          join = true;
        }
      });
      $loader.find(".label").html(join ? "You are joining" : "You are calling");

      if (call.owner.group) {
        var link = call.owner.avatarLink;
        $loader.find(".logo").css("background-image", "url(" + link + ")");
        let groupTitle;
        if (call.title) {
          groupTitle = call.title;
        } else {
          groupTitle = call.owner.title;
        }
        $loader.find(".room").html(groupTitle);
      } else {
        for (var i = 0; i < call.participants.length; i++) {
          // not current user
          if (call.participants[i].id != userId) {
            var link = call.participants[i].avatarLink;
            $loader.find(".logo").css("background-image", "url(" + link + ")");
            $loader.find(".room").html(call.participants[i].title);
            break;
          }
        }
      }
    };

    /**
     * Hides loader
     */
    var hideLoader = function() {
     $("#loader").css("display", "none");
    };
    var showLoader = function() {
      $("#loader").css("display", "block");
    }

    function closeInviteLink() {
      if (!isGuest) {
        const container = document.getElementById("invite-popup");
        if (container) {
          const children = container.children;
          if (children.length) {
            Object.values(children).map(child => container.removeChild(child));
          }
        }
      }
    }
    
    var closeWindow = function() {
      closeInviteLink();
      api.dispose();
      // Set timer first, then actually try to close the window (it may fail)
      setTimeout(() => {
        if (!window.closed) {
          // If not already closed we show the exit message to the user
          app.initExitScreen();
        }
      }, 250);
      window.close();
    };
    
    var subscribeUser = function(userId) {
      // Subscribe to user updates (incoming calls will be notified here)
      webConferencing.onUserUpdate(userId, update => {
        // This connector cares only about own provider events
        if (update.providerType == "jitsi") {
          if (update.eventType == "call_state" && update.callId == callId) {
            if (update.callState == "stopped" && !isStopping) {
              isStopped = true;
              closeWindow();
            }
          }
        } // it's other provider type - skip it
      }, err => {
        log.error("Failed to listen on user updates", err);
      });
    };

    /**
     * Initializes the call
     */
    var initCall = function(userinfo, call) {
      initLoaderScreen(userinfo.id, call);
      var apiUrl = document.getElementById("jitsi-api").getAttribute("src");
      const domain = apiUrl.substring(
        apiUrl.indexOf("://") + 3,
        apiUrl.lastIndexOf("/external_api.js")
      );
      var name = userinfo.firstName + " " + userinfo.lastName;
      const avatarLink = window.location.origin + userinfo.avatarLink;
      if (isGuest) {
        name += " (guest)";
      }
      getJitsiToken(name).then(token => {        
        var roomTitle = getRoomTitle(call);
        var tabTitle = getTabTitle(call, userinfo.id);
        window.document.title = tabTitle;
        var settings = ["devices", "language", "moderator"];
        if (isGuest) {
          settings.push("profile");
        }
        app.init();
        const options = {
          roomName: callId,
          width: "100%",
          jwt: token,
          // height: window.innerHeight,
          height: "100%",
          parentNode: document.querySelector("#meet"),
          onload: hideLoader,
          configOverwrite: {
            subject: roomTitle,
            prejoinPageEnabled: true,
            //requireDisplayName: true,
            //enableWelcomePage: true,
            //enableClosePage: true,
          },
          interfaceConfigOverwrite: {
            TOOLBAR_BUTTONS: [
              "desktop",
              "camera",
              "chat",
              "hangup",
              "fullscreen",
              "microphone",
              "mute-everyone",
              "mute-video-everyone",
              "participants-pane",
              "profile",
              "raisehand",
              "settings",
              "security",
              "select-background",
              "tileview",
              "toggle-camera",
              "videoquality",
              "closedcaptions",
              "sharedvideo",
              "shareaudio"
            ],
            JITSI_WATERMARK_LINK: "",
            SETTINGS_SECTIONS: settings
          },
          userInfo: {
            displayName: name,
            avatarUrl: avatarLink
          }
        };
        // Enable recording only for users and disable for guests
        if (!isGuest) {
          options.interfaceConfigOverwrite.TOOLBAR_BUTTONS.push("recording");
        }
        api = new JitsiMeetExternalAPI(domain, options);
        api.executeCommand("avatarUrl", avatarLink);
        webConferencing.updateCall(callId, "joined");
        log.info("Joined to the call " + callId + " by " + userinfo.id);
        api.on("readyToClose", event => {
          isStopped = true;
          webConferencing.updateCall(callId, "leaved").then(() => {
            closeWindow();
          });
        });
        api.addEventListener("participantRoleChanged", event => {
          const inviteLink = provider.getInviteLink(call);
          if (!isGuest) {
            app.initCallLink(inviteLink);
          }
          api.executeCommand("displayName", name);
          // For recording feature
          if (event.role === "moderator") {
           isModerator = true;
           var participantIds = call.participants.map(part => {
             return part.id;
           });
           saveCallInfo(callId, {
             owner: call.owner.id,
             type: call.owner.type,
             //group: call.owner.group, // TODO result of merge?
             moderator: userinfo.id,
             participants: participantIds
           });
          }
        });
      });
    };

    /**
     * Shows sign in page MOCK
     */
    var showSignInPage = function(callId, inviteId) {
      let url = "/portal/login?initialURI=/jitsi/meet/" + callId;
      if (inviteId) {
        // add invite id if the user connects via invite link
        url +=  encodeURIComponent("?inviteId=" + inviteId);
      }
      return window.document.location.href = url;
    };

    /**
     * Inits current user and context
     */
    this.init = function() {
      callId = getCallId();
      var $initUser = $.Deferred();
      let inviteId = getUrlParameter("inviteId");
      if (inviteId) {
        let trimmedUrl = window.location.href.substring(0, window.location.href.indexOf("?"));
        window.history.pushState({}, "", trimmedUrl);
        isGuest = true;
      }

      getExoUserInfo().then(data => {
        isGuest = false; // we have gotten successfully the current exo user
        $initUser.resolve(data.userInfo, data.authToken);
      }).catch(err => {
        if (isGuest) {
          log.debug("Cannot get user info for call invitation: " + callId + " (" + inviteId + "), treating the user as a guest", err);
          // Show signIn page: get firstName and lastName
          app.initSignInScreen(hideLoader, showLoader).then(() => {
            showSignInPage(callId, inviteId);
          }).catch(guestData => {
            var guestInfo = {};
              guestInfo.firstName = transliterate(guestData.firstName);
              guestInfo.lastName = transliterate(guestData.lastName);
              // Generate unique id
              const lastName = guestInfo.lastName.includes(" ") ? guestInfo.lastName.split(" ").join("") : guestInfo.lastName;
              guestInfo.id =
                "guest-" +
                transliterate(guestData.firstName) +
                "-" +
                lastName +
                "-" +
                Date.now();
              getInternalToken().then(response => {
                var token = response.token;
                $initUser.resolve(guestInfo, token);
              }).catch(err => {
                log.error("Cannot get internal auth token for call: " + callId + " user: " + guestInfo.id, err);
              });
          });
        } else {
          log.warn("Cannot get user info for call: " + callId + ", redirecting to portal login page", err);
          showSignInPage(callId, null);
        }
      });

      // Invites the user in call and check if it's allowed to connect
      function inviteInCall(userinfo, $promise) {
        webConferencing.checkInvite(callId, inviteId, userinfo.id).then(result => {
          if (result.allowed) {
            webConferencing.addGuest(callId, userinfo.id).then(() => {
              $promise.resolve();
            });
          } else {
            $promise.resolve();
            log.warn("Guest has not been invited to call: " + callId + ", guest: " + userinfo.id +
              " (" + userinfo.firstName + " " + userinfo.lastName + ")");
          }
        }).catch(err => {
          log.error("Failed to check call invitation: " + callId + " (" + inviteId + ")", err);
        });
      }

      $initUser.then(function(userinfo, token) {
        authToken = token;
        getContextInfo(userinfo.id).then(contextInfo => {
          getSettings().then(function(settings) {
            // General configuration to reflect the PLF environment  
            eXo.env.portal.profileOwner = userinfo.id;
            webConferencing.init(userinfo, contextInfo);
            settings.isCallApp = true; // Mark the settings as for a call page 
            provider.configure(settings);
            webConferencing.addProvider(provider);
            // XXX Subscribe to the call updates sooner to let Comet to initialize before making rmeote calls!
            subscribeUser(userinfo.id);
            var $promise = $.Deferred();
            if (isGuest) {
              inviteInCall(userinfo, $promise);
            } else {
              if (inviteId) {
                // exo user with invite link
                // invite user (it's outside exo user for the call) if it's allowed to connect (if inviteId is correct)
                inviteInCall(userinfo, $promise);
              } else {
                $promise.resolve();
              }
            }
            $promise.then(() => {
              webConferencing.getCall(callId).then(call => {
                var user = [];
                user = call.participants.filter(participant => {
                  return participant.id === userinfo.id;
                });
                if (user.length == 0) {
                  // Check in members
                  // In case when participants are not updated yet.
                  // See startCall() in WebconferencingService, syncMembersAndParticipants
                  if (call.owner.members) {
                    user = Object.values(call.owner.members).filter(member => {
                      return member.id === userinfo.id;
                    });
                  }
                  if (user.length == 0) {
                    alert("User is not allowed for this call");
                    return;
                  }
                }
                initCall(userinfo, call);
              }).catch(err => {
                log.error("Cannot init call: " + callId + " user: " + userinfo.id, err);
                alert("Error occurred while initializing the call."); // TODO i18n
              });
            });
          });
        });
      });
      window.addEventListener("beforeunload", beforeunloadListener);
    };
  };

  const meetApp = new MeetApp();
  meetApp.init();
});