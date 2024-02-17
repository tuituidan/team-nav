<template>
  <div class="app-container">
    <el-row type="flex" justify="space-between">
      <el-form ref="queryForm" :model="queryParam"
               @submit.native.prevent
               :inline="true" label-width="50px">
        <el-form-item>
          <el-input placeholder="请输入分类名称"
                    style="width: 340px"
                    v-model="queryParam.keywords"
                    clearable
                    @clear="getList"
                    @keyup.enter.native="getList">
            <el-button slot="append" icon="el-icon-search" @click="getList"></el-button>
          </el-input>
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="info"
            class="ivu-info-style"
            plain
            size="small"
            icon="el-icon-circle-plus-outline"
            @click="batchSetValid()"
          >批量还原
          </el-button>
        </el-col>
      </el-row>
    </el-row>
    <el-table
      stripe
      border
      ref="dataTable"
      v-loading="loading"
      :data="table.dataList">
      <el-table-column label="序号" type="index" width="55" align="center"/>
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <el-table-column label="分类" prop="name" show-overflow-tooltip/>
      <el-table-column prop="icon" label="图标" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon v-if="scope.row.icon" :icon-class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column label="卡片数" align="center" prop="cardCount" show-overflow-tooltip/>
      <el-table-column label="可查看角色（不配置则任何人都可查看）" align="center" prop="roles">
        <template slot-scope="scope">
          <span>{{ scope.row.roles && scope.row.roles.map(item => (item.roleName)).join(',') }}</span>
        </template>
        <template slot="header">
          <span>
             <el-tooltip content='不设置则为公开分类，不登录也可以看到，设置后需要登录且拥有对应角色才能看到' placement="top">
                <i class="el-icon-question"></i>
             </el-tooltip>
             所属角色
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-plus-outline"
            @click="batchSetValid(scope.row.id)"
          >还原
          </el-button>
          <el-popconfirm
            class="el-confirm-span"
            title="将被永久删除，您确定要删除此项吗？"
            @confirm="deleteItem(scope.row.id)"
          >
            <el-button
              slot="reference"
              size="mini"
              type="text"
              icon="el-icon-delete"
            >删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination
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
  name: "category-invalid",
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
      }
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      this.$http.get(`/api/v1/category/page`, {params: this.queryParam})
        .then(res => {
          this.table.dataList = res.content;
          this.table.total = res.totalElements;
          this.loading = false;
        });
    },
    batchSetValid(id) {
      let ids;
      if (id) {
        ids = [id];
      } else {
        ids = this.$refs.dataTable.selection.map(it => it.id);
      }
      if (!ids.length) {
        this.$modal.notifyWarning('请选择要还原的项');
        return;
      }
      this.$http.patch(`/api/v1/category/valid/true`, ids)
        .then(() => {
          this.$modal.notifySuccess('已还原');
          this.queryParam.pageIndex = 1;
          this.getList();
        });
    },
    deleteItem(id) {
      this.$http.delete(`/api/v1/category/${id}`)
        .then(() => {
          this.$modal.notifySuccess('删除成功');
          this.getList();
        });
    },
  }
}
</script>

<style scoped lang="scss">
.app-container {
  padding: 0;
}

.ivu-info-style {
  color: #2db7f5;
  background-color: #e8f4ff;
  border-color: #a3d3ff;
}

.ivu-info-style:hover {
  background-color: #2db7f5;
  color: white;
  border-color: #2db7f5;
}
</style>
