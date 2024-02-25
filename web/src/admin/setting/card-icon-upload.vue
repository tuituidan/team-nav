<template>
  <el-card>
    <div slot="header" class="card-title">
      <span>卡片图标上传
        <com-tip tip="设置卡片图标选择默认图标，文件名用于搜索，须保持唯一"></com-tip>
      </span>
    </div>
    <div style="max-height: 325px;overflow: auto">
      <com-icon-uploader :action="uploadUrl"
                    show-name
                    show-edit
                    v-model="iconPaths"
                    @remove="onRemoveCardIcon"
                    @edit-name="onEditIconName"
                    :max-count="500"></com-icon-uploader>
    </div>
  </el-card>
</template>

<script>
export default {
  name: "card-icon-upload",
  components: {
    'com-icon-uploader': () => import('@/components/com-icon-uploader/index.vue'),
    'com-tip': () => import('@/components/com-tip/index.vue'),
  },
  data() {
    return {
      uploadUrl: process.env.VUE_APP_BASE_API + '/api/v1/upload/default',
      iconPaths: []
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$http.get('/api/v1/card/icons')
        .then(res => {
          this.iconPaths = res.map(filename => (`/ext-resources/images/default/${filename}`));
        });
    },
    onRemoveCardIcon(url) {
      this.$http.delete(`/api/v1/icon/${url.substring(url.lastIndexOf('/') + 1)}`)
        .then(() => {
          this.$modal.msgSuccess('删除成功');
        });
    },
    onEditIconName(url, newName) {
      this.$http.patch(`/api/v1/icon/${url.substring(url.lastIndexOf('/') + 1)}/to/${newName}`)
        .then(() => {
          this.$modal.msgSuccess('修改成功');
          this.init();
        });
    },
  }
}
</script>

<style scoped>

</style>
