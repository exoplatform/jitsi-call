import Vue from "vue";
import Vuetify from "vuetify";
import App from "./App.vue";
import InvitePopup from "./components/InvitePopup.vue";
import SignInScreen from "./components/SignInScreen.vue";
import ExitScreen from "./components/ExitScreen.vue";
import "vuetify/dist/vuetify.min.css";

Vue.use(Vuetify);
const vuetify = new Vuetify({
  dark: true,
  icons: {
    iconfont: "mdi",
    values: {
      copy: "mdi-content-copy",
      check: "mdi-check",
      account: "mdi-account",
    },
  },
});

const lang = window.eXo && eXo.env && eXo.env.portal && eXo.env.portal.language || "en";
const resourceBundleName = "jitsi-call";
const url = `/jitsi/api/v1/lang/${lang}`;

// Main init function
export function init(settings = {}) {
  exoi18n.loadLanguageAsync(lang, url).then((i18n) => {
    const props = Object.assign({}, settings, {
      i18n,
      language: lang,
      resourceBundleName,
    });

    // Root App
    new Vue({
      render: h => h(App, { props }),
      i18n,
      vuetify,
    }).$mount("#app");
  });
}

// Invite popup
export function initCallLink(inviteUrl, settings = {}) {
  exoi18n.loadLanguageAsync(lang, url).then((i18n) => {
    const props = Object.assign({}, settings, { i18n, url: inviteUrl });
    new Vue({
      render: h => h(InvitePopup, { props }),
      i18n,
      vuetify,
    }).$mount("#invite-popup");
  });
}

// Sign-in screen
export function initSignInScreen(hideLoader, showLoader, settings = {}) {
  exoi18n.loadLanguageAsync(lang, url).then((i18n) => {
    const props = Object.assign({}, settings, { i18n });
    new Vue({
      render: h =>
        h(SignInScreen, {
          props,
          on: {
            exouserjoin: () => showLoader(),
            guestjoin: () => showLoader(),
          },
        }),
      i18n,
      vuetify,
    }).$mount("#signin-popup");
    hideLoader();
  });
}

// Exit screen
export function initExitScreen(settings = {}) {
  exoi18n.loadLanguageAsync(lang, url).then((i18n) => {
    const props = Object.assign({}, settings, { i18n });
    new Vue({
      render: h => h(ExitScreen, { props }),
      i18n,
      vuetify,
    }).$mount("#exit-screen");
  });
}
