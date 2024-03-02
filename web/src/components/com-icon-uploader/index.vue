<template>
  <div>
    <div class="demo-upload-list" v-for="item in uploadList" :key="item.url">
      <template v-if="item.status === 'success'">
        <div v-if="showName" class="uploader-picture-content">
          <ivu-avatar :title="item.name"
                      :src="item.url"></ivu-avatar>
          <div class="uploader-picture-name" v-text="item.name"></div>
        </div>
        <img v-else :src="item.url" :alt="item.name">
        <div class="demo-upload-list-cover">
          <i class="el-icon-edit-outline" v-if="showEdit" title="修改图标名"
             @click="handleEdit(item)"></i>
          <i class="el-icon-delete" title="删除" @click="handleRemove(item)"></i>
        </div>
      </template>
      <template v-else>
        <el-progress :show-text="false" :percentage="item.percentage"></el-progress>
      </template>
    </div>
    <el-upload v-show="showAdd"
               ref="uploader"
               :show-file-list="false"
               accept="image/x-icon,image/png,image/jpeg,image/jpg,image/gif,image/bmp"
               :on-success="handleSuccess"
               :on-error="handleError"
               @paste.native="handlePaste"
               :multiple="maxCount>1"
               paste
               drag
               :action="action"
               class="el-uploader">
      <div class="demo-upload-btn" title="点击上传，支持拖拽及复制/截图粘贴上传" v-if="showAdd">
        <i class="el-icon-picture-outline"></i>
      </div>
    </el-upload>
  </div>
</template>

<script>

export default {
  name: "com-icon-index",
  props: {
    value: {
      type: [String, Array]
    },
    maxCount: {
      type: Number,
      default: 1
    },
    action: {
      type: String,
      default: process.env.VUE_APP_BASE_API + '/api/v1/upload/images'
    },
    showName: {
      type: Boolean,
      default: false
    },
    showEdit: {
      type: Boolean,
      default: false
    },
  },
  data() {
    return {
      uploadList: [],
    }
  },
  computed: {
    showAdd() {
      return this.uploadList.length < this.maxCount;
    }
  },
  mounted() {
    if (this.value) {
      this.init(this.value);
    }
  },
  watch: {
    value(newVals) {
      this.init(newVals);
    },
  },
  methods: {
    init(newVals) {
      if (!newVals) {
        return;
      }
      let vals = newVals;
      if (!Array.isArray(newVals)) {
        vals = [newVals];
      }
      this.uploadList = vals.map(val => ({
        name: val.substring(val.lastIndexOf('/') + 1, val.lastIndexOf('.')),
        url: val,
        status: 'success',
      }));
    },
    handleRemove(file) {
      this.uploadList.splice(this.uploadList.indexOf(file), 1);
      this.$emit('remove', file.url);
    },
    handleSuccess(res, file) {
      if (this.maxCount > 1) {
        const urls = this.uploadList.map(item => item.url);
        urls.push(res);
        this.$emit('input', urls);
        this.$modal.msgSuccess(`文件【${file.name}】上传成功。`);
      } else {
        this.$emit('input', res);
      }
    },
    handleError(error, file) {
      this.$modal.msgError(`文件【${file.name}】上传错误，${error}。`);
    },
    handlePaste(event) {
      const files = event.clipboardData && event.clipboardData.files;
      if (!(files && files.length)) {
        this.$modal.msgError('未获取到需要粘贴的图片，确保您已进行图片复制操作，且浏览器支持粘贴上传，否则请点击文件选择按钮进行上传！');
        return;
      }
      for (const file of files) {
        this.$refs.uploader.handleStart(file);
      }
      this.$refs.uploader.submit();
    },
    handleEdit(file) {
      this.$prompt('', '', {
        inputPlaceholder: '请输入要修改的图标名称...',
        inputValue: file.name,
        closeOnClickModal: false,
      }).then(({value}) => {
        this.$emit('edit-name', file.url, value);
      });
    },
  }
}
</script>

<style scoped lang="scss">
.demo-upload-list {
  display: inline-block;
  width: 60px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  margin-right: 4px;
}

.demo-upload-list img {
  width: 100%;
  height: 100%;
}

.demo-upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, .6);
}

.demo-upload-list:hover .demo-upload-list-cover {
  display: block;
}

.demo-upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}

.uploader-picture-content {
  width: 100%;
  height: 100%;
  line-height: 40px;
}

.uploader-picture-content .uploader-picture-name {
  margin: 0;
  line-height: 20px;
  font-size: 10px;
}

.demo-upload-btn {
  width: 58px;
  height: 58px;
  line-height: 58px;
}

.el-progress {
  margin-top: 26px;
}

.el-uploader {
  display: inline-block;
  width: 60px;

  ::v-deep .el-upload-dragger {
    width: unset;
    height: unset;
  }
}
</style>
