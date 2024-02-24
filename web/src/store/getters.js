const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  loginUser: state => state.user.userInfo,
  cutOverSpeed: state => state.settings.cutOverSpeed,
}
export default getters
