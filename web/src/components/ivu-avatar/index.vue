<template>
    <span class="ivu-avatar"
          :class="{'ivu-avatar-image':Boolean(src), 'ivu-avatar-square': this.shape === 'square'}"
          :style="styles">
        <img v-if="src" alt="" :src="src" @error="handleError">
        <span v-else ref="children" :style="childrenStyle"><slot></slot></span>
    </span>
</template>
<script>
/**
 * 引入ivu-avatar，因为el-avatar文字填充不能自动缩放
 */
export default {
  name: 'Avatar',
  props: {
    shape: {
      type: String,
      validator (value) {
        return ['circle', 'square'].includes(value);
      },
      default: 'circle'
    },
    size: {
      type: Number,
    },
    src: {
      type: String
    },
  },
  data() {
    return {
      scale: 1,
      childrenWidth: 0,
      isSlotShow: false,
      slotTemp: null
    };
  },
  computed: {
    styles() {
      let style = {};
      if (this.size) {
        style.width = `${this.size}px`;
        style.height = `${this.size}px`;
        style.lineHeight = `${this.size}px`;
        style.fontSize = `${this.size / 2}px`;
      }
      return style;
    },
    childrenStyle() {
      let style = {};
      if (this.isSlotShow) {
        style = {
          msTransform: `scale(${this.scale})`,
          WebkitTransform: `scale(${this.scale})`,
          transform: `scale(${this.scale})`,
          position: 'absolute',
          display: 'inline-block',
          left: `calc(50% - ${Math.round(this.childrenWidth / 2)}px)`
        };
      }
      return style;
    }
  },
  watch: {
    size(val, oldVal) {
      if (val !== oldVal) this.setScale();
    }
  },
  methods: {
    setScale() {
      this.isSlotShow = !this.src;
      if (this.$refs.children) {
        // set children width again to make slot centered
        this.childrenWidth = this.$refs.children.offsetWidth;
        const avatarWidth = this.$el.getBoundingClientRect().width;
        // add 4px gap for each side to get better performance
        if (avatarWidth - 8 < this.childrenWidth) {
          this.scale = (avatarWidth - 8) / this.childrenWidth;
        } else {
          this.scale = 1;
        }
      }
    },
    handleError(e) {
      this.$emit('on-error', e);
    },

  },
  beforeCreate() {
    this.slotTemp = this.$slots.default;
  },
  mounted() {
    this.setScale();
  },
  updated() {
    if (this.$slots.default !== this.slotTemp) {
      this.slotTemp = this.$slots.default;
      this.setScale();
    }
  },

};
</script>
<style scoped>

.ivu-avatar {
  display: inline-block;
  text-align: center;
  background: #ccc;
  color: #fff;
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  vertical-align: middle;
  width: 40px;
  height: 40px;
  line-height: 40px;
  border-radius: 50%
}

.ivu-avatar-image {
  background: 0 0
}

.ivu-avatar-square {
  border-radius: 4px
}

.ivu-avatar > img {
  width: 100%;
  height: 100%
}

</style>
