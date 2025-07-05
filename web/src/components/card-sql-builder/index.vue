<template>
  <div>
    <div class="datasource-select-container">
      <el-select v-model="value.datasourceId" placeholder="请选择数据源" clearable @change="changeInput">
        <el-popover
          placement="right"
          width="600"
          trigger="hover"
          v-for="item in datasourceList" :key="item.id">
          <el-descriptions :column="1" border labelClassName="desc-label">
            <el-descriptions-item label="数据库类型">{{ item.type }}</el-descriptions-item>
            <el-descriptions-item label="jdbcUrl">{{ item.jdbcUrl }}</el-descriptions-item>
            <el-descriptions-item label="数据库账号">{{ item.username }}</el-descriptions-item>
          </el-descriptions>
          <el-option slot="reference"
                     :label="item.name"
                     :value="item.id">
            <div>
              <span class="datasource-name">{{ item.name }}</span>
              <i  v-if="$store.getters.loginUser.id && $store.getters.loginUser.isAdmin"
                  class="el-icon-delete datasource-delete" @click="deleteDatasource(item)"></i>
            </div>
          </el-option>
        </el-popover>
      </el-select>
      <div class="datasource-add-container" v-if="$store.getters.loginUser.id && $store.getters.loginUser.isAdmin">
        <i class="el-icon-circle-plus-outline datasource-add" @click="openAddDatasource()"></i>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-input type="textarea"
                v-model="value.sql"
                @input="changeInput"
                placeholder="请输入查询sql，仅支持返回一行一列的数据"
                :autosize="{ minRows: 3, maxRows: 8}"></el-input>
    </div>
    <datasource-edit ref="refEdit" @refresh="loadDatasource"></datasource-edit>
  </div>
</template>

<script>
export default {
  name: "card-sql-builder",
  components: {
    'datasource-edit': () => import("./datasource-edit.vue"),
  },
  props: {
    value: {
      type: Object,
      default: () => {
        return {
          datasourceId: '',
          sql: '',
        }
      },
    },
  },
  data() {
    return {
      datasourceList: [],
      showTable: false,
    }
  },
  mounted() {
    this.loadDatasource();
  },
  methods: {
    loadDatasource() {
      this.datasourceList = [];
      this.$http.get('/api/v1/datasource')
        .then(res => {
          this.datasourceList = res;
        })
    },
    openAddDatasource() {
      this.$refs.refEdit.open();
    },
    deleteDatasource(item) {
      this.$modal.confirm('是否确认删除选中的数据源？').then(() => {
        return this.$http.delete(`/api/v1/datasource/${item.id}`);
      }).then(() => {
        this.loadDatasource();
        this.$modal.msgSuccess("删除成功");
      });
    },
    changeInput() {
      this.$emit('input', this.value)
    },
  }
}
</script>

<style scoped lang="scss">
.datasource-select-container {
  display: flex;
  justify-content: space-between;

  .el-select {
    flex: 1;
  }
}

.datasource-add-container {
  width: 40px;
  text-align: center
}

::v-deep .desc-label {
  width: 120px;
}

.datasource-name {
  float: left;
}

.datasource-add {
  color: #67c23a;
  font-size: 24px;
  vertical-align: middle;
  cursor: pointer;
}

.datasource-delete {
  font-size: 20px;
  line-height: 32px;
  float: right;
  color: #F56C6C;
}
</style>
