<!--该组件用于构建卡片的简介页面-->
<template>
  <div class="app-container">
    <el-row>
      <el-col :span="18" :xs="24">
        <el-row>
          <el-col :span="5" class="card-icon-col">
            <ivu-avatar v-if="cardsInfos.icon.src"
                        :shape="cardIconShape"
                        :src="cardsInfos.icon.src"
                        class="card-icon"
            ></ivu-avatar>
            <ivu-avatar v-else
                        :shape="cardIconShape"
                        :style="{background: cardsInfos.icon.color}"
                        class="card-icon"
            >{{ cardsInfos.icon.text }}
            </ivu-avatar>
          </el-col>
          <el-col :span="19">
            <h1>{{ cardsInfos.title }}</h1>
            <div v-show="cardsInfos.url!=undefined">
              <el-link icon="el-icon-link" type="primary" @click="linkClickHandler(cardsInfos.url)">直达链接</el-link>
            </div>
          </el-col>
        </el-row>
        <el-divider></el-divider>
        <el-row class="card-introduction">
          <h2>简介</h2>
          <div>{{ cardsInfos.content }}</div>
          <div>轮播图</div>
        </el-row>
        <el-divider></el-divider>
        <el-row>
          <h2>下载</h2>
        </el-row>
        <el-divider></el-divider>
        <el-row>
          <h2>相关推荐</h2>
        </el-row>
      </el-col>
      <el-col :span="6" :xs="0">
        <ourself-information></ourself-information>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import OurselfInformation from '@/home/components/ourself-information/index.vue'

export default {
  name: 'card-introduction',
  components: { OurselfInformation },
  mounted() {
    this.getCardAllInfo()
  },
  computed: {
    ...mapGetters([
      'cardIconShape',
      'cardSize'
    ])

  },
  data() {
    let routerParamsList = JSON.parse(this.$route.params.list)
    return {
      // 遮罩层
      loading: true,
      // 卡片详细信息
      cardsInfos: {
        title: routerParamsList.title,
        content: routerParamsList.content,
        url: routerParamsList.url,
        icon: {
          src: routerParamsList.src !== '' ? routerParamsList.icon.src : '',
          text: routerParamsList.icon.text,
          color: routerParamsList.icon.color
        }
      }
    }
  },
  methods: {
    linkClickHandler(url) {
      if (url) {
        window.open(url)
      }
    },
    getCardAllInfo() {
      // let routerParamsList = JSON.parse(this.$route.params.list)
      // this.cardsInfos.title = routerParamsList.cardsInfos.title;
      this.loading = true
      console.log(typeof this.$route.params.list, 'list')
      //let routerParamsList =JSON.parse(this.$route.params.list);
      //console.log(typeof routerParamsList);
      console.log(this.cardsInfos.content, '输出')
      this.loading = false
      //console.log(routerParamsList.content,"输出");
    }
  }
}
</script>


<style lang="scss" scoped>
.card-icon-col {
  height: 100%;
}

.card-icon {
  vertical-align: middle;
  width: 100%;
  height: 100%
}


</style>
