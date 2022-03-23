<template>
  <v-app >
    <v-btn
      id="icon-popup"
      :title="hoverMsg"
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
          color="white"
          class="align-self-start pe-1"
          size="25">fas fa-light fa-copy
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

    this.changePosition();

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



    changePosition(){
      const icon = document.getElementById("icon-popup");
      console.log("icon:",icon);
      const clonedIcon = icon.cloneNode(true);
      console.log("clonedIcon:",clonedIcon);
      /*icon.remove();*/
      const wait = 5000;

      setTimeout(this.waitInsert,wait);


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

#icon-popup {
  position: fixed;
  bottom: 94.5%;
  left: 42.5%;
  background: none;
}
#vAlert {
  position: fixed;
  bottom: 5%;
  left: 2%;
  background: none;
}
</style>
