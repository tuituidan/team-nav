<template>
  <el-upload
    :action="uploadUrl"
    :file-list="fileList"
    :accept="accept"
    :on-remove="fileRemove"
    :on-success="fileUploadSuccess"
    :on-error="fileUploadError"
    :multiple="multiple"
    :limit="limit">
    <el-button icon="el-icon-upload" size="small">
      <slot></slot>
    </el-button>
  </el-upload>
</template>

<script>
export default {
  name: "file-uploader-index",
  props: {
    type: {
      type: String,
      required: true,
    },
    accept: {
      type: String,
    },
    multiple: {
      type: Boolean,
      default: false
    },
    fileList: {
      type: Array,
    },
    limit: {
      type: Number,
      default: 10
    }
  },
  data() {
    return {
      uploadUrl: `${process.env.VUE_APP_BASE_API}/api/v1/upload/` + this.type,
    }
  },
  methods: {
    fileRemove(file, fileList) {
      this.callBackFileList(fileList);
    },
    fileUploadSuccess(response, file, fileList) {
      this.callBackFileList(fileList);
    },
    fileUploadError(err) {
      this.$modal.msgError(err.message);
    },
    callBackFileList(fileList) {
      this.$emit('file-change', fileList.map(item => ({name: item.name, path: item.response || item.id})));
    },
  }
}
</script>

<style scoped>

</style>
