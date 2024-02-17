<template>
  <el-dialog :title="title" :visible.sync="show"
             width="600px"
             :close-on-click-modal="false"
             append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="卡片类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio
            v-for="item in types"
            :key="item.id"
            :label="item.id"
          >{{item.name}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="所属分类" prop="category">
        <category-select ref="refCategory"
                         style="width: 100%"
                         v-model="form.category"
                         placeholder="请选择所属分类"></category-select>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="标题" />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input v-model="form.content" type="textarea"
                  maxlength="200"
                  show-word-limit
                  clearable
                  :rows="4"
                  placeholder="请输入内容" />
      </el-form-item>
      <el-form-item label="链接" prop="url">
        <el-input v-model="form.url" placeholder="链接" @blur="getFavicons"/>
      </el-form-item>
      <el-form-item class="form-icon-loading" v-if="showFaviconLoading">
        <el-button loading type="text">正在尝试获取该地址的图标</el-button>
      </el-form-item>
      <el-form-item label="图标" prop="icon">
        <card-icon-select ref="refCardIcon" v-model="form.icon" @input="iconChange" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script src="./index.js"></script>

<style scoped>
.form-icon-loading{
  margin-top: -16px;
  margin-bottom: 0;
}
</style>
