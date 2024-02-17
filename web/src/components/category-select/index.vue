<template>
  <el-cascader
    clearable
    filterable
    :placeholder="placeholder"
    :value="value"
    :options="options"
    :props="{ expandTrigger: 'hover', emitPath: false, checkStrictly: checkStrictly, value: 'id', label: 'name'  }"
    @input="change"
    @change="change">
  </el-cascader>
</template>

<script>
export default {
  name: "category-select",
  props: {
    value: {
      type: String,
    },
    placeholder: {
      type: String,
    },
    loadFirst: {
      type: Boolean,
    },
    checkStrictly: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      options: []
    };
  },
  model: {
    prop: 'value',
    event: 'change'
  },
  mounted() {
    if (this.loadFirst) {
      this.init();
    }
  },
  methods: {
    init(level = 3) {
      this.$http.get(`/api/v1/category/tree?level=${level}`)
        .then(res => {
          this.options = res;
          if (this.loadFirst) {
            this.$emit("change", this.searchFirstLeaf(this.options));
          }
        });
    },
    searchFirstLeaf(list) {
      if (!list.length) {
        return '';
      }
      if (list[0].children) {
        return this.searchFirstLeaf(list[0].children);
      }
      return list[0].id;
    },
    change(value) {
      this.$emit("change", value);
    }
  }
}
</script>

<style scoped>

</style>
