<template>
  <div class="category-children">
    <div class="card-sm card-md card-xl card-xxl" v-for="card in datas" :key="card.id">
      <el-popover trigger="hover" popper-class="my-popover" :close-delay="0">
        <el-card class="nav-card"
                 shadow="hover"
                 slot="reference"
                 @click.native="cardClickHandler(card)">
          <div style="display: flex">
            <ivu-avatar v-if="card.icon.src"
                        :src="card.icon.src"
                        class="nav-card-icon"></ivu-avatar>
            <ivu-avatar v-else
                        class="nav-card-icon"
                        :style="{background: card.icon.color}">{{ card.icon.text }}
            </ivu-avatar>
            <div class="nav-card-content">
              <div v-text="card.title" class="card-title"></div>
              <div v-text="card.content" class="card-content"></div>
            </div>
          </div>
        </el-card>
        <div slot="default">
          <div v-html="card.tip"></div>
          <div v-if="card.showQrcode">
            <img :src="showQrcodeHandler(card.url)" alt=""/>
          </div>
        </div>
      </el-popover>
    </div>
  </div>
</template>

<script>
export default {
  name: "category-children",
  props: {
    datas: {
      type: Array
    }
  },
  methods: {
    cardClickHandler(card){
      if (card.url) {
        window.open(card.url);
      }
    },
    showQrcodeHandler(url){
      return `${process.env.VUE_APP_PROXY_URL}/api/v1/qrcode?url=${encodeURIComponent(url)}`;
    }
  }
}
</script>


<style lang="scss" scoped>
.category-children {
  display: flex;
  flex-wrap: wrap;
  padding-left: 20px;
  padding-bottom: 20px;

  .nav-card {
    border: none;
    box-shadow: 0 2px 2px 0 rgba(16, 116, 231, 0.14), 0 1px 5px 0 rgba(16, 116, 231, 0.12), 0 3px 1px -2px rgba(16, 116, 231, 0.2);
    cursor: pointer;

    ::v-deep .el-card__body {
      padding: 13px;
    }

    .nav-card-icon {
      vertical-align: middle;
      width: 40px
    }

    .nav-card-content {
      width: calc(100% - 46px);
      margin-left: 6px
    }

    .card-title {
      font-size: 14px;
      font-weight: bold;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .card-content {
      font-size: 13px;
      color: #0366d6;
      display: block;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      margin-top: 4px;
    }
  }

  .nav-card:hover {
    box-shadow: 0 8px 17px 0 rgba(3, 102, 214, 0.2), 0 6px 20px 0 rgba(3, 102, 214, 0.19);
    border-color: #eee;
  }
}

.card-sm {
  -ms-flex: 0 0 50%;
  flex: 0 0 50%;
  max-width: 50%;
  padding-right: 20px;
  margin-top: 20px;
}

@media (min-width: 768px) {
  .card-md {
    -ms-flex: 0 0 25%;
    flex: 0 0 25%;
    max-width: 25%;
  }
}


@media (min-width: 1200px) {
  .card-xl {
    -ms-flex: 0 0 20%;
    flex: 0 0 20%;
    max-width: 20%;
  }
}

@media (min-width: 1680px) {
  .card-xxl {
    -ms-flex: 0 0 16.666%;
    flex: 0 0 16.666%;
    max-width: 16.666%;
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

.my-popover {
  background-color: rgba(70, 76, 91, .9);
  color: white;
  max-width: 350px;

  .popper__arrow {
    border-bottom-color: rgba(70, 76, 91, .9) !important;
  }

  .popper__arrow::after {
    border-bottom-color: rgba(70, 76, 91, .9) !important;
  }
}
</style>
