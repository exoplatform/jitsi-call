<template>
  <div id="invite-popup">
    <v-text-field
      :value="url"
      :style="linkStyle"
      background-color="#0376da"
      class="btn-copy"
      append-icon="$copy"
      solo
      single-line
      hide-details
      dark
      type="text"
      readonly
      @click="copyUrl"
      @click:append="copyUrl"></v-text-field>
    <v-text-field
      :value="textLink"
      :style="style"
      background-color="#46a546"
      class="btn-copy"
      append-icon="$check"
      solo
      single-line
      hide-details
      dark
      type="text"
      readonly></v-text-field>
    <input
      ref="copyinput"
      :value="url"
      type="text"
      style="opacity: 0; width: 160px; position: absolute; top: 0;"
      class="btn-copy"/>
  </div>
</template>

<script>
export default {
  props: {
    // eslint-disable-next-line vue/require-default-prop
    url: {
      type: String,
      reguired: true,
    }
  },
  data() {
    return {
      textLink: "Link copied to clipboard",
      isClicked: false,
      isVisible: false
    };
  },
  computed: {
    style() {
      return this.isClicked ? "display: block" : "display: none";
    },
    linkStyle() {
      return this.isClicked ? "display: none" : "display: block";
    }
  },
  methods: {
    copyUrl() {
      this.isClicked = !this.isClicked;
      // eslint-disable-next-line no-magic-numbers
      setTimeout(() => (this.isClicked = !this.isClicked), 5000);
      this.$refs.copyinput.select();
      document.execCommand("copy");
    },
    showInvitePopup(e) {
      this.isVisible = true;
      // eslint-disable-next-line no-magic-numbers
      setTimeout(() => (this.isVisible = false), 5000);
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
  bottom: 10%;
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
.btn-copy.v-text-field >>> input {
  text-overflow: ellipsis;
  display: inline-block !important;
  white-space: nowrap;
  overflow: hidden;
  margin-right: 25px;
}
</style>
<style lang="less">
#invite-popup {
  input[readonly],
  input[type="text"] {
    background-color: transparent !important;
    border: none;
    margin-bottom: 0;
    height: unset;
    cursor: copy;
    &:focus {
      background: transparent;
      box-shadow: none;
      border: none;
      outline: none;
    }
  }
}
</style>