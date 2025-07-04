/*
 * @author tuituidan
 * @date 2025/7/3
 */

const utils = {
  transDict(dicts, key, [keyName, valueName]) {
    for (const dict of dicts) {
      if (dict[keyName] === key) {
        return dict[valueName];
      }
    }
  },
}
export default utils
