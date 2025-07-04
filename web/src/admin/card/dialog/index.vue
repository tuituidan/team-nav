<template>
  <el-dialog :title="title" :visible.sync="show"
             width="1100px"
             :close-on-click-modal="false"
             append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="110px" @submit.native.prevent>
      <el-row>
        <el-col :span="14">
          <el-form-item label="卡片类型" prop="type">
         <span slot="label">
          <el-tooltip placement="top">
            <div slot="content">
              普通卡片：内容型卡片，通过添加链接跳转对应网址<br/>
              静态网站：上传静态网站的zip压缩包，自动生成可访问链接<br/>
              http动态卡片：通过http接口动态获取的卡片显示内容
              sql动态卡片：通过sql动态获取的卡片显示内容
            </div>
            <i class="el-icon-question"></i>
          </el-tooltip>
          卡片类型
        </span>
            <el-radio-group v-model="form.type">
              <el-radio
                v-for="item in $store.getters.cardTypes"
                :key="item.id"
                :label="item.id"
                :disabled="Boolean(form.id)"
              >{{ item.name }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="所属分类" prop="category">
            <category-select ref="refCategory"
                             style="width: 100%"
                             v-model="form.category"
                             placeholder="请选择所属分类"></category-select>
          </el-form-item>
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="标题" clearable v-trim/>
          </el-form-item>
          <el-form-item label="动态内容" prop="dynamicBuilder" v-if="form.type.startsWith('dynamic')">
            <card-http-builder v-if="form.type === 'dynamic-http'" v-model="form.dynamicBuilder"></card-http-builder>
            <card-sql-builder v-if="form.type === 'dynamic-sql'" v-model="form.dynamicBuilder"></card-sql-builder>
          </el-form-item>
          <el-form-item label="内容" prop="content" v-else>
            <el-input v-model="form.content" type="textarea" v-trim
                      maxlength="200"
                      show-word-limit
                      clearable
                      :rows="3"
                      placeholder="请输入内容"/>
          </el-form-item>
          <el-form-item label="链接"
                        v-if="form.type==='default' || form.type==='dynamic-http' || form.type==='dynamic-sql'"
                        :rules="[{required: form.showQrcode, message:'链接不能为空', trigger:'blue' }]"
                        prop="url">
            <el-row>
              <el-col :span="18">
                <el-input v-model="form.url" placeholder="链接" @blur="getFavicons" clearable v-trim/>
              </el-col>
              <el-col :span="5" :offset="1">
                <el-checkbox v-model="form.showQrcode">显示二维码</el-checkbox>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="网站文件" prop="zip" v-else>
            <file-uploader type="modules"
                           accept="application/zip"
                           :file-list="form.zip?[form.zip]:[]"
                           @file-change="zipFileChange"
                           :limit="1">zip压缩包上传
            </file-uploader>
          </el-form-item>
          <el-form-item class="form-icon-loading" v-if="showFaviconLoading">
            <el-button loading type="text">正在尝试获取该地址的图标</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="私密内容" prop="privateContent">
        <span slot="label">
          <el-tooltip content='登录后才能看到的信息' placement="top">
          <i class="el-icon-question"></i>
          </el-tooltip>
          私密信息
        </span>
            <el-input v-model="form.privateContent" type="textarea" v-trim
                      maxlength="200"
                      show-word-limit
                      clearable
                      :rows="2"
                      placeholder="请输入内容"/>
          </el-form-item>
          <el-form-item label="图标" prop="icon">
            <card-icon-select ref="refCardIcon" v-model="form.icon"/>
          </el-form-item>
          <el-form-item label="附件" prop="attachment">
            <file-uploader type="attachments"
                           :file-list="form.attachments?form.attachments:[]"
                           multiple
                           @file-change="attachmentChange"
                           :limit="10">附件上传
            </file-uploader>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-checkbox v-if="!Boolean(form.id)" v-model="saveOption.saveKeepAdd">保存后不关闭</el-checkbox>
      <el-checkbox v-if="!Boolean(form.id)" v-model="saveOption.saveNotClear" style="margin-right: 25px">保存后不清空
      </el-checkbox>
      <el-button v-if="apply" type="success" @click="submitForm">通 过</el-button>
      <el-button v-else type="primary" @click="submitForm">保 存</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script src="./index.js"></script>

<style scoped lang="scss">
.form-icon-loading {
  margin-top: -16px;
  margin-bottom: 0;
}

.dialog-footer {
  text-align: center;
}
</style>
