/*
 * @author tuituidan
 * @date 2024/2/18
 */
// 获取元素
function getInput(el) {
  let inputEle;
  if (el.tagName !== "INPUT") {
    // 若 el-input 中 type 为 textarea
    if (el._prevClass.includes("el-textarea")) {
      inputEle = el.querySelector("textarea");
    } else {
      inputEle = el.querySelector("input");
    }
  } else {
    inputEle = el;
  }
  return inputEle;
}

export default {
  bind: (el) => {
    let inputEle = getInput(el);
    const handler = event =>  {
      const newVal = event.target.value.trim();
      if (event.target.value !== newVal) {
        event.target.value = newVal;
        inputEle.dispatchEvent(new Event('input'));
      }
    };
    el.inputEle = inputEle;
    el._blurHandler = handler;
    // 事件监听
    inputEle.addEventListener("blur", handler);
  },
  unbind(el) {
    const {inputEle} = el;
    inputEle.removeEventListener("blur", el._blurHandler);
  },
}
