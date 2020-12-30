<template>
  <div id="app">
    <Call />
  </div>
</template>

<script>
import Call from "./components/Call.vue";
import { EventBus } from "./main.js";
import { storage } from "./main.js";

const myEvent = new CustomEvent("iframe_message", { detail: {url: "http://localhost:8080"}, bubbles: true });

export default {
  name: "App",
  components: {
    Call
  },
  data() {
    return {
      storage: {}
    };
  },
  created() {
    this.storage = storage;
    EventBus.$emit("appCreated", { storage: storage });
  },
  mounted() {
    window.parent.postMessage("iframe_message");
    window.addEventListener("pointermove", () => {
      window.parent.dispatchEvent(myEvent);
    });
  }
};
</script>

<style lang="less">
html {
  overflow-y: hidden;
  body {
    height: 100vh;
  }
}
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
  #meet {
    height: 100%;
  }
}
</style>