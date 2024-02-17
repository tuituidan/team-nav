<template>
  <el-tooltip :content="tip">
    <i class="el-icon-warning-outline"></i>
  </el-tooltip>
</template>

<script>
export default {
  name: "com-tip",
  props: {
    tip: String
  }
}
</script>

<style scoped>
.el-icon-warning-outline {
  margin-left: 15px;
  vertical-align: -0.1em;
  font-size: 20px;
  color: #2d8cf0;
}
</style>
