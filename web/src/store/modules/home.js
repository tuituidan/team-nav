import http from '@/plugins/http';
import {buildStarCard, saveStarCard} from "@/utils";

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
  LOAD_STAR_CARD: state => {
    const starCategory = buildStarCard(state.datas);
    if (state.datas.find(item => item.id === '1')) {
      state.menus.splice(0, 1);
      state.datas.splice(0, 1);
    }
    if (starCategory) {
      state.menus.splice(0, 0, starCategory);
      state.datas.splice(0, 0, starCategory);
    }
  }
}

const actions = {
  setStarCard({commit}, card) {
    const star = card.star;
    if (star) {
      if (localStorage.starCardIds) {
        const arr = localStorage.starCardIds.split(',');
        arr.splice(arr.indexOf(card.id), 1);
        localStorage.starCardIds = arr.join(',');
      }
    } else {
      saveStarCard([card.id]);
    }
    commit('LOAD_STAR_CARD');
    const params = localStorage.starCardIds ? localStorage.starCardIds.split(',') : [];
    http.patch('/api/v1/user/star/card', params);
    return Promise.resolve(!star);
  },
  // 加载
  loadHomeCards({commit}, keywords) {
    return http.get(`/api/v1/card/tree?keywords=${keywords}`).then(res => {
      commit('CHANGE_DATA', res);
      commit('LOAD_STAR_CARD');
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

