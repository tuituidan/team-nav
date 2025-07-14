<template>
  <el-tooltip content="微信登录" placement="bottom" effect="light">
    <el-button type="text" @click="wechatLogin">
      <img src="./images/wechat-icon.png" alt="微信登录" class="wechat-icon" />
    </el-button>
  </el-tooltip>
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
.wechat-icon {
  width: 24px; /* 图标大小 */
  height: 24px;
  transition: transform 0.3s ease; /* 添加 hover 动画 */
}

.wechat-icon:hover {
  transform: scale(1.1); /* 鼠标悬停时放大图标 */
}
</style>
