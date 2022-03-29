<template>
  <v-app >
    <v-btn
      id="icon-popup"
      :title="hoverMsg"
      class="subject-text"
      style="margin: 0 4px 4px 4px;border:0px;border-radius:3px;"
      icon
      @click="displayMessage"
      @click:append="displayMessage">
      <div>
        <input
          ref="copyinput"
          :value="url"
          type="text"
          style="opacity: 0; width: 20px; position: absolute; top: 0;"/>
      </div>
      <div class="d-flex flex-lg-row flex-column">
        <v-icon
          id="icon-id"
          color="white"
          class="align-self-start pe-1">
          fas fa-light fa-copy
        </v-icon>
      </div>
    </v-btn>

    <div id="vAlert">
      <v-alert
        v-if="alert"
        type="success"
        border="left"
        colored-border
        dismissible>
        {{ textLinkMsg }}
      </v-alert>
    </div>

  </v-app>
</template>

<script>

export default {
  props: {
    // eslint-disable-next-line vue/require-default-prop
    url: {
      type: String,
      reguired: true
    }
  },

  data() {
    return {
      textLink: "Link copied to clipboard",
      hoverMsg: "Copy meeting link to copyboard",
      type:"",
      alert: false
    };
  },

  computed: {
    textLinkMsg(){
      return this.textLink;
    }
  },

  //j'essaie de faire l'appele du change position dans update pour catcher l'element après le chargement de la DOM
  //updated(){}

  mounted(){
    const iframe = document.getElementById("jitsiConferenceFrame0");
    const elementAutoHide = iframe.contentWindow.document.getElementById("autoHide");
    console.log(elementAutoHide);
    const icon = document.getElementById("icon-popup");
    if (icon && elementAutoHide) {
      //add vuetify-css in iframe
      const link = iframe.contentWindow.document.createElement("link");
      //set the attributes for link element
      link.rel = "stylesheet";
      link.type = "text/css";
      link.href = "/eXoSkin/skin/css/vuetify/vuetify-all.css";

      // Get HTML head element to append
      // link element to it
      iframe.contentWindow.document.getElementsByTagName("HEAD")[0].appendChild(link);

      console.log("icon:",icon);
      elementAutoHide.firstChild.prepend(icon);
    }

  },

  methods: {
    displayMessage() {
      this.alert = true;
      this.message=this.textLinkMsg;
      // eslint-disable-next-line no-magic-numbers
      setTimeout(() => (this.alert = false), 5000);
      this.$refs.copyinput.select();
      document.execCommand("copy");
    },

    waitInsert(){
      const autoHide = document.getElementById("autoHide");
      console.log("autoHide",autoHide);
      insertAfter(clonedIcon, autoHide);
    }

  }
};
</script>

<style lang="less" scoped>
@import "../../skin/less/variables.less";

#vAlert {
  position: fixed;
  bottom: 5%;
  left: 2%;
  background: none;
}
</style>
