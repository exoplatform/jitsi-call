<template>
  <v-app>
    <div id="signin-popup" ref="signinpopup">
      <div v-show="showDialog" class="background"></div>
      <v-dialog
        ref="signin"
        v-model="showDialog"
        content-class="sign-in-dialog"
        hide-overlay
        width="500px"
        persistent
        no-click-animation
      >
        <v-card dark>
          <v-card-text>Sign In to join the call as eXo user</v-card-text>
          <v-card-actions>
            <v-btn color="#578dc9" large class="ui-action" @click="eXoUserJoining">Sign In</v-btn>
          </v-card-actions>
          <v-card-text>Or request to join as a Guest</v-card-text>
          <v-card-actions style="flex-flow: column;">
            <v-text-field
              v-model="fullName"
              light
              prepend-inner-icon="$account"
              type="text"
              class="ui-action"
              label="Full name"
              hide-details
              dense
              solo
              required
            ></v-text-field>
            <v-btn class="ui-action" large outlined @click="guestJoining">Join as a Guest</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      showDialog: true,
      fullName: ""
    };
  },
  computed: {
    guestData() {
      const whiteSpace = this.fullName.indexOf(" ");
      const firstName = this.fullName.substring(0, whiteSpace);
      const lastName = this.fullName.substring(whiteSpace);
      return {
        firstName: firstName,
        lastName: lastName
      };
    }
  },
  methods: {
    guestJoining() {
      this.showDialog = false;
      this.$emit("guestjoin", this.guestData);
    },
    eXoUserJoining() {
      this.showDialog = false;
      this.$emit("exouserjoin");
    }
  }
};
</script>
 <style lang="less" scoped>
.v-dialog__container {
  display: block;
}
</style>
<style lang="less">
.v-dialog__content {
  background: radial-gradient(50% 50% at 50% 50%, #2a3a4b 20.83%, #1e2a36 100%);
  .v-dialog {
    &.sign-in-dialog {
      overflow-y: hidden;
      height: 400px;
      // box-shadow: 0 0px 40px -5px rgba(255, 255, 255, 0.12),
      //   0 0px 20px -5px rgba(255, 255, 255, 0.12),
      //   0 0px 40px -5px rgba(255, 255, 255, 0.12);
      box-shadow: none;
      .theme--dark.v-card {
        background-color: #385268;
        height: 400px;
        padding: 50px;
        .v-card__text {
          padding: 16px 10px;
          font-size: 24px;
          font-family: Helvetica, arial, sans-serif;
          color: #fff;
        }
        .v-card__actions {
          .ui-action {
            width: 100%;
            &.v-text-field {
              margin-bottom: 15px;
              .v-icon {
                &.mdi {
                  border-right: 1px solid;
                  padding: 0 7px;
                }
              }
              .v-text-field__slot {
                .v-label {
                  padding-left: 15px;
                }
                input {
                  padding-left: 15px;
                }
              }
            }
            &.v-btn {
              text-transform: none;
              font-family: Helvetica, arial, sans-serif;
              font-weight: bold;
              &.v-btn--outlined {
                font-weight: initial;
              }

              .v-btn__content {
                font-size: 18px;
              }
            }
          }
        }
      }
    }
  }
}
</style>