<template>
  <el-dialog title="修改密码" :visible.sync="show" width="500px"
             :close-on-click-modal="false"
             append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px" @submit.native.prevent>
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="form.oldPassword" placeholder="请输入旧密码" type="password" show-password v-trim/>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" placeholder="请输入新密码" type="password" show-password v-trim/>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" placeholder="请确认新密码" type="password" show-password v-trim/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">保 存</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "change-password",
  data() {
    return {
      // 是否显示弹出层
      show: false,
      // 表单参数
      form: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      // 表单校验
      // 表单校验
      rules: {
        oldPassword: [
          {required: true, message: "旧密码不能为空", trigger: "blur"}
        ],
        newPassword: [
          {required: true, message: "新密码不能为空", trigger: "blur"},
          {min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur"}
        ],
        confirmPassword: [
          {required: true, message: "确认密码不能为空", trigger: "blur"},
          {
            required: true, validator: (rule, value, callback) => {
              if (this.form.newPassword !== value) {
                callback(new Error("两次输入的密码不一致"));
              } else {
                callback();
              }
            }, trigger: "blur"
          }
        ]
      }
    }
  },
  methods: {
    open() {
      this.show = true;
      this.resetForm();
    },
    // 取消按钮
    cancel() {
      this.show = false;
      this.resetForm();
    },
    resetForm(){
      this.form = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      };
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$http.patch(`/api/v1/user/password`, this.form)
            .then(() => {
              this.$modal.msgSuccess('密码修改成功');
              this.show = false;
            })
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
