<template>
  <div class="app-container">
    <el-row type="flex" justify="space-between">
      <el-form ref="queryForm" @submit.native.prevent>
        <el-form-item>
          <el-input placeholder="请输入分类名称"
                    style="width: 340px"
                    v-model="keywords"
                    clearable
                    @clear="getList"
                    @keyup.enter.native="getList">
            <el-button slot="append" icon="el-icon-search" @click="getList"></el-button>
          </el-input>
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8 mt5">
        <el-col :span="1.5">
          <el-button
            type="info"
            plain
            icon="el-icon-sort"
            size="small"
            @click="toggleExpandAll()"
          >展开/折叠
          </el-button>
        </el-col>
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
            type="warning"
            plain
            size="small"
            icon="el-icon-remove-outline"
            v-btn-multiple="selections"
            @click="handleRemove()"
          >移除
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
        <el-col :span="1.5">
          <el-button
            plain
            size="small"
            icon="el-icon-delete"
            :disabled="categoryList.length<=0"
            @click="openSortDialog()"
          >排序
          </el-button>
        </el-col>
      </el-row>
    </el-row>
    <el-table
      stripe
      border
      ref="dataTable"
      v-loading="loading"
      :data="categoryList"
      row-key="id"
      v-if="refreshTable"
      @selection-change="selections = $refs.dataTable.selection"
      :default-expand-all="isExpandAll">
      <el-table-column label="序号" type="index" width="55" align="center"/>
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <el-table-column prop="name" label="分类名称" :show-overflow-tooltip="true" width="300"></el-table-column>
      <el-table-column prop="icon" label="图标" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon v-if="scope.row.icon" :icon-class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="60"></el-table-column>
      <el-table-column prop="cardCount" label="卡片数" width="60"></el-table-column>
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="openDialog(scope.row)"
          >修改
          </el-button>
          <el-button
            v-if="scope.row.level<3"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="openDialog({pid:scope.row.id})"
          >新增
          </el-button>
          <el-button
            slot="reference"
            size="mini"
            type="text"
            icon="el-icon-remove-outline"
            @click="handleRemove(scope.row)"
          >移除
          </el-button>
          <el-button
            slot="reference"
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
    <sort-dialog ref="sortDialog" @change-sort="changeSort"></sort-dialog>
  </div>
</template>

<script>

export default {
  name: "category-valid",
  components: {
    'edit-dialog': () => import('@/admin/category/dialog/index.vue'),
    'sort-dialog': () => import('@/admin/components/sort-dialog'),
  },
  data() {
    return {
      // 选中数组
      selections: [],
      // 是否展开，默认全部折叠
      isExpandAll: false,
      // 重新渲染表格状态
      refreshTable: true,
      // 遮罩层
      loading: true,
      // 表格树数据
      categoryList: [],
      // 查询参数
      keywords: '',
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      this.$http.get('/api/v1/category', {params: {keywords: this.keywords}})
        .then(res => {
          this.categoryList = res;
        })
        .finally(() => {
          this.loading = false;
        })
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    openSortDialog() {
      this.$refs.sortDialog.open(this.categoryList);
    },
    changeSort(draggingData, dropData, type) {
      const data = {
        draggingId: draggingData.id,
        dropId: dropData.id,
        parentId: dropData.pid,
        type: type
      };
      this.$http.patch('/api/v1/category/actions/sort', data).then(() => {
        this.$modal.msgSuccess("排序成功");
        this.getList();
      })
    },
    openDialog(item) {
      this.$refs.editDialog.open(item);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row ? row.id : this.selections.map(item => item.id).join(',');
      this.$modal.confirm('将被永久删除，是否确认删除选中的数据项？').then(() => {
        return this.$http.delete(`/api/v1/category/${ids}`);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    },
    handleRemove(row) {
      const ids = row ? [row.id] : this.selections.map(item => item.id);
      this.$modal.confirm('将移除到历史分类，是否确认移除选中的数据项？').then(() => {
        return this.$http.patch('/api/v1/category/valid/false', ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已移除到历史分类");
      });
    },
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
}
</style>
