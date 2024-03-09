<template>
  <el-drawer size="320px" :visible="visible" :with-header="false" :append-to-body="true" :show-close="false">
    <div class="drawer-container">
      <div>
        <div class="setting-drawer-content">
          <div class="setting-drawer-title">
            <h3 class="drawer-title">主题风格设置</h3>
          </div>
          <div class="setting-drawer-block-checbox">
            <div class="setting-drawer-block-checbox-item" @click="handleTheme('theme-dark')">
              <img src="@/assets/images/dark.svg" alt="dark">
              <div v-if="sideTheme === 'theme-dark'" class="setting-drawer-block-checbox-selectIcon"
                   style="display: block;">
                <i aria-label="图标: check" class="anticon anticon-check">
                  <svg viewBox="64 64 896 896" data-icon="check" width="1em" height="1em" :fill="theme"
                       aria-hidden="true" focusable="false" class="">
                    <path
                      d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z"/>
                  </svg>
                </i>
              </div>
            </div>
            <div class="setting-drawer-block-checbox-item" @click="handleTheme('theme-light')">
              <img src="@/assets/images/light.svg" alt="light">
              <div v-if="sideTheme === 'theme-light'" class="setting-drawer-block-checbox-selectIcon"
                   style="display: block;">
                <i aria-label="图标: check" class="anticon anticon-check">
                  <svg viewBox="64 64 896 896" data-icon="check" width="1em" height="1em" :fill="theme"
                       aria-hidden="true" focusable="false" class="">
                    <path
                      d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z"/>
                  </svg>
                </i>
              </div>
            </div>
          </div>
        </div>
        <el-divider/>
        <h3 class="drawer-title">系统布局配置</h3>

        <div class="drawer-item">
          <span>菜单设置</span>
          <el-checkbox-group v-model="menuSetting">
            <el-checkbox label="open">默认展开</el-checkbox>
            <el-checkbox label="accordion">手风琴</el-checkbox>
          </el-checkbox-group>
        </div>
        <div class="drawer-item">
          <span>卡片图标</span>
          <el-radio-group v-model="cardIconShape">
            <el-radio label="circle">圆形</el-radio>
            <el-radio label="square">方形</el-radio>
          </el-radio-group>

        </div>
        <el-button size="small" type="primary" plain icon="el-icon-document-add" @click="saveSetting">保存配置</el-button>
        <el-button size="small" plain icon="el-icon-refresh" @click="resetSetting">重置配置</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script>

export default {
  data() {
    return {
      theme: '#409EFF',
      sideTheme: this.$store.state.settings.sideTheme
    };
  },
  computed: {
    visible: {
      get() {
        return this.$store.state.settings.showSettings
      }
    },
    menuSetting: {
      get() {
        const val = [];
        if (this.$store.state.settings.menuDefaultOpen) {
          val.push('open');
        }
        if (this.$store.state.settings.menuAccordion) {
          val.push('accordion');
        }
        console.log(val);
        return val;
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'menuDefaultOpen',
          value: val.includes('open'),
        });
        this.$store.dispatch('settings/changeSetting', {
          key: 'menuAccordion',
          value: val.includes('accordion'),
        });
      },
    },
    cardIconShape: {
      get() {
        return this.$store.state.settings.cardIconShape
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'cardIconShape',
          value: val,
        });
      },
    },
  },
  methods: {
    handleTheme(val) {
      this.$store.dispatch('settings/changeSetting', {
        key: 'sideTheme',
        value: val
      })
      this.sideTheme = val;
    },
    saveSetting() {
      this.$modal.loading("正在保存到本地，请稍候...");
      this.$cache.local.set(
        "layout-setting",
        `{
            "sideTheme":"${this.sideTheme}",
            "menuDefaultOpen":"${this.$store.state.settings.menuDefaultOpen}",
            "menuAccordion":"${this.$store.state.settings.menuAccordion}",
            "cardIconShape":"${this.$store.state.settings.cardIconShape}"
          }`
      );
      setTimeout(() => this.$modal.closeLoading(), 1000)
    },
    resetSetting() {
      this.$modal.loading("正在清除设置缓存并刷新，请稍候...");
      this.$cache.local.remove("layout-setting")
      setTimeout("window.location.reload()", 1000)
    }
  }
}
</script>

<style lang="scss" scoped>
.setting-drawer-content {
  .setting-drawer-title {
    margin-bottom: 12px;
    color: rgba(0, 0, 0, .85);
    font-size: 14px;
    line-height: 22px;
    font-weight: bold;
  }

  .setting-drawer-block-checbox {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin-top: 10px;
    margin-bottom: 20px;

    .setting-drawer-block-checbox-item {
      position: relative;
      margin-right: 16px;
      border-radius: 2px;
      cursor: pointer;

      img {
        width: 48px;
        height: 48px;
      }

      .setting-drawer-block-checbox-selectIcon {
        position: absolute;
        top: 0;
        right: 0;
        width: 100%;
        height: 100%;
        padding-top: 15px;
        padding-left: 24px;
        color: #1890ff;
        font-weight: 700;
        font-size: 14px;
      }
    }
  }
}

.drawer-container {
  padding: 20px;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;

  .drawer-title {
    margin-bottom: 12px;
    color: rgba(0, 0, 0, .85);
    font-size: 14px;
    line-height: 22px;
  }

  .drawer-item {
    color: rgba(0, 0, 0, .65);
    font-size: 14px;
    padding: 12px 0;
    display: flex;
    justify-content: space-between;
  }
}
</style>
