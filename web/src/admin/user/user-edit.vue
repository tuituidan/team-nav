<template>
  <el-dialog :title="title" :visible.sync="show"
             :close-on-click-modal="false"
             width="600px" append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="用户姓名" prop="nickname">
        <el-input v-model="form.nickname" placeholder="请输入用户姓名" maxlength="30"/>
      </el-form-item>
      <el-form-item label="登录账号" prop="nickname">
        <el-input v-model="form.username" placeholder="请输入登录账号" maxlength="30"/>
      </el-form-item>
      <el-form-item label="所属角色">
        <role-select v-model="form.roleIds"></role-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio
            v-for="dict in statusOptions"
            :key="dict.id"
            :label="dict.id"
          >{{dict.name}}</el-radio>
        </el-radio-group>
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
  name: "user-edit",
  components: {
    'role-select': () => import('@/components/role-select/index.vue'),
  },
  data() {
    return {
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      show: false,
      statusOptions: [
        {
          id: '1',
          name: '有效'
        },
        {
          id: '2',
          name: '无效'
        },
      ],
      form: {
        id: '',
        nickname: '',
        username: '',
        roleIds: [],
        status: '1'
      },
      rules: {
        nickname: [
          {required: true, message: "用户姓名不能为空", trigger: "blur"}
        ],
        username: [
          {required: true, message: "登录账号不能为空", trigger: "blur"}
        ],
        status: [
          {required: true, message: "状态不能为空", trigger: "blur"}
        ],
      }
    }
  },
  methods: {
    open(item){
      if (item) {
        this.title = '编辑用户';
        this.form = {...item};
      } else {
        this.title = '新增用户';
      }
      this.show = true;
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$http.save('/api/v1/user', {...this.form})
            .then(() => {
              this.$modal.notifySuccess('保存成功');
              this.show = false;
              this.$emit('refresh');
            })
        }
      });
    },
    // 取消按钮
    cancel() {
      this.show = false;
    },
  }
}
</script>

<style scoped>

</style>
