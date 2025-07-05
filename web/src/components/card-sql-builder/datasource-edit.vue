<template>
  <el-dialog title="新增数据源" :visible.sync="show" width="600px"
             :close-on-click-modal="false"
             append-to-body>
    <el-form ref="form" :model="form" :rules="rules" submit.native.prevent label-width="100px">
      <el-form-item label="数据库类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio
            v-for="item in typeOptions"
            :key="item"
            :label="item"
          >{{ item }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="数据源名称" prop="name">
        <el-input v-model="form.name" placeholder="数据源名称" clearable v-trim/>
      </el-form-item>
      <el-form-item label="jdbcUrl" prop="jdbcUrl">
        <el-input v-model="form.jdbcUrl" placeholder="jdbcUrl" clearable v-trim/>
      </el-form-item>
      <el-form-item label="数据库账户" prop="username">
        <el-input v-model="form.username" placeholder="数据库账户" clearable v-trim/>
      </el-form-item>
      <el-form-item label="数据库密码" prop="password">
        <el-input v-model="form.password" placeholder="数据库密码" clearable v-trim show-password/>
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
  name: "datasource-edit",
  data() {
    return {
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      show: false,
      typeOptions: ['mysql', 'postgresql', 'sqlserver'],
      // 表单参数
      form: {
        type: 'mysql',
        name: '',
        jdbcUrl: '',
        username: '',
        password: '',
      },
      // 表单校验
      rules: {
        name: [
          {required: true, message: "数据源名称不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "数据库类型不能为空", trigger: "blur"}
        ],
        jdbcUrl: [
          {required: true, message: "jdbcUrl不能为空", trigger: "blur"}
        ],
        username: [
          {required: true, message: "数据库账户不能为空", trigger: "blur"}
        ],
        password: [
          {required: true, message: "数据库密码不能为空", trigger: "blur"}
        ],
      }
    }
  },
  methods: {
    open(){
      this.reset();
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
        type: 'mysql',
        jdbcUrl: '',
        username: '',
        password: '',
      };
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$http.save('/api/v1/datasource', {...this.form})
            .then(() => {
              this.$modal.msgSuccess('保存成功');
              this.show = false;
              this.$emit('refresh');
            })
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
