<template>
  <div
    id="invite-popup"
    :class="{ copied: copied }"
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
      @click="copyLink">
      <svg
        class="invite-copy-link__copy"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
        aria-hidden="true"
        focusable="false">
        <path d="M19 21H8V7h11v14zm0-16H8a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h11a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2zM16 1H4a2 2 0 0 0-2 2v14h2V3h12V1z" />
      </svg>
      <svg
        class="invite-copy-link__done"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
        aria-hidden="true"
        focusable="false">
        <path d="M21 7L9 19l-5.5-5.5 1.41-1.41L9 16.17 19.59 5.59 21 7z" />
      </svg>
    </button>
    <span
      class="invite-copy-link__feedback"
      role="status">{{ textLink }}</span>
  </div>
</template>

<script>
/** Id of the stylesheet injected in the Jitsi iframe, to inject it only once. */
const COPY_LINK_STYLE_ID = "invite-popup-style";

/** How long the copy confirmation stays displayed, in ms. */
const COPIED_FEEDBACK_DELAY = 3000;

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
  #invite-popup .invite-copy-link__copy {
    display: block;
  }
  #invite-popup .invite-copy-link__done,
  #invite-popup .invite-copy-link__feedback {
    display: none;
  }
  #invite-popup.copied .invite-copy-link__copy {
    display: none;
  }
  #invite-popup.copied .invite-copy-link__done {
    display: block;
  }
  #invite-popup.copied .invite-copy-link__feedback {
    display: inline-block;
  }
  #invite-popup .invite-copy-link__feedback {
    margin-right: 8px;
    color: #fff;
    font-family: Helvetica, Arial, sans-serif;
    font-size: 13px;
    line-height: 1;
    white-space: nowrap;
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
      copied: false,
      copiedTimeout: null,
    };
  },

  mounted(){
    const iframe = document.getElementById("jitsiConferenceFrame0");
    const frameDocument = iframe && iframe.contentWindow && iframe.contentWindow.document;
    const elementAutoHide = frameDocument && frameDocument.getElementById("autoHide");
    if (!elementAutoHide || !elementAutoHide.firstChild) {
      return;
    }
    if (!frameDocument.getElementById(COPY_LINK_STYLE_ID)) {
      const style = frameDocument.createElement("style");
      style.id = COPY_LINK_STYLE_ID;
      style.type = "text/css";
      style.textContent = COPY_LINK_STYLE;
      frameDocument.getElementsByTagName("HEAD")[0].appendChild(style);
    }
    const previous = frameDocument.getElementById("invite-popup");
    if (previous && previous !== this.$el) {
      previous.remove();
    }
    elementAutoHide.firstChild.prepend(this.$el);
  },

  beforeDestroy() {
    if (this.copiedTimeout) {
      clearTimeout(this.copiedTimeout);
    }
  },

  methods: {
    copyLink() {
      this.showCopiedFeedback();
      if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(this.url).catch(() => this.copyWithSelection());
      } else {
        this.copyWithSelection();
      }
    },

    copyWithSelection() {
      const input = this.$refs.copyinput;
      input.select();
      input.ownerDocument.execCommand("copy");
    },

    showCopiedFeedback() {
      this.copied = true;
      if (this.copiedTimeout) {
        clearTimeout(this.copiedTimeout);
      }
      this.copiedTimeout = setTimeout(() => {
        this.copied = false;
        this.copiedTimeout = null;
      }, COPIED_FEEDBACK_DELAY);
    }
  }
};

</script>
