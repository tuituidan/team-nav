import http from '@/plugins/http';

const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''
const state = {
  title: process.env.VUE_APP_TITLE,
  sideTheme: storageSetting.sideTheme || 'theme-dark',
  showSettings: false,
  cutOverSpeed: 10,
  logoPath: '/assets/images/logo.png',
  logoToFavicon: false,
  showDoc: false,
  menuDefaultOpen: storageSetting.menuDefaultOpen === 'true',
  menuAccordion: storageSetting.menuAccordion === 'true',
  cardIconShape: storageSetting.cardIconShape || 'circle',
  version: {},
}
const mutations = {
  CHANGE_SETTING: (state, {key, value}) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  CHANGE_VERSION: (state, version) => {
    state.version = version;
  }
}

const actions = {
  // 修改布局设置
  changeSetting({commit}, data) {
    commit('CHANGE_SETTING', data)
  },
  // 加载配置
  loadSettings({commit}) {
    return http.get('/api/v1/setting').then(res => {
      commit('CHANGE_SETTING', {key: 'title', value: res.navName});
      commit('CHANGE_SETTING', {key: 'cutOverSpeed', value: res.cutOverSpeed});
      commit('CHANGE_SETTING', {key: 'logoPath', value: res.logoPath});
      commit('CHANGE_SETTING', {key: 'logoToFavicon', value: res.logoToFavicon});
      commit('CHANGE_SETTING', {key: 'showDoc', value: res.showDoc});
      return res;
    });
  },
  // 加载版本信息
  loadVersion({commit}) {
    return http.get('/api/v1/app/version').then(res => {
      commit('CHANGE_VERSION', res);
      return res;
    });
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

