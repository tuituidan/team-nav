<template>
  <div :class="classObj" class="app-wrapper" :style="{'--current-color': '#409EFF'}">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
    <sidebar v-if="!sidebar.hide" class="sidebar-container"/>
    <div :class="{sidebarHide:sidebar.hide}" class="main-container">
      <div class="fixed-header">
        <navbar/>
      </div>
      <app-main />
      <right-panel>
        <settings/>
      </right-panel>
    </div>
  </div>
</template>

<script src="./index.js"></script>

<style lang="scss" scoped>
@import "~@/assets/styles/mixin.scss";
@import "~@/assets/styles/variables.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: relative;
  z-index: 9;
  width: 100%;
  transition: width 0.28s;
}

//.hideSidebar .fixed-header {
//  width: calc(100% - 54px);
//}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}
</style>
<style lang="scss">
body {
  overflow: hidden;
}
</style>
