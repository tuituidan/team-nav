<template>
  <el-popover
    placement="bottom-start"
    width="460"
    trigger="click"
    @show="$refs['iconSelect'].reset()"
  >
    <icon-select ref="iconSelect" @selected="selected" :active-icon="value"/>
    <el-input slot="reference" :value="value"
              :placeholder="placeholder">
      <svg-icon
        v-if="value"
        slot="prefix"
        :icon-class="value"
        style="width: 25px;"
      />
      <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
    </el-input>
  </el-popover>
</template>

<script>

export default {
  name: "category-icon-select-index",
  components: {
    'icon-select': () => import('@/components/IconSelect/index.vue'),
  },
  props: {
    value: {
      type: String,
    },
    placeholder: {
      type: String,
      default: '点击选择图标'
    },
  },
  data(){
    return {

    }
  },
  methods: {
    // 选择图标
    selected(name) {
      this.$emit('input', name);
    },
  }
}
</script>

<style scoped>

</style>
