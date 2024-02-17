const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''
const state = {
  title: process.env.VUE_APP_TITLE,
  sideTheme: storageSetting.sideTheme || 'theme-dark',
  showSettings: false,
  cutOverSpeed: 10,
  logoPath: '/assets/images/logo.png',
  logoToFavicon:false,
}
const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  // 修改布局设置
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  },
  // 设置网页标题
  setSetting({ commit }, setting) {
    state.title = setting.navName;
    state.cutOverSpeed = setting.cutOverSpeed;
    state.logoPath = setting.logoPath;
    state.logoToFavicon = setting.logoToFavicon;
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

