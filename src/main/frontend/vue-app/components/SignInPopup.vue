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
          <v-btn @click="eXoUserJoining">Sign In</v-btn>
        </v-card-actions>
        <v-card-text>Or request to join as a Guest</v-card-text>
        <v-card-actions>
          <v-text-field v-model="fullName" prepend-inner-icon="$account" type="text" label="Full name" solo required></v-text-field>
          <v-btn outlined @click="guestJoining">Join as a Guest</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      showDialog: true,
      fullName: "",
      // guestData: {firstName: "Mia", lastName: "Kirsh"}
    };
  },
  computed: {
    guestData() {
      const fullName = this.fullName.split(" ");
      const firstName = fullName[0];
      fullName.shift();
      const lastName = fullName.length > 1 ? fullName.join(" ") : fullName;
      return {
        firstName: firstName, lastName: lastName
      };
    }
  },
  methods: {
    guestJoining() {
      this.$emit("guestjoin", this.guestData);
      this.showDialog = false;
    },
    eXoUserJoining() {
      this.$emit("exouserjoin");
      this.showDialog = false;
    }
  }
};
</script>
 <style lang="less">
  .v-dialog__container {
    display: block;
}
</style>