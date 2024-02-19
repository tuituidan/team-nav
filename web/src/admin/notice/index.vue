<template>
  <div class="app-container">
    <el-row type="flex" justify="space-between">
      <el-col :span="1"></el-col>
      <el-row :gutter="10" class="mb8 mt5">
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-plus"
            size="small"
            @click="openDialog()"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            size="small"
            icon="el-icon-edit"
            v-btn-single="selections"
            @click="openDialog(selections[0])"
          >修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            size="small"
            icon="el-icon-delete"
            v-btn-multiple="selections"
            @click="handleDelete()"
          >删除
          </el-button>
        </el-col>
      </el-row>
    </el-row>
    <el-table
      stripe
      border
      ref="dataTable"
      v-loading="loading"
      :data="dataList"
      @selection-change="selections = $refs.dataTable.selection">
      <el-table-column label="序号" type="index" width="55" align="center" />
      <el-table-column type="selection" width="50" align="center"/>
      <el-table-column label="公告内容" prop="content">
        <template slot-scope="scope">
          <div v-html="scope.row.content"></div>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status || !Boolean(scope.row.endTime)">启用中</el-tag>
          <el-tag v-else type="danger">已停止</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="openDialog(scope.row)"
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
    <edit-dialog ref="editDialog" @refresh="getList"></edit-dialog>
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
      // 选中数组
      selections: [],
      // 数据
      dataList: [],
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      this.$http.get(`/api/v1/notice`)
        .then(res => {
          this.dataList = res;
        }).finally(()=>{
        this.loading = false;
      });
    },
    /** 修改按钮操作 */
    openDialog(row) {
      this.$refs.editDialog.open(row);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row ? row.id : this.selections.map(item => item.id).join(',');
      this.$modal.confirm('是否确认删除选中的数据项？').then(() => {
        return this.$http.delete(`/api/v1/notice/${ids}`);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    },
  }
}
</script>

<style scoped>

</style>
