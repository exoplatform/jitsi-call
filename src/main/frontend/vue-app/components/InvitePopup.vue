<template>
  <div id="invite-popup">
    <v-text-field
      :value="url"
      :style="linkStyle"
      background-color="#0376da"
      class="btn-copy"
      append-icon="mdi-content-copy"
      solo
      single-line
      hide-details
      dark
      type="text"
      readonly
      @click="copyUrl"
      @click:append="copyUrl"
    ></v-text-field>
    <v-text-field
      :value="textLink"
      :style="style"
      background-color="#46a546"
      class="btn-copy"
      append-icon="mdi-check"
      solo
      single-line
      hide-details
      dark
      type="text"
      readonly
    ></v-text-field>
    <input
      ref="copyinput"
      :value="url"
      type="text"
      style="opacity: 0; width: 160px"
      class="btn-copy"
    />
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
      textLink: "Link copied to clipboard",
      isClicked: false
    };
  },
  computed: {
    style() {
      return this.isClicked ? "display: block" : "display: none";
    },
    linkStyle() {
      return this.isClicked ?  "display: none" : "display: block";
    }
  //   color() {
  //     return this.isClicked ? "#46a546" : "#0376da";
  //   },
  //   title() {
  //     return this.isClicked ? "Link copied to clipboard" : this.url;
  //   }
  },
  methods: {
    copyUrl() {
      this.isClicked = !this.isClicked;
      // eslint-disable-next-line no-magic-numbers
      setTimeout(() => this.isClicked = !this.isClicked, 5000);
      this.$refs.copyinput.select();
      document.execCommand("copy");
    }
  }
};
</script>
<style lang="less" scoped>
@import "../../skin/less/variables.less";
.btn-copy {
  &.v-input {
    &.v-text-field {
      font-size: 14px;
    }
  }
}
.btn-copy {
  cursor: pointer; 
}
#invite-popup {
  position: absolute;
  top: 10%;
  left: 50%;
  transform: translateX(-50%);
  font-family: Helvetica, arial, sans-serif;
  font-weight: bold;
  &.btn-copy {
    cursor: pointer;
  }
  .v-text-field {
    width: 370px;
    cursor: pointer;
  }
}
</style>
<style scoped>
.btn-copy.v-text-field >>> input{
      text-overflow: ellipsis;
      display: inline-block !important;
      white-space: nowrap;
      overflow: hidden;
      margin-right: 25px;
    }
</style>