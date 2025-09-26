// import "material-design-icons-iconfont/dist/material-design-icons.css";
import Vue from "vue";
import Vuetify from "vuetify";
import App from "./App.vue";
import InvitePopup from "./components/InvitePopup.vue";
import SignInScreen from "./components/SignInScreen.vue";
import ExitScreen from "./components/ExitScreen.vue";
import "vuetify/dist/vuetify.min.css";

window.Vue.use(window.Vuetify);

const vuetify = new window.Vuetify({
  dark: true,
  icons: {
    iconfont: "mdi",
    values: {
      copy: "mdi-content-copy",
      check: "mdi-check",
      account: "mdi-account"
    }
  },
});

const lang = window.eXo && eXo.env && eXo.env.portal && eXo.env.portal.language || "en";

const langUrl = `/jitsi/api/v1/lang/${lang}`;

// init main app
export function init(exoi18n) {
  return exoi18n.loadLanguageAsync(lang, langUrl).then(i18n => {
    return new window.Vue({
      el: "#app",
      components: { App },
      vuetify,
      i18n,
      render: h => h(App),
    });
  });
}

// init invite popup
export function initCallLink(meetingUrl, exoi18n) {
  return exoi18n.loadLanguageAsync(lang, langUrl).then(i18n => {
    return new window.Vue({
      el: "#invite-popup",
      components: { InvitePopup },
      vuetify,
      i18n,
      render: h => h(InvitePopup, { props: { url: meetingUrl } }),
    });
  });
}

// init sign-in screen
export function initSignInScreen(hideLoader, showLoader, exoi18n) {
  return exoi18n.loadLanguageAsync(lang, langUrl).then(i18n => {
    return new Promise((resolve, reject) => {
      new window.Vue({
        el: "#signin-popup",
        components: { SignInScreen },
        created() { hideLoader(); },
        vuetify,
        i18n,
        render(h) {
          return h(SignInScreen, {
            on: {
              exouserjoin: () => { showLoader(); resolve(); this.$destroy(); },
              guestjoin: ($event) => { showLoader(); reject($event); this.$destroy(); },
            }
          });
        },
      });
    });
  });
}

// init exit screen
export function initExitScreen(exoi18n) {
  return exoi18n.loadLanguageAsync(lang, langUrl).then(i18n => {
    return new Promise((resolve) => {
      new window.Vue({
        el: "#exit-screen",
        components: { ExitScreen },
        vuetify,
        i18n,
        render: h => h(ExitScreen),
      });
    });
  });
}