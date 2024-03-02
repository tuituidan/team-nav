/*
 * @author tuituidan
 * @date 2024/2/18
 */
export const btnSingle = {
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
}
export const btnMultiple = {
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
}
