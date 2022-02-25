<template>
  <div
    id="loading-container"
    class="bg-gray-900 min-h-screen mx-auto my-auto grid grid-cols-1 content-center">
    <p class="text-center text-yellow-400 my-4">Redirect to login...</p>
  </div>
</template>

<script>
export default {
  name: 'IndexPage',
  layout: 'CleanLayout',
  mounted() {
    setInterval(this.login, 250);
  },
  methods: {
    async login() {
      await this.$auth.loginWith('oidc');
    },
  },
};
</script>

<style scoped>
</style>
