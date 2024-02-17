<template>
  <div class="has-logo" :style="{ backgroundColor: variables.menuBackground }">
    <logo :collapse="isCollapse"/>
    <el-scrollbar class="theme-dark" wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBackground"
        :text-color="variables.menuColor"
        :unique-opened="true"
        active-text-color="#409EFF"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path  + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";

export default {
  components: {SidebarItem, Logo},
  data() {
    return {
      sidebarRouters: [
        {
          path: '/admin/category',
          name: 'category',
          meta: {title: '分类管理', icon: 'el-icon-menu'}
        },
        {
          path: '/admin/card',
          name: 'card',
          meta: {title: '卡片管理', icon: 'el-icon-s-grid'}
        },
        {
          path: '/admin/role',
          name: 'role',
          meta: {title: '角色管理', icon: 'el-icon-user-solid'}
        },
        {
          path: '/admin/user',
          name: 'user',
          meta: {title: '用户管理', icon: 'el-icon-s-custom'}
        },
        {
          path: '/admin/notice',
          name: 'notice',
          meta: {title: '通知公告', icon: 'el-icon-message-solid'}
        },
        {
          path: '/admin/setting',
          name: 'setting',
          meta: {title: '系统设置', icon: 'el-icon-s-tools'}
        },
      ]
    }
  },
  computed: {
    ...mapGetters(["sidebar"]),
    activeMenu() {
      const route = this.$route;
      const {meta, path} = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    }
  }
};
</script>
