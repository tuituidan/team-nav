import http from '@/plugins/http';
import {saveStarCard} from '@/utils';

const state = {
  userInfo: {
    id: '',
    username: '',
    nickname: '',
    firstname: '',
    isAdmin: false,
    avatar: ''
  },
  applyCount: 0
}

const mutations = {
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  },
  SET_APPLY_COUNT: (state, count) => {
    state.applyCount = count
  },
}
const actions = {
  // 更新申请数
  refreshApplyCount({commit}) {
    return http.get('/api/v1/apply/cards/count')
      .then(res => {
        commit('SET_APPLY_COUNT', res);
      })
  },
  // 登录
  Login({commit}, userInfo) {
    const username = userInfo.username.trim()
    const password = userInfo.password
    return http.post('/api/v1/quick/login', {username, password});
  },
  // 获取用户信息
  GetUserInfo({commit, state}) {
    return http.get('/api/v1/login/user').then(res => {
      let user = res;
      if (!user) {
        user = {
          id: '',
          username: '',
          nickname: '未登录',
          isAdmin: false,
          avatar: '',
        }
      }
      // 只初始拉取一次，以后都以本地为准
      if(!localStorage.hasOwnProperty('starCardIds')){
        saveStarCard(user.starCardIds);
      }
      user.firstname = user.nickname.substring(user.nickname.length - 3);
      user.isAdmin = user.id === '1' || (Array.isArray(user.roleIds) && user.roleIds.includes('1'));
      commit('SET_USER_INFO', user)
      return Promise.resolve(user);
    })
  },

  // 退出系统
  LogOut({commit, state}) {
    // 清空 localStorage 和 sessionStorage
    localStorage.clear();
    sessionStorage.clear();

    // // 强制刷新页面
    // // 跳转到主页并强制刷新
    window.location.href = '/';
    window.location.reload(true);
    
    return http.post('/logout').then(() => {
      window.localStorage.userLogout = true;

    })
  },
}


export default {
  namespaced: true,
  state,
  mutations,
  actions
}
