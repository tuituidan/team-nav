<template>
  <div class="app-container">
    <el-row class="mb8" type="flex" justify="space-between">
      <el-col :span="1"></el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="small"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
    </el-row>
    <el-table
      stripe
      border
      v-loading="loading"
      :data="dataList">
      <el-table-column label="序号" type="index" width="55" align="center" />
      <el-table-column label="公告内容" prop="content">
        <template slot-scope="scope">
          <div v-html="scope.row.content"></div>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="[]" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <edit-dialog ref="editDialog"></edit-dialog>
  </div>
</template>

<script>
export default {
  name: "category-index",
  components: {
    'edit-dialog': ()=> import('@/admin/notice/dialog/index.vue')
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 数据
      dataList: [],
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.loading = true;
      this.$http.get(`/api/v1/notice`)
        .then(res => {
          this.dataList = res;
        }).finally(()=>{
        this.loading = false;
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$refs.editDialog.open();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const postId = row.postId || this.ids
      getPost(postId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改岗位";
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const postIds = row.postId || this.ids;
      this.$modal.confirm('是否确认删除岗位编号为"' + postIds + '"的数据项？').then(function() {
        return delPost(postIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
}
</script>

<style scoped>

</style>
