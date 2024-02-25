<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="分类列表" name="panel-valid">
        <category-valid ref="categoryValid"></category-valid>
      </el-tab-pane>
      <el-tab-pane label="历史分类" name="panel-invalid" lazy>
        <category-invalid ref="categoryInvalid"></category-invalid>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: "category-index",
  components: {
    'category-valid': () => import('@/admin/category/category-valid.vue'),
    'category-invalid': () => import('@/admin/category/category-invalid.vue'),
  },
  data() {
    return {
      activeName: 'panel-valid',
    };
  },
  methods: {
    handleClick(tab, event) {
      if (tab.name === 'panel-valid') {
        this.$refs.categoryValid.getList();
      } else {
        this.$refs.categoryInvalid && this.$refs.categoryInvalid.getList();
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding-top: 10px;
  padding-bottom: 15px;
}

//.tree-header-container {
//  display: flex;
//  background-color: #f8f8f9;
//  color: #515a6e;
//  border-top: 1px solid #dfe6ec;
//
//  .col-category {
//    width: 325px;
//    text-align: center;
//    border-left: 1px solid #dfe6ec;
//
//    .el-input {
//      width: 300px;
//      padding-top: 5px;
//    }
//  }
//
//  .col-handler {
//    padding: 8px 0;
//  }
//
//  .col-card-count {
//    padding: 8px 0;
//  }
//
//  .col-role {
//    text-align: center;
//    border-right: 1px solid #dfe6ec;
//    flex: 1;
//    padding: 8px 0;
//  }
//}
//
//.col-handler {
//  width: 85px;
//  text-align: center;
//  border-left: 1px solid #dfe6ec;
//  border-right: 1px solid #dfe6ec;
//}
//
//.col-card-count {
//  width: 80px;
//  text-align: center;
//  border-right: 1px solid #dfe6ec;
//  line-height: 26px;
//}
//
//.el-tree {
//  border: 1px solid #dfe6ec;
//}
</style>
