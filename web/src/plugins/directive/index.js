/*
 * @author tuituidan
 * @date 2024/3/3
 */
import {btnMultiple, btnSingle} from './table-btn-control'
import inputTrim from './el-input-trim'

export default {
  install(Vue) {
    Vue.directive('trim', inputTrim);
    Vue.directive('btn-single', btnSingle);
    Vue.directive('btn-multiple', btnMultiple);
  }
}
