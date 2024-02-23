<template>
  <div>
    <ivu-avatar v-if="loginUser.avatar"
                :src="loginUser.avatar"
                :title="loginUser.nickname"></ivu-avatar>
    <ivu-avatar v-else
                :title="loginUser.nickname"
                :style="{backgroundColor: avatarColor}">{{ loginUser.firstname }}
    </ivu-avatar>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "header-avatar",
  computed: {
    ...mapGetters([
      'loginUser'
    ]),
    avatarColor() {
      if(this.loginUser.id){
        return '#409eff';
      }
      return '#ccc'
    }
  },
}
</script>

<style scoped>

</style>
