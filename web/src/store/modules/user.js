import http from '@/plugins/http';

const state = {
  userInfo: {
    id: '',
    username: '',
    nickname: '',
    firstname: '',
    isAdmin: false,
    avatar: ''
  }
}

const mutations = {
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  },
}
const actions = {
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
      user.firstname = user.nickname.substring(user.nickname.length - 3);
      user.isAdmin = user.id === '1' || (Array.isArray(user.roleIds) && user.roleIds.includes('1'));
      commit('SET_USER_INFO', user)
      return Promise.resolve(user);
    })
  },

  // 退出系统
  LogOut({commit, state}) {
    return http.post('/logout')
  },
}


export default {
  namespaced: true,
  state,
  mutations,
  actions
}
