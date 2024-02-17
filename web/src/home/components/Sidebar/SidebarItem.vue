<template>
  <div>
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)">
      <el-menu-item @click="menuItemClickHandler(item.id)" :index="item.id"
                    :class="{'submenu-title-noDropdown':!isNest}">
        <item :icon="onlyOneChild.icon|| item.icon" :title="onlyOneChild.name"/>
      </el-menu-item>
    </template>

    <el-submenu v-else ref="subMenu" :index="item.id" popper-append-to-body>
      <template slot="title">
        <item :icon="item.icon" :title="item.name"/>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.id"
        :is-nest="true"
        :item="child"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import Item from './Item'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'SidebarItem',
  components: {Item},
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    this.onlyOneChild = null
    return {}
  },
  methods: {
    menuItemClickHandler(id) {
      const rows = document.getElementById(id);
      rows.scrollIntoView(
        {behavior: 'smooth', block: 'start'});
      const classList = rows.nextSibling.firstChild.classList;
      classList.add('blink-box');
      setTimeout(() => {
        classList.remove('blink-box');
      }, 3200);
    },
    hasOneShowingChild(children = [], parent) {
      if (!children) {
        children = [];
      }
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = {...parent, path: '', noShowingChildren: true}
        return true
      }

      return false
    },
  }
}
</script>
