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
    if (localStorage.username
      && localStorage.password
      && localStorage.autoLogin === 'true'
      && !localStorage.userLogout
      && !this.$store.getters.loginUser.id) {
      this.$store.dispatch('settings/loadVersion');
    }
    this.$store.dispatch('home/loadHomeCards', '');
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', {withoutAnimation: false})
    }
  }
}
