<template>
  <div class="icon-body">
    <el-input v-model="name" class="icon-search" clearable
              placeholder="请输入图标名称" @clear="filterIcons" @input="filterIcons">
      <i slot="suffix" class="el-icon-search el-input__icon"/>
    </el-input>
    <div class="icon-list">
      <div class="list-container">
        <div v-for="item in filterList" class="icon-item-wrapper"
             :key="item.name" @click="selectedIcon(item)">
          <el-avatar :src="item.src"
                     style="background: none;vertical-align: middle"></el-avatar>
          <div style="font-size: 10px" v-text="item.name"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'card-icon-select',
  data() {
    return {
      name: '',
      filterList: [],
      iconList: [],
    }
  },
  mounted() {
    this.loadIcons();
  },
  methods: {
    loadIcons() {
      this.$http.get('/api/v1/card/icons')
        .then(res => {
          this.filterList = this.iconList = res.map(filename => ({
            src: `/ext-resources/images/default/${filename}`,
            name: filename.substring(0, filename.lastIndexOf('.'))
          }));
        })
    },
    filterIcons() {
      if (this.name) {
        this.filterList = this.iconList.filter(item => item.name.includes(this.name))
      } else {
        this.filterList = this.iconList
      }
    },
    selectedIcon(icon) {
      this.$emit('selected', icon)
      document.body.click()
    },
    reset() {
      this.name = '';
      this.filterList = this.iconList
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.icon-body {
  width: 100%;
  padding: 5px;

  .icon-search {
    position: relative;
    margin-bottom: 5px;
  }

  .icon-list {
    height: 200px;
    overflow: auto;

    .list-container {
      display: flex;
      flex-wrap: wrap;

      .icon-item-wrapper {
        cursor: pointer;
        width: 60px;
        height: 60px;
        text-align: center;
      }
    }
  }
}
</style>
