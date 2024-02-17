<template>
  <div class="app-container">
    <el-row>
      <el-col :span="8">
        <role-list @roleRowChange="roleRowChange"></role-list>
      </el-col>
      <el-col :span="15" :offset="1">
        <div style="height: 51px">
          <span v-if="roleDetail.roleName">选择角色：</span>
          <span v-text="roleDetail.roleName"></span>
        </div>
        <el-row>
          <el-col :span="11">
            <el-row class="mb8" type="flex" justify="space-between">
              <el-col :span="18">
                拥有分类：
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-document"
                  size="mini"
                  :disabled="!Boolean(roleDetail.id)"
                  @click="roleCategorySave()"
                >保存
                </el-button>
              </el-col>
            </el-row>
            <el-card shadow="never">
              <el-tree
                ref="categoryTree"
                :data="categories"
                :props="{label: 'name'}"
                node-key="id"
                highlight-current
                :default-checked-keys="roleDetail.categoryIds"
                show-checkbox>
              </el-tree>
            </el-card>
          </el-col>
          <el-col :span="11" :offset="1">
            <el-row class="mb8" type="flex" justify="space-between">
              <el-col :span="18">
                拥有用户：
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-document"
                  size="mini"
                  :disabled="!Boolean(roleDetail.id)"
                  @click="roleUserSave()"
                >保存
                </el-button>
              </el-col>
            </el-row>
            <el-table
              stripe
              border
              highlight-current-row
              ref="dataTable"
              v-loading="loading"
              :data="table.dataList">
              <el-table-column type="selection" width="50" align="center"/>
              <el-table-column label="用户姓名" align="center" prop="nickname"
                               :show-overflow-tooltip="true"/>
            </el-table>
            <pagination
              small
              :pager-count="5"
              layout="sizes,prev,pager,next"
              v-show="table.total>0"
              :total="table.total"
              :page.sync="queryParam.pageIndex"
              :limit.sync="queryParam.pageSize"
              @pagination="getUserList"
            />
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "role-index",
  components: {
    'role-list': () => import('./role-list')
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 查询参数
      queryParam: {
        pageIndex: 0,
        pageSize: 10,
      },
      table: {
        total: 0,
        dataList: []
      },
      categories: [],
      roleDetail: {
        id: '',
        roleName: '',
        categoryIds: [],
        userIds: [],
      },
    };
  },
  mounted() {
    this.getCategoriesTree();
    this.getUserList();
  },
  methods: {
    getCategoriesTree() {
      this.$http.get('/api/v1/category/tree')
        .then(res => {
          this.categories = res;
        })
    },
    getUserList() {
      this.loading = true;
      this.$http.get(`/api/v1/user/page`, {params: this.queryParam})
        .then(res => {
          this.table.dataList = res.content;
          this.table.total = res.totalElements;
          this.loading = false;
        });
    },
    roleRowChange(row) {
      this.$refs.categoryTree.setCheckedKeys([]);
      this.$refs.dataTable.clearSelection();
      this.$http.get(`/api/v1/role/${row.id}`)
        .then(res => {
          this.roleDetail = res;
          this.table.dataList.filter(it => this.roleDetail.userIds.includes(it.id)).forEach(it => {
            this.$refs.dataTable.toggleRowSelection(it);
          })
        })
    },
    roleCategorySave() {
      if (!this.roleDetail.id) {
        return;
      }
      const keys = this.$refs.categoryTree.getCheckedKeys();
      this.$http.patch(`/api/v1/role/${this.roleDetail.id}/category`, keys)
        .then(() => {
          this.$modal.msgSuccess("保存成功");
          this.roleRowChange({id:this.roleDetail.id});
        })
    },
    roleUserSave() {
      if (!this.roleDetail.id) {
        return;
      }
      this.$http.patch(`/api/v1/role/${this.roleDetail.id}/user`, {
        checkIds: this.$refs.dataTable.selection.map(it=>it.id),
        userIds: this.table.dataList.map(it=>it.id)
      })
        .then(() => {
          this.$modal.msgSuccess("保存成功");
          this.roleRowChange({id:this.roleDetail.id});
        })
    },
  }
}
</script>

<style scoped>

</style>
