<template>
  <div>
    <el-row v-for="(item, index) in value" :key="item+index" :gutter="10"
            :class="{'header-row':index>0}">
      <el-col :span="11">
        <el-input v-model="item.key" placeholder="请输入键名" maxlength="100" clearable v-trim/>
      </el-col>
      <el-col :span="11">
        <el-input v-model="item.value" placeholder="请输入键值" maxlength="100" clearable v-trim/>
      </el-col>
      <el-col :span="2">
        <i v-if="value.length===1" @click="addHandler"
           class="el-icon-circle-plus-outline header-icon header-icon-add"></i>
        <i v-else @click="removeHandler(index)" class="el-icon-remove-outline header-icon header-icon-remove"></i>
      </el-col>
    </el-row>
    <el-row :gutter="10" class="header-row" v-if="value.length>1 || value.length<=0">
      <el-col :span="22">
        <el-input style="visibility: hidden"/>
      </el-col>
      <el-col :span="2">
        <i @click="addHandler"
           class="el-icon-circle-plus-outline header-icon header-icon-add"></i>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "key-value-builder",
  props: {
    value: {
      type: Array,
    },
  },
  methods: {
    addHandler() {
      this.value.push({type: '', key: '', value: ''})
    },
    removeHandler(index) {
      this.value.splice(index, 1)
    },
  },
}
</script>

<style scoped>
.header-row {
  margin-top: 8px;
}


.header-icon {
  vertical-align: middle;
  font-size: 20px;
  cursor: pointer;
}

.header-icon-add {
  color: #67c23a;
}

.header-icon-remove {
  color: #f56c6c;
}
</style>
