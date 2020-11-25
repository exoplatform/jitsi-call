<template>
  <div id="invite-popup">
    <v-text-field
      :value="url"
      background-color="#0376da"
      class="btn-copy"
      append-icon="mdi-content-copy"
      solo
      hide-details
      dark
      type="text"
      readonly
      style="text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;"
      @click:append="copyUrl"
    ></v-text-field>
    <input
      ref="copyinput"
      :value="url"
      type="text"
      style="opacity: 0; width: 160px"
      class="btn-copy" />
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
      textLink: "Copy meeting link"
    };
  },
  methods: {
    copyUrl() {
      this.$refs.copyinput.select();
      document.execCommand("copy");
    }
  }
};
</script>
<style lang="less" scoped>
@import "../../skin/less/variables.less";
.v-input {
  font-size: 14px;
}
 input[type="text"] {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
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