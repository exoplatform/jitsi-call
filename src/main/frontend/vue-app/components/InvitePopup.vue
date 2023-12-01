<template>
  <v-app>
    <v-btn
      id="icon-popup"
      :title="hoverMsg"
      class="subject-text"
      style="margin: 0 4px 4px 4px;border:0px;border-radius:3px;"
      icon
      @click="displayMessage">
      <div>
        <input
          id="inputURL"
          ref="copyinput"
          :value="url"
          type="text"
          style="opacity: 0; width: 20px; position: absolute; top: 0;"
        />
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
    };
  },

  computed: {
    textLinkMsg(){
      return this.textLink;
    }
  },

  mounted(){
    const iframe = document.getElementById("jitsiConferenceFrame0");
    const elementAutoHide = iframe.contentWindow.document.getElementById("autoHide");
    const icon = document.getElementById("icon-popup");
    if (icon && elementAutoHide) {
      const link = iframe.contentWindow.document.createElement("link");
      link.rel = "stylesheet";
      link.type = "text/css";
      link.href = "/eXoSkin/skin/css/vuetify/vuetify-all.css";
      iframe.contentWindow.document.getElementsByTagName("HEAD")[0].appendChild(link);
      elementAutoHide.firstChild.prepend(icon);
    }
  },

  methods: {
    displayMessage() {
      this.$root.$emit("alert-message", this.textLinkMsg, "success");
      // eslint-disable-next-line no-magic-numbers
      const iframe = document.getElementById("jitsiConferenceFrame0");
      iframe.contentWindow.document.getElementById("inputURL").select();
      iframe.contentWindow.document.execCommand("copy");
    }
  }
};

</script>
