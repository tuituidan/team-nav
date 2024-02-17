/*
 * @author tuituidan
 * @date 2023/9/24
 */
import ResizeMixin from './mixin/ResizeHandler'
import {mapState} from 'vuex'
import variables from '@/assets/styles/variables.scss'

export default {
  name: 'home-index',
  components: {
    'app-main': () => import('@/home/components/AppMain'),
    'navbar': () => import('@/home/components/Navbar'),
    'right-panel': () => import('@/components/RightPanel'),
    'settings': () => import('@/home/components/Settings'),
    'sidebar': () => import('@/home/components/Sidebar'),
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      sideTheme: state => state.settings.sideTheme,
      sidebar: state => state.app.sidebar,
      device: state => state.app.device,
    }),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    },
    variables() {
      return variables;
    }
  },
  mounted() {
    this.loadDatas('');
  },
  data() {
    return {
      menus: [],
      datas: [],
    }
  },
  methods: {
    loadDatas(keywords) {
      this.$http.get('/api/v1/card/tree?keywords=' + keywords)
        .then(res => {
          this.menus = res.menus;
          this.datas = res.datas;
        })
    },
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', {withoutAnimation: false})
    }
  }
}
