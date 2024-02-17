<template>
  <el-dialog :title="title" :visible.sync="show" width="800px"
             :close-on-click-modal="false"
             append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="公告内容" prop="postName">
        <quill-editor v-model="form.content" placeholder="请输入岗位名称"/>
      </el-form-item>
      <el-form-item label="结束时间" prop="postCode">
        <el-input v-model="form.endTime" placeholder="请输入编码名称"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "index",
  components: {
    'quill-editor': () => import('@/components/Editor/index.vue')
  },
  data() {
    return {
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      show: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        postName: [
          {required: true, message: "岗位名称不能为空", trigger: "blur"}
        ],
        postCode: [
          {required: true, message: "岗位编码不能为空", trigger: "blur"}
        ],
        postSort: [
          {required: true, message: "岗位顺序不能为空", trigger: "blur"}
        ]
      }
    }
  },
  methods: {
    open(item){
      if(item){
        this.title = '编辑'
      }else {
        this.title = '新增'
      }
      this.show = true;
    },
    // 取消按钮
    cancel() {
      this.show = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        status: "0",
        remark: undefined
      };
      this.$refs.form.resetFields();
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.postId != undefined) {
            updatePost(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.show = false;
              this.getList();
            });
          } else {
            addPost(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.show = false;
              this.getList();
            });
          }
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
