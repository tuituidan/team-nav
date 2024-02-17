<template>
  <span class="ivu-badge" ref="badge">
        <slot></slot>
        <sup v-if="$slots.count" class="ivu-badge-count ivu-badge-count-custom"><slot name="count"></slot></sup>
    </span>
</template>
<script>
/**
 * 引入ivu-badge，因为el-badge无法slot一个图标
 */
export default {
  name: 'Badge',
};
</script>
<style scoped>

.ivu-badge {
  position: relative;
  display: inline-block
}

.ivu-badge-count {
  font-family: "Monospaced Number";
  line-height: 1;
  vertical-align: middle;
  position: absolute;
  -webkit-transform: translateX(50%);
  -ms-transform: translateX(50%);
  transform: translateX(50%);
  top: -8px;
  right: 1px;
  height: 20px;
  border-radius: 10px;
  min-width: 20px;
  background: #ed4014;
  border: 1px solid transparent;
  color: #fff;
  line-height: 18px;
  text-align: center;
  padding: 0 6px;
  font-size: 12px;
  white-space: nowrap;
  -webkit-transform-origin: -10% center;
  -ms-transform-origin: -10% center;
  transform-origin: -10% center;
  z-index: 10;
  -webkit-box-shadow: 0 0 0 1px #fff;
  box-shadow: 0 0 0 1px #fff
}

.ivu-badge-count-custom {
  background: 0 0;
  color: inherit;
  border-color: transparent;
  -webkit-box-shadow: none;
  box-shadow: none
}

.ivu-badge-count a, .ivu-badge-count a:hover {
  color: #fff
}

</style>
