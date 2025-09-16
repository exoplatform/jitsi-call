<template>
  <div id="meet">
    <h1>{{ $t('call.title') }}</h1>
  </div>
</template>

<script>
export default {
  name: "Call",
  data() {
    return {
      backendResponse: [],
      errors: []
    };
  }
};
</script>