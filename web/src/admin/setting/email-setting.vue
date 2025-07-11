<template>
  <el-card>
    <div slot="header" class="card-title">
      <span>邮箱配置</span>
      <el-button class="btn-save" type="text" @click="saveSetting">保存</el-button>
      <el-button class="btn-save btn-test" type="text" @click="testSetting">测试</el-button>
      <el-button class="btn-save btn-test" type="text" @click="resetForm">清空</el-button>
    </div>
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" @submit.native.prevent>
      <el-form-item label="邮箱服务地址" prop="host">
        <el-input v-model="form.host" placeholder="请输入邮箱服务地址" maxlength="100" v-trim/>
      </el-form-item>
      <el-form-item label="邮箱服务协议" prop="host">
        <el-input v-model="form.protocol" placeholder="请输入邮箱服务协议" maxlength="100" v-trim/>
      </el-form-item>
      <el-form-item label="邮箱服务端口" prop="port">
        <el-input-number v-model="form.port"
                         :min="1"
                         step-strictly
                         :max="65535"
                         placeholder="请输入邮箱服务端口" maxlength="100" v-trim/>
      </el-form-item>
      <el-form-item label="邮箱账号" prop="username">
        <el-input v-model="form.username" placeholder="请输入邮箱账号" maxlength="100" v-trim/>
      </el-form-item>
      <el-form-item label="邮箱密码" prop="password">
        <el-input v-model="form.password" show-password placeholder="请输入邮箱密码" maxlength="100" v-trim/>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "email-setting",
  components: {
    'com-tip': () => import('@/components/com-tip/index.vue'),
  },
  data() {
    return {
      form: {
        host: '',
        protocol: '',
        port: '',
        username: '',
        password: '',
      },
      rules: {
        host: [
          {required: true, message: "邮箱服务地址不能为空", trigger: "blur"}
        ],
        port: [
          {required: true, message: "邮箱服务端口不能为空", trigger: "blur"}
        ],
        username: [
          {required: true, message: "邮箱账号不能为空", trigger: "blur"}
        ],
        password: [
          {required: true, message: "邮箱密码不能为空", trigger: "blur"}
        ],
      },
    }
  },
  mounted() {
    this.loadSetting();
  },
  methods: {
    resetForm() {
      this.form = {
        host: '',
        port: 0,
        username: '',
        password: '',
        protocol:''
      };
      this.$nextTick(() => {
        this.$refs.form.clearValidate();
      })

      this.$http.patch(`/api/v1/setting/email`, {...this.form})
        .then(() => {
        });
    },
    loadSetting() {
      this.$http.get(`/api/v1/setting/email`)
        .then(res => {
          this.form = res;
        });
    },
    saveSetting() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$http.patch(`/api/v1/setting/email`, {...this.form})
            .then(() => {
              this.$modal.msgSuccess('保存成功');
            });
        }
      });
    },
    testSetting() {
      if (!this.form.id) {
        this.$modal.msgError('请先保存配置');
        return;
      }
      this.$modal.loading('正在尝试发送邮件，请耐心等待...');
      this.$http.post(`/api/v1/setting/email/test`)
        .then(() => {
          this.$modal.msgSuccess('发送成功');
        })
        .finally(() => {
          this.$modal.closeLoading();
        });
    },
  }
}
</script>

<style scoped>
.btn-test {
  margin-right: 10px;
}

.el-input-number {
  width: 100%;
}

</style>
