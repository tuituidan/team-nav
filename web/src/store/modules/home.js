import http from '@/plugins/http';

const state = {
  menus: [],
  datas: [],
  openIds: [],
}
const mutations = {
  CHANGE_DATA: (state, res) => {
    state.menus = res.menus;
    state.datas = res.datas;
    state.openIds = res.menus.filter(it => Array.isArray(it.children) && it.children.length > 0).map(it => it.id);
  },
}

const actions = {
  // 加载
  loadHomeCards({commit}, keywords) {
    return http.get(`/api/v1/card/tree?keywords=${keywords}`).then(res => {
      commit('CHANGE_DATA', res);
      return res;
    });
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

