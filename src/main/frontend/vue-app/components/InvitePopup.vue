<template>
  <div
    id="invite-popup"
    class="invite-copy-link">
    <input
      id="inputURL"
      ref="copyinput"
      :value="url"
      type="text"
      tabindex="-1"
      aria-hidden="true">
    <button
      id="icon-popup"
      :title="hoverMsg"
      :aria-label="hoverMsg"
      type="button"
      @click="displayMessage">
      <svg
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
        aria-hidden="true"
        focusable="false">
        <path d="M19 21H8V7h11v14zm0-16H8a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h11a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2zM16 1H4a2 2 0 0 0-2 2v14h2V3h12V1z" />
      </svg>
    </button>
  </div>
</template>

<script>
// Styles of the copy link button, injected in the Jitsi iframe document since the
// button is moved there and stylesheets of the parent document do not apply to it.
const COPY_LINK_STYLE = `
  #invite-popup {
    position: relative;
    display: inline-flex;
    align-items: center;
    vertical-align: middle;
  }
  #invite-popup #inputURL {
    position: absolute;
    top: 0;
    left: 0;
    width: 20px;
    height: 20px;
    margin: 0;
    padding: 0;
    border: 0;
    background: transparent;
    opacity: 0;
    pointer-events: none;
  }
  #invite-popup #icon-popup {
    display: inline-flex !important;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    min-width: 0;
    margin: 0 4px;
    padding: 0 !important;
    border: 0 !important;
    border-radius: 4px;
    background: transparent !important;
    box-shadow: none !important;
    color: #fff !important;
    cursor: pointer;
  }
  #invite-popup #icon-popup:hover,
  #invite-popup #icon-popup:focus {
    background: rgba(255, 255, 255, 0.2) !important;
    outline: none;
  }
  #invite-popup #icon-popup svg {
    width: 16px;
    height: 16px;
    fill: currentColor;
    pointer-events: none;
  }
`;

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
      textLink: "Link copied to clipboard",
      hoverMsg: "Copy meeting link to copyboard",
      type:"",
    };
  },

  computed: {
    textLinkMsg(){
      return this.textLink;
    }
  },

  mounted(){
    const iframe = document.getElementById("jitsiConferenceFrame0");
    const frameDocument = iframe && iframe.contentWindow && iframe.contentWindow.document;
    const elementAutoHide = frameDocument && frameDocument.getElementById("autoHide");
    if (!elementAutoHide || !elementAutoHide.firstChild) {
      return;
    }
    const style = frameDocument.createElement("style");
    style.type = "text/css";
    style.textContent = COPY_LINK_STYLE;
    frameDocument.getElementsByTagName("HEAD")[0].appendChild(style);
    elementAutoHide.firstChild.prepend(this.$el);
  },

  methods: {
    displayMessage() {
      this.$root.$emit("alert-message", this.textLinkMsg, "success");
      const input = this.$refs.copyinput;
      input.select();
      input.ownerDocument.execCommand("copy");
    }
  }
};

</script>
