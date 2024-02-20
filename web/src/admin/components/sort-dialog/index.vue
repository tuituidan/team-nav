<template>
  <el-dialog title="拖动排序" :visible.sync="show"
             :close-on-click-modal="false"
             width="450px" append-to-body>
    <el-tree :data="dataList"
             draggable
             :props="defaultProps"
             :allow-drop="allowDrop"
             @node-drop="nodeDrop"
             node-key="id">
      <span style="display: flex;justify-content: space-between" slot-scope="{ node, data }">
        <span v-html="node.label"></span>
      </span>
    </el-tree>
  </el-dialog>
</template>

<script>
export default {
  name: "sort-dialog-index",
  data() {
    return {
      show: false,
      dataList: [],
      defaultProps: {
        label: 'name'
      }
    }
  },
  methods: {
    open(datas, props) {
      // 拷贝一份
      this.dataList = JSON.parse(JSON.stringify(datas));
      if (props) {
        this.defaultProps = props;
      }
      this.show = true;
    },
    allowDrop(draggingNode, dropNode, type) {
      // 仅允许同级排序
      return 'inner' !== type && draggingNode.data.level === dropNode.data.level
    },
    nodeDrop(draggingNode, dropNode, type) {
      this.$emit('change-sort', draggingNode.data, dropNode.data, type)
    },
  }
}
</script>

<style scoped lang="scss">

</style>
