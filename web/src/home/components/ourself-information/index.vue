<template>
  <el-card class="cardWithBg" >
      <img ref="cardWithBg" class="bg"/>
    <div class="title">{{title}} </div>
  </el-card>
</template>
<script>
export default {
  name: 'ourself-information',
  mounted(){
    this.setLogoPathAsBg();
  },
  methods:{
    setLogoPathAsBg(){
      console.log(this.$store.state.settings.logoPath);
      this.$refs.cardWithBg.src = this.$store.state.settings.logoPath
    }
  },
  data(){
    return {
      title:this.$store.state.settings.title,
    }
  }
}
</script>

<style lang="scss" scoped>
.cardWithBg{
  background: inherit;
}
.bg{
  width: 100%;
  display: block;
}
.title{
  text-align: center;
}
</style>
