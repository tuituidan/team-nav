<template>
  <el-card>
    <div slot="header" class="card-title">
      <span>基础设置</span>
      <el-button class="btn-save" type="text" @click="saveSetting">保存</el-button>
    </div>
    <el-form ref="settingForm"
             :model="settingItem"
             label-width="120px"
             :rules="settingItemRules" @submit.native.prevent>
      <el-form-item label="网站标题" prop="navName">
        <el-input v-model.trim="settingItem.navName"
                  clearable
                  maxlength="50"
                  placeholder="网站标题"></el-input>
      </el-form-item>
      <el-form-item label="网站LOGO" prop="logoPath">
        <div class="multi-form-item">
          <com-icon-uploader v-model="settingItem.logoPath" @remove="onRemoveLogo"></com-icon-uploader>
          <div>
            <el-checkbox v-model="settingItem.logoToFavicon">同步修改favicon</el-checkbox>
            <com-tip tip="勾选时网站的favicon也将使用此logo图片"></com-tip>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="公告切换间隔" prop="countdown">
        <div class="multi-form-item">
          <el-input-number :max="200"
                           :min="10"
                           v-model="settingItem.cutOverSpeed"
                           class="multi-form-item-input"></el-input-number>
          <div style="margin-left: 5px">秒</div>
          <com-tip tip="配置首页多个通知公告的自动切换间隔时间"></com-tip>
        </div>
      </el-form-item>
      <el-form-item label="Nginx访问网站" prop="nginxOpen">
        <div class="multi-form-item">
          <div>
            <el-switch v-model="settingItem.nginxOpen">
            </el-switch>
          </div>
          <el-input type="text"
                    maxlength="100"
                    clearable
                    class="multi-form-item-input"
                    v-model.trim="settingItem.nginxUrl"
                    placeholder="Nginx地址"></el-input>
          <com-tip tip="可以配置通过Nginx来访问上传的静态网站，Nginx配置可参考README.md，开启后访问网站时将跳转Nginx地址"></com-tip>
        </div>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "base-setting",
  components: {
    'com-icon-uploader': () => import('@/components/com-icon-uploader/index.vue'),
    'com-tip': () => import('@/components/com-tip/index.vue'),
  },
  data() {
    return {
      settingItem: {
        navName: '',
        logoPath: '',
        logoToFavicon: false,
        cutOverSpeed: 10,
        nginxOpen: false,
        nginxUrl: '',
      },
      layoutSizeList: [
        {
          id: 'small-layout',
          name: '较小'
        },
        {
          id: 'big-layout',
          name: '较大'
        }
      ],
      settingItemRules: {
        navName: [
          {required: true, message: '网站标题不能为空'},
        ]
      }
    }
  },
  mounted() {
    this.loadSetting();
  },
  methods: {
    loadSetting() {
      this.$http.get(`/api/v1/setting`)
        .then(res => {
          this.settingItem = res;
          this.settingItem.cutOverSpeed = res.cutOverSpeed || 10;
          this.settingItem.logoToFavicon = Boolean(res.logoToFavicon);
          this.settingItem.nginxOpen = Boolean(res.nginxOpen);
        });
    },
    saveSetting() {
      this.$refs.settingForm.validate(valid => {
        if (valid) {
          this.$http.patch(`/api/v1/setting`, {...this.settingItem})
            .then(() => {
              this.$modal.msgSuccess('保存成功');
              setTimeout(() => {
                location.reload();
              }, 1000);
            });
        }
      });
    },
    onRemoveLogo(url) {
      // 只有一个，不用判断url，直接移除
      this.settingItem.logoPath = '';
    },
  }
}
</script>

<style scoped>
.multi-form-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.multi-form-item .multi-form-item-input {
  margin-left: 15px;
  flex: auto;
}
</style>
