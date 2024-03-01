<template>
  <el-dialog :title="title" :visible.sync="show"
             width="630px"
             :close-on-click-modal="false"
             append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="110px" @submit.native.prevent>
      <el-form-item label="卡片类型" prop="type">
         <span slot="label">
          <el-tooltip placement="top">
            <div slot="content">
              普通卡片：内容型卡片，通过添加链接跳转对应网址<br/>
              静态网站：上传静态网站的zip压缩包，自动生成可访问链接<br/>
              动态卡片：添加http接口或sql查询获取动态的卡片内容（暂不可用，开发中...）
            </div>
            <i class="el-icon-question"></i>
          </el-tooltip>
          卡片类型
        </span>
        <el-radio-group v-model="form.type">
          <el-radio
            v-for="item in types"
            :key="item.id"
            :label="item.id"
            :disabled="Boolean(form.id) || item.disabled"
          >{{ item.name }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="所属分类" prop="category">
        <category-select ref="refCategory"
                         style="width: 100%"
                         v-model="form.category"
                         placeholder="请选择所属分类"></category-select>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model.trim="form.title" placeholder="标题"/>
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input v-model.trim="form.content" type="textarea"
                  maxlength="200"
                  show-word-limit
                  clearable
                  :rows="3"
                  placeholder="请输入内容"/>
      </el-form-item>
      <el-form-item label="私密内容" prop="privateContent">
        <span slot="label">
          <el-tooltip content='登录后才能看到的信息' placement="top">
          <i class="el-icon-question"></i>
          </el-tooltip>
          私密信息
        </span>
        <el-input v-model.trim="form.privateContent" type="textarea"
                  maxlength="200"
                  show-word-limit
                  clearable
                  :rows="2"
                  placeholder="请输入内容"/>
      </el-form-item>
      <el-form-item label="链接"
                    v-if="form.type==='default'"
                    :rules="[{required: form.showQrcode, message:'链接不能为空', trigger:'blue' }]"
                    prop="url">
        <el-row>
          <el-col :span="18">
            <el-input v-model.trim="form.url" placeholder="链接" @blur="getFavicons"/>
          </el-col>
          <el-col :span="5" :offset="1">
            <el-checkbox v-model="form.showQrcode">显示二维码</el-checkbox>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="网站文件" prop="zip" v-else>
        <file-uploader type="modules"
                       accept="application/zip"
                       :file-list="form.zip?[form.zip]:[]"
                       @file-change="zipFileChange"
                       :limit="1">zip压缩包上传
        </file-uploader>
      </el-form-item>
      <el-form-item class="form-icon-loading" v-if="showFaviconLoading">
        <el-button loading type="text">正在尝试获取该地址的图标</el-button>
      </el-form-item>
      <el-form-item label="图标" prop="icon">
        <card-icon-select ref="refCardIcon" v-model="form.icon" @input="iconChange"/>
      </el-form-item>
      <el-form-item label="附件" prop="attachment">
        <file-uploader type="attachments"
                       :file-list="form.attachments?form.attachments:[]"
                       multiple
                       @file-change="attachmentChange"
                       :limit="10">附件上传
        </file-uploader>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button v-if="isApply" type="primary" @click="submitForm">提交审核</el-button>
      <el-button v-else type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "card-edit-index",
  components: {
    'category-select': () => import('@/components/category-select/index.vue'),
    'card-icon-select': () => import('@/components/card-icon-select/index.vue'),
    'file-uploader': () => import('@/components/file-uploader/index.vue'),
  },
  data() {
    return {
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      show: false,
      showFaviconLoading: false,
      isApply: false,
      // 菜单树选项
      categoryOptions: [],
      // 表单参数
      form: {
        type: 'default',
        category: '',
        title: '',
        content: '',
        privateContent: '',
        url: '',
        showQrcode: false,
        icon: null,
        zip: null,
        attachmentIds: [],
        attachments: [],
      },
      types: [
        {
          id: 'default',
          name: '普通卡片',
          disabled: false,
        },
        {
          id: 'zip',
          name: '静态网站',
          disabled: false,
        },
        {
          id: 'dynamic',
          name: '动态卡片',
          disabled: true,
        },
      ],
      // 表单校验
      rules: {
        category: [
          {required: true, message: "所属分类不能为空", trigger: "blur"}
        ],
        title: [
          {required: true, message: "标题不能为空", trigger: "blur"}
        ],
        content: [
          {required: true, message: "内容不能为空", trigger: "blur"}
        ],
        icon: [
          {required: true, message: "请添加一个图标", trigger: "change"},
        ],
        zip: [
          {required: true, message: '请上传网站zip文件'}
        ]
      }
    }
  },
  methods: {
    open(item = {}) {
      this.resetForm();
      if (item.id) {
        this.title = '编辑卡片';
        this.form = {...item};
        this.form.attachmentIds = Array.isArray(this.form.attachments)
          ? this.form.attachments.map(item => item.id) : [];
      } else if (item.category) {
        this.form.type = 'default';
        this.form.category = item.category;
        this.title = '新增卡片';
      } else {
        this.form.type = 'default';
        this.title = '申请卡片';
        this.isApply = true;
      }
      this.show = true;
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
        this.$refs.refCategory.init();
        this.$refs.refCardIcon.init(this.form.icon);
      })
    },
    resetForm() {
      this.form = {
        type: 'default',
        category: '',
        title: '',
        content: '',
        privateContent: '',
        url: '',
        showQrcode: false,
        icon: null,
        zip: null,
        attachmentIds: [],
        attachments: [],
      };
    },
    zipFileChange(fileList) {
      if (fileList.length <= 0) {
        this.form.zip = null;
      } else {
        this.form.zip = {...fileList[0], isNew: true};
        this.$refs.form.validateField('zip');
      }
    },
    attachmentChange(fileList) {
      console.log(fileList);
      this.form.attachmentIds = Array.isArray(fileList)
        ? fileList.map(item => item.path) : [];
      console.log(this.form.attachmentIds);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid && this.form.icon) {
          const newZip = this.form.type === 'zip' && this.form.zip && this.form.zip.isNew;
          if (newZip) {
            this.$modal.loading('压缩包解压时间较长，请耐心等待...');
          }
          this.$http.save('/api/v1/card', {...this.form})
            .then(() => {
              this.$modal.msgSuccess('保存成功');
              this.show = false;
              this.$emit('refresh', this.form.category);
            })
            .finally(() => {
              if (newZip) {
                this.$modal.closeLoading();
              }
            })
        }
      });
    },
    // 取消按钮
    cancel() {
      this.show = false;
    },
    iconChange() {
      // 单个触发校验
      this.$refs.form.validateField('icon');
    },
    getFavicons() {
      if (!(this.form.url && this.form.url.startsWith('http'))) {
        return;
      }
      this.showFaviconLoading = true;
      this.$http.get('/api/v1/card/icon', {params: {url: this.form.url}})
        .then(res => {
          if (!(Array.isArray(res) && res.length > 0)) {
            return;
          }
          for (const url of res) {
            this.$refs.refCardIcon.uploadSuccess(url);
          }
        })
        .finally(() => {
          this.showFaviconLoading = false;
        });
    },
  },
}
</script>

<style scoped lang="scss">
.form-icon-loading {
  margin-top: -16px;
  margin-bottom: 0;
}
</style>
