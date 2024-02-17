<template>
  <div>
    <el-form :model="queryParam" ref="queryForm"
             @submit.native.prevent
             size="small" :inline="true" label-width="68px">
      <el-form-item>
        <el-input
          v-model="queryParam.keywords"
          placeholder="请输入角色名称进行搜索"
          clearable
          @clear="getList"
          style="width: 300px"
          @keyup.enter.native="getList"
          suffix-icon="el-icon-search"
        />
      </el-form-item>
    </el-form>
    <el-row class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="openEditDialog()"
        >新增
        </el-button>
      </el-col>
    </el-row>
    <el-table
      stripe
      border
      highlight-current-row
      ref="dataTable"
      v-loading="loading"
      @row-click="roleRowClickHandler"
      @current-change="roleRowChange"
      :data="table.dataList">
      <el-table-column label="选择" width="50" align="center">
        <template slot-scope="scope">
          <el-radio v-model="selectRow" :label="scope.row.id"><i></i></el-radio>
        </template>
      </el-table-column>
      <el-table-column label="角色名称" align="center" prop="roleName"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="openEditDialog(scope.row)"
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
    <pagination
      :pager-count="5"
      layout="total,sizes,prev,pager,next"
      v-show="table.total>0"
      :total="table.total"
      :page.sync="queryParam.pageIndex"
      :limit.sync="queryParam.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
export default {
  name: "role-list",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 查询参数
      queryParam: {
        pageIndex: 0,
        pageSize: 10,
        keywords: ''
      },
      table: {
        total: 0,
        dataList: []
      },
      selectRow: '',
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      this.$http.get(`/api/v1/role/page`, {params: this.queryParam})
        .then(res => {
          this.table.dataList = res.content;
          this.table.total = res.totalElements;
          this.loading = false;
        });
    },
    roleRowClickHandler(row) {
      this.selectRow = row.id;
    },
    roleRowChange(row) {
      if (row) {
        this.$emit('roleRowChange', row);
      }
    },
    /** 修改按钮操作 */
    openEditDialog(row) {
      const title = row ? '编辑角色' : '添加角色';
      this.$prompt('', title, {
        inputValue: row ? row.roleName : '',
        inputPlaceholder: '请输入角色名称'
      }).then(({value}) => {
        this.$http.save('/api/v1/role', {id: row ? row.id : '', roleName: value})
          .then(() => {
            this.$modal.msgSuccess("保存成功");
            this.show = false;
            this.getList();
          });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm(`是否确认删除【${row.roleName}】数据项？`)
        .then(() => {
          return this.$http.delete(`/api/v1/role/${row.id}`);
        }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  }
}
</script>

<style scoped>

</style>