<template>
  <div class="navbar" style="display: flex">
    <hamburger id="hamburger-container"
               :is-active="sidebar.opened"
               class="hamburger-container" @toggleClick="toggleSideBar"/>
    <el-input
      ref="refSearchInput"
      class="header-search"
      size="small"
      clearable
      placeholder="请输入关键字回车搜索..."
      v-model="keywords"
      v-trim
      @change="searchHandler"
      @clear="searchHandler"
      suffix-icon="el-icon-search">
    </el-input>
    <carousel-flip-notice class="carousel-flip-notice"></carousel-flip-notice>
    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <screenfull class="right-menu-item hover-effect"/>
        <doc v-if="showDoc" class="right-menu-item hover-effect"/>
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <header-avatar></header-avatar>
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-if="!loginUser.id"
                            @click.native="$store.dispatch('settings/loadVersion')"
          >登录</el-dropdown-item>
          <router-link v-if="loginUser.id && loginUser.isAdmin" to="/admin/category">
            <el-dropdown-item>后台管理</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">布局设置</el-dropdown-item>
          <el-dropdown-item v-if="loginUser.id" @click.native="openChangePassword">修改密码</el-dropdown-item>
          <el-dropdown-item v-if="loginUser.id && !loginUser.isAdmin">
            <card-apply>卡片申请</card-apply>
          </el-dropdown-item>
          <el-dropdown-item v-if="loginUser.id" divided @click.native="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <change-password ref="changePassword"></change-password>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  components: {
    'hamburger': () => import('@/components/Hamburger'),
    'screenfull': () => import('@/home/components/Screenfull'),
    'change-password': () => import('@/home/components/change-password'),
    'header-avatar': () => import('@/components/header-avatar'),
    'doc': () => import('@/home/components/doc'),
    'card-apply': () => import('@/home/components/card-apply'),
    'carousel-flip-notice': () => import('@/home/components/carousel-flip-notice'),
  },
  data() {
    return {
      keywords: ''
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'device',
      'loginUser',
      'showDoc'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
  },
  mounted() {
    document.addEventListener('keypress', event => {
      if (!document.body.className.includes('el-popup-parent--hidden')) {
        this.$refs.refSearchInput.focus()
      }
    })
  },
  methods: {
    searchHandler() {
      this.$store.dispatch('home/loadHomeCards', this.keywords);
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('user/LogOut').then(() => {




          this.$store.dispatch('settings/loadVersion');

        })
      }).catch(() => {
      });
    },
    openChangePassword() {
      this.$refs.changePassword.open();
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  display: flex;
  height: 60px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 56px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .header-search {
    float: left;
    line-height: 60px;
    width: 250px;
  }

  .carousel-flip-notice {
    flex: auto;
    margin: 0 20px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 60px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
