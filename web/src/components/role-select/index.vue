<template>
  <el-select style="width: 100%"
             multiple :value="value" @change="onChange" placeholder="请选择角色" clearable>
    <el-option
      v-for="item in roleOptions"
      :key="item.id"
      :label="item.roleName"
      :value="item.id"
    />
  </el-select>
</template>

<script>
export default {
  name: "role-select-index",
  props: {
    value: {
      type: Array
    }
  },
  data() {
    return {
      roleOptions: [],
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$http.get('/api/v1/role')
        .then(res => {
          this.roleOptions = res;
        })
    },
    onChange(value) {
      this.$emit('input', value);
    }
  }
}
</script>

<style scoped>

</style>
