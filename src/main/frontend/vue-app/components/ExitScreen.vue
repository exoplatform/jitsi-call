<template>
  <v-app>
    <img src="/jitsi/images/logo.png ">
    <div id="exit-screen" ref="exitscreen">
      <p>This call already finished. Now you can close this window.</p>
    </div>
  </v-app>
</template>

<script>
export default {
  name: "ExitScreen"
};
</script>
 <style lang="less" scoped>
  #exit-screen {
    height: 100vh;
    width: 100%;
    background: radial-gradient(50% 50% at 50% 50%, #2a3a4b 20.83%, #1e2a36 100%);
    display: flex;
    justify-content: center;
    align-items: center;
    p {
      font-size: 24px;
      font-family: Helvetica, arial, sans-serif;
      color: #fff;
      padding: 0 25px;
    }
  }
 img {
   position: absolute;
   top: 30px;
   left: 50px;
   z-index: 2000; 
   object-fit: contain;
   height: 70px;
   width: 140px;
   opacity: 0.7;
 }
</style>
<style lang="less">
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
      -webkit-box-shadow: none;
      -moz-box-shadow: none;
      border: none;
      outline: none;
    }
  }
  input:focus:invalid:focus {
     box-shadow: none;
     border-color: transparent;
     -webkit-box-shadow: none;
     -moz-box-shadow: none;
  }
</style>