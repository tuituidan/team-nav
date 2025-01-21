<template>
  <div>
    <button @click="wechatLogin">微信登录</button>
  </div>
</template>


<script>
export default {
  name: 'wechat-login',
  methods: {

    async wechatLogin() {

      try {
        const url = `/oauth2/authorization/wechat`
        window.location.href = url
      } catch (error) {
        console.error('Failed to generate state:', error)
      }
    }
  }
}

</script>


<style scoped>

</style>
