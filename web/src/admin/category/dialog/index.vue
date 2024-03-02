<template>
  <el-dialog :title="title" :visible.sync="show" width="600px" append-to-body :close-on-click-modal="false">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" @submit.native.prevent>
      <el-row>
        <el-col :span="24">
          <el-form-item label="上级分类" prop="pid">
            <category-select ref="refCategory"
                             style="width: 100%"
                             v-model="form.pid"
                             check-strictly
                             placeholder="请选择上级分类"></category-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="分类图标" prop="icon">
            <category-icon-select v-model="form.icon"></category-icon-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="分类名称" prop="name">
            <el-input clearable v-model="form.name" placeholder="请输入分类名称" v-trim/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item prop="roleIds">
              <span slot="label">
                <el-tooltip content='不设置则为公开分类，不登录也可以看到，设置后需要登录且拥有对应角色才能看到' placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                所属角色
              </span>
            <role-select v-model="form.roleIds"></role-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script src="./index.js"></script>

<style scoped>

</style>
