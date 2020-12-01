<template>
  <div id="signin-popup">
    <v-dialog
      ref="signin"
      v-model="showDialog"
      content-class="sing-in-dialog"
      hide-overlay
      width="400px"
      height="400px">
      <v-card>
        <v-card-text>Sign In to join the call as eXo user</v-card-text>
        <v-card-actions>
          <v-btn>Sign In</v-btn>
        </v-card-actions>
        <v-card-text>Or request to join as a Guest</v-card-text>
        <v-card-actions>
          <v-text-field label="Full name" required></v-text-field>
          <v-btn outlined>Join as a Guest</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      showDialog: true
    };
  }
};
</script>
 <style lang="less">
  .v-dialog__container {
    display: block;
}
</style>