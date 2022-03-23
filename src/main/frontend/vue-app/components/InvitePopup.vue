<template>
  <div id="invite-popup">
    <v-btn
      id="copy-paste-icon"
      :title="hoverMsg"
      icon
      @click="displayMessage"
      @click:append="displayMessage">
      <div class="d-flex flex-lg-row flex-column">
        <v-icon
          color="primary"
          class="align-self-start pe-1"
          size="25">fas fa-light fa-copy
        </v-icon>
      </div>
    </v-btn>
    <v-alert
      v-if="alert"
      id="alertMessage"
      type="success"
      border="left"
      colored-border
      dismissible>
      {{ textLinkMsg }}
    </v-alert>
  </div>
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
      hoverMsg: "Copy meeting link to copyboard",
      textLink: "Link copied to clipboard",
      active: false,
      isClicked: false,
      alert: false
    };
  },
  computed: {
    style() {
      return this.isClicked ? "display: block" : "display: none";
    },
    linkStyle() {
      return this.isClicked ? "display: none" : "display: block";
    },
    textLinkMsg(){
      return this.textLink;
    }

    //   color() {
    //     return this.isClicked ? "#46a546" : "#0376da";
    //   },
    //   title() {
    //     return this.isClicked ? "Link copied to clipboard" : this.url;
    //   }
  },
  methods: {
    displayMessage(message,typeAlert) {
      this.typeAlert=this.type;
      this.alert = true;
      this.message=this.textLinkMsg;
      // eslint-disable-next-line no-magic-numbers
      setTimeout(() => (this.alert = false), 5000);
      this.$refs.copyinput.select();
      document.execCommand("copy");
    }
    /*displayMessage(message) {
      this.isClicked = !this.isClicked;
      this.type="success";
      this.alert = true;
      this.message=this.textLinkMsg;
      window.setTimeout(() => (this.isClicked = !this.isClicked), 5000);
      //window.setTimeout(() => this.alert = false, 5000);
      // eslint-disable-next-line no-magic-numbers

      this.$refs.copyinput.select();
      document.execCommand("copy");
    }*/
  }
};
</script>
<style lang="less" scoped>
@import "../../skin/less/variables.less";

#invite-popup {
  position: absolute;
  bottom: 10%;
  left: 50%;
  transform: translateX(-50%);
  font-family: Helvetica, arial, sans-serif;
  font-weight: bold;

}

.v-alert__border--left, .v-alert__border--right {
  bottom: 0;
  top: 0;
}
</style>