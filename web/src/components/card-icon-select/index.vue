<template>
  <div>
    <el-row type="flex" justify="space-between">
      <el-col :span="8">
        <el-upload
          :action="uploadUrl"
          :show-file-list="false"
          accept="image/x-icon,image/png,image/jpeg,image/jpg,image/gif,image/bmp"
          :on-success="uploadSuccess"
          :limit="1">
          <el-button icon="el-icon-upload" size="small">图标上传</el-button>
        </el-upload>
      </el-col>
      <el-col :span="8">
        <el-popover
          width="540"
          trigger="click"
          @show="$refs['iconSelect'].reset()">
          <icon-select ref="iconSelect" @selected="iconSelect"/>
          <el-button slot="reference" icon="el-icon-search" size="small">图标选择</el-button>
        </el-popover>
      </el-col>
      <el-col :span="8">
        <el-input class="icon-text-input"
                  maxlength="8"
                  placeholder="图标文字"
                  @input="setTextIcon"
                  v-model="textIcon.text" v-trim>
          <el-color-picker slot="suffix"
                           :predefine="predefineColors"
                           v-model="textIcon.color"
                           @change="textIconColorChange"
                           size="small"></el-color-picker>
        </el-input>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24" class="icon-select-container">
        <ivu-badge @click.native="selectIcons(item)"
                   v-for="item in icons" :key="item.src">
          <ivu-avatar v-if="item.src" :src="item.src">
          </ivu-avatar>
          <ivu-avatar :style="{backgroundColor: item.color}" v-else>{{ item.text }}</ivu-avatar>
          <i class="el-icon-success"
             slot="count"
             v-if="item.checked" style="color: #19be6b;font-size: 16px"></i>
        </ivu-badge>
        <div v-if="iconErrTip" class="el-form-item__error">图标不能为空，请点击选择一个图标</div>
      </el-col>
    </el-row>
  </div>

</template>

<script>
export default {
  name: "card-icon-select-index",
  components: {
    'icon-select': () => import('@/components/card-icon-select/card-icon-select.vue'),
    'ivu-badge': () => import('@/components/ivu-badge/index.vue'),
  },
  data() {
    return {
      uploadUrl: `${process.env.VUE_APP_BASE_API}/api/v1/upload/images`,
      iconErrTip: false,
      textIcon: {
        text: '',
        color: ''
      },
      icons: [],
      predefineColors: [
        '#5e35b1',
        '#311b92',
        '#8e24aa',
        '#3949ab',
        '#1a237e',
        '#1e88e5',
        '#0d47a1',
        '#01579b',
        '#006064',
        '#00897b',
        '#2e7d32',
        '#1b5e20',
        '#fb8c00',
        '#ef6c00',
        '#e65100',
        '#6d4c41',
        '#4e342e',
        '#3e2723',
        '#546e7a',
        '#37474f',
      ],
      randomColor: [
        '#5e35b1',
        '#512da8',
        '#4527a0',
        '#311b92',
        '#8e24aa',
        '#7b1fa2',
        '#6a1b9a',
        '#4a148c',
        '#3949ab',
        '#303f9f',
        '#283593',
        '#1a237e',
        '#1e88e5',
        '#1976d2',
        '#1565c0',
        '#0d47a1',
        '#039be5',
        '#0288d1',
        '#0277bd',
        '#01579b',
        '#00acc1',
        '#0097a7',
        '#00838f',
        '#006064',
        '#00897b',
        '#00796b',
        '#00695c',
        '#004d40',
        '#43a047',
        '#388e3c',
        '#2e7d32',
        '#1b5e20',
        '#7cb342',
        '#689f38',
        '#558b2f',
        '#33691e',
        '#fb8c00',
        '#f57c00',
        '#ef6c00',
        '#e65100',
        '#6d4c41',
        '#5d4037',
        '#4e342e',
        '#3e2723',
        '#546e7a',
        '#455a64',
        '#37474f',
        '#263238'
      ]
    }
  },
  mounted() {
    this.textIcon.color = this.randomColor[Math.floor(Math.random() * this.randomColor.length)]
  },
  methods: {
    init(item) {
      this.icons = [];
      if (item) {
        this.icons.push({...item, checked: true});
      }
    },
    uploadSuccess(response) {
      this.iconSelect({src: response});
    },
    setTextIcon() {
      if (!this.textIcon.text) {
        this.icons = this.icons.filter(item => !Boolean(item.text));
        return;
      }
      let icon = this.icons.find(item => Boolean(item.text));
      if (icon) {
        icon.text = this.textIcon.text;
        this.selectIcons(icon);
        return;
      }
      icon = {
        ...this.textIcon,
      };
      this.icons.push(icon);
      this.selectIcons(icon);
    },
    textIconColorChange() {
      let curIcon = this.icons.find(icon => Boolean(icon.color));
      if (curIcon && this.textIcon.color) {
        curIcon.color = this.textIcon.color;
      }
    },
    iconSelect(item) {
      const icon = {src: item.src, checked: true};
      this.icons.push(icon);
      this.selectIcons(icon);
    },
    selectIcons(icon) {
      this.icons.forEach(it => it.checked = false);
      icon.checked = true;
      this.$emit('input', icon);
    }
  }
}
</script>

<style scoped lang="scss">
.icon-text-input {
  ::v-deep .el-input__suffix {
    right: 0;
  }
}

.el-color-picker {
  ::v-deep .el-color-picker__trigger {
    border: none;
  }
}

.icon-select-container {
  margin-top: 5px;
  display: flex;
  flex-wrap: wrap;

  .ivu-badge {
    margin: 5px;
    cursor: pointer;
  }
}

</style>
