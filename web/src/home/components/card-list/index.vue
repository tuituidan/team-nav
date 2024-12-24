<template>
  <div >
  <div v-for="(category, index) in homeCards" :key="category.id" class="category-main">
    <div :id="category.id" :class="{'anchor-point-first':index===0}" class="anchor-point"></div>
    <div v-if="Array.isArray(category.children)&&category.children.length===1" class="category-title">
      <span v-text="category.name + ' / ' + category.children[0].name"></span>
    </div>
    <div v-else class="category-title">
      <span v-text="category.name"></span>
    </div>
    <el-tabs v-if="Array.isArray(category.children)&&category.children.length>1" :value="category.children[0].id">
      <el-tab-pane v-for="child in category.children" :key="child.id" :label="child.name" :name="child.id" lazy>
        <category-children :datas="child.cards"></category-children>
      </el-tab-pane>
    </el-tabs>
    <category-children v-else-if="Array.isArray(category.children)&&category.children.length===1"
                       :datas="category.children[0].cards"
    >
    </category-children>
    <category-children v-else :datas="category.cards"></category-children>
  </div>
  <el-empty v-if="homeCards.length<=0" description="没有任何数据"></el-empty>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'

export default {
  name: 'card-list',
  components: {
    'category-children': () => import('@/home/components/category-children/index.vue')

  },
  computed: {
    ...mapGetters([
      'homeCards'
    ])
  },
  methods: {}
}
</script>



<style lang="scss" scoped>
.category-main {
  .anchor-point {
    position: relative;
    top: -18px;
  }
  .anchor-point-first{
    top: -20px;
  }

  .el-tabs {
    ::v-deep .el-tabs__header {
      margin: 8px 0 0 20px;

      .el-tabs__nav-wrap::after {
        display: none;
      }

      .el-tabs__active-bar {
        display: none;
      }

      .el-tabs__nav {
        background-color: #e0e0e0;
        border-radius: 12px;
        height: 24px;

        .el-tabs__item {
          border-radius: 12px;
          text-align: center;
          padding: 0 18px;
          height: 24px;
          line-height: 24px;
          color: #515a6e;
        }

        .el-tabs__item.is-active {
          background-color: #2b85e4;
          color: white;
        }
      }
    }
  }

  .category-title {
    font-size: 16px;
    padding-left: 20px;
    color: #515a6e;
  }
}

.blink-box {
  animation: glow 800ms ease-out infinite alternate;
}

@keyframes glow {
  0% {
    border-color: red;
    box-shadow: 0 0 5px rgba(255, 0, 0, .2), inset 0 0 5px rgba(255, 0, 0, .1), 0 0 0 red;
  }
  100% {
    border-color: red;
    box-shadow: 0 0 20px rgba(255, 0, 0, .6), inset 0 0 10px rgba(255, 0, 0, .4), 0 0 0 red;
  }
}
</style>
<style lang="scss">
// fix css style bug in open el-dialog
//.el-popup-parent--hidden {
//  .fixed-header {
//    padding-right: 6px;
//  }
//}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>
