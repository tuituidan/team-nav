import cache from './cache'
import modal from './modal'
import download from './download'
import http from './http'
import utils from './utils'

export default {
  install(Vue) {
    // 缓存对象
    Vue.prototype.$cache = cache
    // 模态框对象
    Vue.prototype.$modal = modal
    // 下载文件
    Vue.prototype.$download = download
    // http请求
    Vue.prototype.$http = http
    // utils
    Vue.prototype.$utils = utils
  }
}
