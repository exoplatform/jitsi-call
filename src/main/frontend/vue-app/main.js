// import "material-design-icons-iconfont/dist/material-design-icons.css";
import Vue from "vue";
import Vuetify from "vuetify";
import App from "./App.vue";
import InvitePopup from "./components/InvitePopup.vue";
import SignInScreen from "./components/SignInScreen.vue";
import ExitScreen from "./components/ExitScreen.vue";
import "vuetify/dist/vuetify.min.css";

//Vue.config.productionTip = false
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
const localePortlet = "locale.jitsi";
const resourceBundleName = "jitsi";
const url = `/portal/rest/i18n/bundle/${localePortlet}.${resourceBundleName}-${lang}.json`;

export function init() {
  // getting locale ressources
  // return exoi18n.loadLanguageAsync(lang, url).then((i18n) => {
  return new window.Vue({
    el: "#app",
    components: {
      App,
    },
    vuetify,
    render: function(h) {
      return h(App);
    },
  });
}

export function initCallLink(url) {
  // getting locale ressources
  // return exoi18n.loadLanguageAsync(lang, url).then((i18n) => {
  return new window.Vue({
    el: "#invite-popup",
    components: {
      InvitePopup,
    },
    vuetify,
    render: function(h) {
      return h(InvitePopup, {
        props: {
          url: url,
        },
      });
    },
  });
}

export function initSignInScreen(hideLoader, showLoader) {
  const result = new Promise((resolve, reject) => {
    new window.Vue({
      el: "#signin-popup",
      components: {
        SignInScreen,
      },
      created() {
        hideLoader();
      },
      vuetify,
      render: function(h) {
        const thevue = this;
        return h(SignInScreen, {
          on: {
            exouserjoin: function(){
              showLoader();
              resolve();
              thevue.$destroy();
            },
            guestjoin: function($event){
              showLoader();
              reject($event);
              thevue.$destroy();
            },
          }
        });
      },
    });
  });
  return result;
}

export function initExitScreen() {
  const result = new Promise((resolve, reject) => {
    new window.Vue({
      el: "#exit-screen",
      components: {
        ExitScreen,
      },
      vuetify,
      render: function(h) {
        return h(ExitScreen);
      },
    });
  });
  return result;
}
