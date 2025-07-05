<template>
  <div>
    <div>
      <el-input v-model="value.url" placeholder="请输入数据获取地址" @input="changeInput" size="small">
        <el-select v-model="value.httpMethod" slot="prepend" style="width: 90px">
          <el-option label="GET" value="GET"></el-option>
          <el-option label="POST" value="POST"></el-option>
        </el-select>
      </el-input>
    </div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="Query参数" name="query">
        <key-value-builder v-model="value.queryParams"></key-value-builder>
      </el-tab-pane>

      <el-tab-pane label="Body参数" name="body" v-if="value.httpMethod === 'POST'">
        <el-radio-group v-model="value.bodyType" style="margin-bottom: 15px">
          <el-radio label="none">none</el-radio>
          <el-radio label="form-data">form-data</el-radio>
          <el-radio label="json">json</el-radio>
        </el-radio-group>

        <div v-if="value.bodyType === 'form-data'">
          <key-value-builder v-model="value.bodyFormData"></key-value-builder>
        </div>

        <div v-if="value.bodyType === 'json'">
          <el-input type="textarea" :rows="8" v-model="value.bodyJsonData"></el-input>
        </div>
      </el-tab-pane>

      <el-tab-pane label="认证" name="auth">
        <el-select v-model="value.authType" style="width: 200px; margin-bottom: 15px">
          <el-option label="无认证" value="none"></el-option>
          <el-option label="键值对" value="key-value"></el-option>
          <el-option label="Basic Auth" value="basic"></el-option>
          <el-option label="Bearer Token" value="bearer"></el-option>
          <el-option label="JWT Bearer" value="jwt"></el-option>
        </el-select>

        <div v-if="value.authType === 'key-value'">
          <key-value-builder v-model="value.authKeyValues"></key-value-builder>
        </div>

        <div v-if="value.authType === 'basic'">
          <el-input v-model="value.basicUsername" placeholder="请输入用户名" style="margin-bottom: 10px"></el-input>
          <el-input v-model="value.basicPassword" placeholder="请输入密码"></el-input>
        </div>

        <div v-if="value.authType === 'bearer'">
          <el-input v-model="value.bearerToken" placeholder="请输入token"></el-input>
        </div>

        <div v-if="value.authType === 'jwt'">
          <el-select v-model="value.jwtAlgorithm" placeholder="请选择加密算法" clearable
                     style="width: 100%; margin-bottom: 10px">
            <el-option v-for="algorithm in signatureAlgorithms"
                       :key="algorithm"
                       :label="algorithm"
                       :value="algorithm"></el-option>
          </el-select>
          <el-input v-model="value.jwtSecret" placeholder="请输入秘钥Secret" clearable
                    style="margin-bottom: 10px"></el-input>
          <el-divider content-position="left">payload参数</el-divider>
          <key-value-builder v-model="value.jwtPayload"></key-value-builder>
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-row>
      <el-col :span="6">
        <el-tooltip content='如果接口返回的是json字符串，可通过填写jsonpath语法取其中的某个数据作为最终结果，如果接口返回的只是普通字符串，这里无需填写'>
          <i class="el-icon-question"></i>
        </el-tooltip>
        <span style="margin-left: 5px">结果解析表达式</span>
      </el-col>
      <el-col :span="18">
        <el-input v-model="value.resultExp" placeholder="请输入结果解析表达式" size="small"></el-input>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "card-http-builder",
  components: {
    'key-value-builder': () => import('@/components/card-http-builder/key-value-builder.vue'),
  },
  props: {
    value: {
      type: Object,
      default: () => {
        return {
          httpMethod: 'GET',
          url: '',
          queryParams: [{key: '', value: ''}],
          bodyType: 'none',
          bodyFormData: [{key: '', value: ''}],
          bodyJsonData: '{\n  \n}',
          authType: 'none',
          authKeyValues: [{key: '', value: ''}],
          basicUsername: '',
          basicPassword: '',
          bearerToken: '',
          jwtAlgorithm: 'HS256',
          jwtSecret: '',
          jwtPayload: [{key: '', value: ''}],
          resultExp: '',
        }
      },
    },
  },
  data() {
    return {
      activeTab: 'query',
      signatureAlgorithms: [
        'HS256', 'HS384', 'HS512',
        'RS256', 'RS384', 'RS512',
        'PS256', 'PS384', 'PS512',
        'ES256', 'ES384', 'ES512'
      ],
    }
  },
  methods: {
    changeInput(){
      this.$emit('input', this.value)
    },
  }
}
</script>

<style scoped>
.el-tabs {
  margin-top: 10px;
  margin-bottom: 15px;
}

.el-table {
  margin-bottom: 10px;
}
</style>
