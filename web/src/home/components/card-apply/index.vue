<template>
  <div>
    <span @click="openDialog()"><slot></slot></span>
    <card-dialog ref="cardDialog"></card-dialog>
  </div>
</template>

<script>
export default {
  name: "card-apply",
  components: {
    'card-dialog': () => import("@/admin/card/dialog/index.vue"),
  },
  methods: {
    openDialog(){
      this.$refs.cardDialog.open();
    },
  }
}
</script>

<style scoped>

</style>
