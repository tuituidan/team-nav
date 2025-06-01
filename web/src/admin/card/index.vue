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
            @click="openDialog({category: queryParams.category, type: 'default'})"
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
            :disabled="dataList.length<2"
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
      :data="dataList"
      @selection-change="selections = $refs.dataTable.selection">
      <el-table-column label="序号" type="index" width="55" align="center"/>
      <el-table-column type="selection" width="50" align="center"/>
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
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-copy"
            @click="openDialog(scope.row, true)"
          >复制
          </el-button>
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
    <sort-dialog ref="sortDialog" @change-sort="changeSort"></sort-dialog>
  </div>
</template>

<script>
export default {
  name: "card-index",
  components: {
    'edit-dialog': () => import("@/admin/card/dialog/index.vue"),
    'category-select': () => import('@/components/category-select/index.vue'),
    'sort-dialog': () => import('@/admin/components/sort-dialog'),
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      selections: [],
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
      if (!this.queryParams.category) {
        return;
      }
      this.loading = true;
      this.$http.get(`/api/v1/category/${this.queryParams.category}/card`)
        .then(res => {
          this.dataList = res;
        }).finally(() => {
        this.loading = false;
      });
    },
    openDialog(item, isCopy) {
      if(isCopy){
        const data = JSON.parse(JSON.stringify(item));
        data.id = null;
        data.sort = null;
        this.$refs.editDialog.open(data);
        return;
      }
      this.$refs.editDialog.open(item);
    },
    openSortDialog() {
      this.$refs.sortDialog.open(this.dataList, {label: 'title'});
    },
    changeSort(draggingData, dropData, type) {
      const data = {
        draggingId: draggingData.id,
        dropId: dropData.id,
        type: type
      };
      this.$http.patch(`/api/v1/category/${this.queryParams.category}/card/actions/sort`, data).then(() => {
        this.$modal.msgSuccess("排序成功");
        this.getList();
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row ? row.id : this.selections.map(item => item.id).join(',');
      this.$modal.confirm('是否确认删除选中的数据项？').then(() => {
        return this.$http.delete(`/api/v1/card/${ids}`);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    },
  }
}
</script>

<style scoped lang="scss">

</style>
