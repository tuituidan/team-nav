/*
 * @author tuituidan
 * @date 2024/2/18
 */
import Vue from 'vue';

Vue.directive('btn-single', {
  componentUpdated(el, binding) {
    const datas = binding.value;
    if (Array.isArray(datas) && datas.length === 1) {
      el.classList.remove('is-disabled');
      el.removeAttribute('disabled');
      return;
    }
    el.setAttribute('disabled', 'disabled');
    el.classList.add('is-disabled');
  }
});
Vue.directive('btn-multiple', {
  componentUpdated(el, binding) {
    const datas = binding.value;
    if (Array.isArray(datas) && datas.length > 0) {
      el.classList.remove('is-disabled');
      el.removeAttribute('disabled');
      return;
    }
    el.setAttribute('disabled', 'disabled');
    el.classList.add('is-disabled');
  }
});
