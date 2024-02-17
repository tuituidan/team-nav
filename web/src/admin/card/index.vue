<template>
  <div class="app-container">
    <el-row type="flex" justify="space-between">
      <el-form ref="queryForm"
               @submit.native.prevent :inline="true" label-width="50px">
        <el-form-item label="分类" prop="category">
          <category-select ref="refCategory"
                           style="width: 340px"
                           v-model="queryParams.category"
                           load-first
                           @change="getList"
                           placeholder="请选择所属分类"></category-select>
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8 mt5">
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            size="small"
            icon="el-icon-plus"
            @click="openDialog()"
          >新增
          </el-button>
        </el-col>
      </el-row>
    </el-row>
    <el-table
      stripe
      border
      v-loading="loading"
      :data="dataList">
      <el-table-column label="序号" type="index" width="55" align="center"/>
      <el-table-column label="分类" align="center" prop="categoryName" show-overflow-tooltip/>
      <el-table-column label="图标" align="center" width="80" prop="icon" class-name="narrow-padding">
        <template slot-scope="scope">
          <ivu-avatar v-if="scope.row.icon.src"
                      :src="scope.row.icon.src"
                      style="vertical-align: middle"></ivu-avatar>
          <ivu-avatar v-else
                      style="vertical-align: middle"
                      :style="{background: scope.row.icon.color}">{{ scope.row.icon.text }}
          </ivu-avatar>
        </template>
      </el-table-column>
      <el-table-column label="标题" align="center" prop="title" show-overflow-tooltip/>
      <el-table-column label="内容" align="center" prop="content" show-overflow-tooltip/>
      <el-table-column label="链接" align="center" prop="url" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="openDialog(scope.row)"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <edit-dialog ref="editDialog" @refresh="getList"></edit-dialog>
  </div>
</template>

<script>
export default {
  name: "card-index",
  components: {
    'edit-dialog': () => import("@/admin/card/dialog/index.vue"),
    'category-select': () => import('@/components/category-select/index.vue'),
    'ivu-avatar': () => import('@/components/ivu-avatar/index.vue'),
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      dataList: [],
      // 查询参数
      queryParams: {
        category: ''
      },
    };
  },
  mounted() {

  },
  methods: {
    getList() {
      this.loading = true;
      this.$http.get(`/api/v1/category/${this.queryParams.category}/card`).then(res => {
        this.dataList = res;
        this.loading = false;
      });
    },
    openDialog(item) {
      this.$refs.editDialog.open(item);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const postIds = row.postId || this.ids;
      this.$modal.confirm('是否确认删除岗位编号为"' + postIds + '"的数据项？').then(function () {
        return delPost(postIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      })
    },
  }
}
</script>

<style scoped lang="scss">

</style>
