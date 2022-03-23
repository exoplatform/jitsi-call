<template>
  <v-app id="alert-popup">
    <v-alert
      id="alertMessage"
      type="success"
      border="left"
      colored-border
      dismissible>
      {{ textLink }}
    </v-alert>
  </v-app>

</template>

<script>
export default {
  data() {
    return {
      textLink: "Link copied to clipboard",
    };
  }

};
</script>

<style lang="less" scoped>
@import "../../skin/less/variables.less";

#alert-popup {
  position: fixed;
  bottom: 5%;
  left: 10%;
  transform: translateX(-50%);
  font-family: Helvetica, arial, sans-serif;
  font-weight: bold;
  background: none;
}
</style>