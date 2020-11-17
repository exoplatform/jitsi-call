import Vue from "vue";
import App from "./App.vue";

//Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount("#app");

export function init(webconferencing, provider, callApp) { 
  console.log(webconferencing);
  console.log(provider);
  console.log(callApp);
  
}