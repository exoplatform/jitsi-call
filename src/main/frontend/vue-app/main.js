// import "material-design-icons-iconfont/dist/material-design-icons.css";
import Vue from "vue";
import App from "./App.vue";
import InvitePopup from "./components/InvitePopup.vue";
import SignInPopup from "./components/SignInPopup.vue";
import Vuetify from "vuetify";
import "vuetify/dist/vuetify.min.css";

//Vue.config.productionTip = false
Vue.use(Vuetify);
const vuetify = new Vuetify({
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

const lang =
  // eslint-disable-next-line no-extra-parens
  (eXo && eXo.env && eXo.env.portal && eXo.env.portal.language) || "en";
const localePortlet = "locale.jitsi";
const resourceBundleName = "jitsi";
const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/${localePortlet}.${resourceBundleName}-${lang}.json`;

export function init() {
  // getting locale ressources
  // return exoi18n.loadLanguageAsync(lang, url).then((i18n) => {
  return new Vue({
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
  return new Vue({
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

export function initSignInPopup(hideLoader, showLoader) {
  const result = new Promise((resolve, reject) => {
    new Vue({
      el: "#signin-popup",
      components: {
        SignInPopup,
      },
      created() {
        hideLoader();
      },
      vuetify,
      render: function(h) {
        const thevue = this;
        return h(SignInPopup, {
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
