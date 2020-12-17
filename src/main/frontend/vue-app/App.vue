<template>
  <div id="app">
    <Call />
  </div>
</template>

<script>
import Call from "./components/Call.vue";

export default {
  name : "App",
  components: {
    Call
  }
};
</script>

<style lang="less">
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    input[readonly],
    input[type="text"] {
      background-color: transparent !important;
      border: none;
      margin-bottom: 0;
      height: unset;
      cursor: default;
      &:focus {
        background: transparent;
        box-shadow: none;
        border: none;
        outline: none;
      }
    }
  }
</style>