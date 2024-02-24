<template>
  <el-tooltip content="文档地址">
    <div>
      <svg-icon icon-class="question" @click="gotoDocs"/>
    </div>
  </el-tooltip>
</template>

<script>
export default {
  name: "doc-index",
  methods: {
    gotoDocs() {
      window.open(`${process.env.VUE_APP_PROXY_URL}/docs/`)
    },
  }
}
</script>

<style scoped>

</style>
