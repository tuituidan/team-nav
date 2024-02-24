<template>
  <div>
    <el-carousel v-if="dataList.length"
                 trigger="click" height="60px"
                 indicator-position="none"
                 :interval="cutOverSpeed * 1000">
      <el-carousel-item v-for="item in dataList" :key="item.id">
        <div style="display: flex; align-items: center;justify-content: center">
          <div style="height: 60px;line-height: 60px;display: flex;align-items: center" v-html="item.content"></div>
          <el-statistic
            v-if="item.endTime"
            format="DD天HH小时mm分钟ss秒"
            :value="new Date(item.endTime)"
            time-indices>
          </el-statistic>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "carousel-flip-notice",
  computed: {
    ...mapGetters([
      'cutOverSpeed',
    ]),
  },
  data() {
    return {
      dataList: []
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$http.get('/api/v1/notice', {params: {status: true}}).then(res => {
        this.dataList = res;
      })
    }
  }
}
</script>

<style scoped lang="scss">
.el-statistic {
  width: auto;
  height: 36px;
  display: flex;
  ::v-deep .con .number {
    font-size: 26px;
    color: red;
  }
}
</style>
