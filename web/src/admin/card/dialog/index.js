/*
 * @author tuituidan
 * @date 2023/11/24
 */
export default {
  name: "card-edit-index",
  components: {
    'category-select': () => import('@/components/category-select/index.vue'),
    'card-icon-select': () => import('@/components/card-icon-select/index.vue')
  },
  data() {
    return {
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      show: false,
      showFaviconLoading: false,
      // 菜单树选项
      categoryOptions: [],
      // 表单参数
      form: {
        type: 'default',
        category: '',
        title:'',
        content:'',
        url: '',
        icon: null
      },
      types: [
        {
          id: 'default',
          name: '普通'
        },
        {
          id: 'file',
          name: '文件'
        },
        {
          id: 'site',
          name: '静态网站'
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
        ]
      }
    }
  },
  methods: {
    open(item) {
      if (item) {
        this.title = '编辑卡片';
        this.form = {...item};
      } else {
        this.title = '新增卡片';
      }
      this.show = true;
      this.$nextTick(() => {
        this.$refs.refCategory.init();
        this.$refs.refCardIcon.init(this.form.icon);
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid && this.form.icon) {
          /*if (this.editItem.type === 'zip' && this.newZip) {
            this.$tools.showLoading('原型文件解压时间较长，请耐心等待...');
          }*/
          this.$http.save('/api/v1/card', {...this.form})
            .then(() => {
              this.$modal.msgSuccess('保存成功');
              this.show = false;
              this.$emit('refresh', this.form.category);
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
      this.$refs["form"].validateField('icon');
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
