<template>
  <div
    id="loading-container"
    class="bg-gray-900 min-h-screen mx-auto my-auto grid grid-cols-1 content-center"
  >
    <Spinner
      id="spinner"
      fill="#fbbf24"
      fill-opacity="0.2"
      class="mx-auto my-auto"
      width="135px"
      height="140px"
    />
    <p class="text-center text-yellow-400 my-4">Redirect to login...</p>
  </div>
</template>

<script>
import Spinner from '~/assets/bars.svg?inline'
export default {
  name: 'IndexPage',
  components: { Spinner },
  layout: 'CleanLayout',
  mounted() {
    setInterval(this.login, 250)
  },
  methods: {
    async login() {
      await this.$auth.loginWith('oidc')
    },
  },
}
</script>

<style scoped>
#spinner {
  vertical-align: middle;
}
</style>
