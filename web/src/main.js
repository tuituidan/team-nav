import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import plugins from './plugins' // plugins
import directive from '@/plugins/directive'
import './assets/icons' // icon
import '@/components'
// 头部标签组件
import VueMeta from 'vue-meta'

Vue.use(plugins)
Vue.use(VueMeta)
Vue.use(directive)
Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
