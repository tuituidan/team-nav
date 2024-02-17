<template>
  <div class="setting-panel">
    <base-setting class="card-style"></base-setting>
    <card-icon-upload class="card-style card-icon-upload-panel"></card-icon-upload>
  </div>
</template>

<script>
export default {
  name: "settings-index",
  components: {
    'base-setting': () => import('@/admin/setting/base-setting.vue'),
    'card-icon-upload': () => import('@/admin/setting/card-icon-upload.vue'),
  }
}
</script>

<style lang="scss" scoped>
.setting-panel {
  display: flex;
  flex-wrap: wrap;
  padding: 10px;
}

.card-style {
  width: 490px;

  ::v-deep .btn-save {
    float: right;
    padding: 3px 0
  }

  ::v-deep .card-title {
    margin: 0 0 5px 0;
  }

}

.card-icon-upload-panel {
  flex: auto;
  margin-left: 10px;
}
</style>
