<template>
  <v-app>
    <img src="/jitsi/images/logo.png ">
    <div id="signin-popup" ref="signinpopup">
      <div v-show="showDialog" class="background"></div>
      <v-dialog
        ref="signin"
        v-model="showDialog"
        :hide-overlay="true"
        content-class="sign-in-dialog"
        width="500px"
        persistent
        no-click-animation>
        <v-card dark>
          <v-card-text>Sign In to join the call as eXo user</v-card-text>
          <v-card-actions>
            <v-btn
              :elevation="0"
              color="#578dc9"
              large
              class="ui-action"
              @click="eXoUserJoining">Sign In</v-btn>
          </v-card-actions>
          <v-card-text class="text-guest">Or request to join as a Guest</v-card-text>
          <v-card-actions style="flex-flow: column;">
            <div style="position: relative; width: 100%;">
              <v-text-field
                ref="textfield"
                v-model="fullName"
                light
                prepend-inner-icon="$account"
                type="text"
                class="ui-action guestName"
                label="Full name"
                hide-details
                color="#999"
                dense
                solo
                flat
                required></v-text-field>
              <i class="iconUser"></i>
            </div>
            <v-btn class="ui-action" large outlined @click="guestJoining">Join as Guest</v-btn>
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
  .v-dialog__container {
    display: block;
  }
</style>
<style lang="less">
html {
  overflow-y: hidden;
}
.v-application {
  .elevation-0 {
    box-shadow: none !important;
  }
}
.v-dialog__content {
  background: radial-gradient(50% 50% at 50% 50%, #2a3a4b 20.83%, #1e2a36 100%);
  .v-dialog {
    &.sign-in-dialog {
      overflow-y: hidden;
      height: 400px;
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
          &.text-guest {
            font-size: 19px;
          }
        }
        .v-card__actions {
          .ui-action {
            width: 100%;
            &.v-text-field {
              margin-bottom: 15px;
              &.v-text-field--solo {
                .v-label {
                  &:focus,:active {
                    outline: none;
                  }
                }
                }
              .v-icon {
                &.mdi {
                  border-right: 1px solid rgb(178 188 202);
                  padding: 0 10px;
                  &.mdi-account::before {
                    opacity: 0;
                  }
                }
              }
              .theme--light {
                &.v-icon {
                  color: #999;
                }
              }
              .v-text-field__slot {
                .v-label {
                  padding-left: 15px;
                  color: #999;
                }
                input {
                  padding-left: 15px;
                  &::placeholder {
                    color: #999;
                  }
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
          .guestName input {
            box-shadow: none;
            font-size: 15px;
          }
          .iconUser {
            position: absolute;
            top: 4%;
            left: 1%;
            height: 30px;
            width: 30px;
            background: #ffffff url(/eXoSkin/skin/images/themes/default/username.png) no-repeat left -3px;
          }
        }
      }
    }
  }
}
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