<template>
  <div class="setting-panel">
    <base-setting class="card-style"></base-setting>
    <card-icon-upload class="card-style card-icon-upload-panel"></card-icon-upload>
    <email-setting class="card-style card-email-panel"></email-setting>
    <data-backup class="card-style card-data-backup"></data-backup>
  </div>
</template>

<script>
export default {
  name: "settings-index",
  components: {
    'base-setting': () => import('@/admin/setting/base-setting.vue'),
    'email-setting': () => import('@/admin/setting/email-setting.vue'),
    'card-icon-upload': () => import('@/admin/setting/card-icon-upload.vue'),
    'data-backup': () => import('@/admin/setting/data-backup.vue'),
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
  width: calc(100% - 500px);
  margin-left: 10px;
}

.card-email-panel {
  margin-top: 10px;
}

.card-data-backup {
  margin-left: 10px;
  margin-top: 10px;
}
</style>
