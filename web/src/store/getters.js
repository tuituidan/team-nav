const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  loginUser: state => state.user.userInfo,
  cutOverSpeed: state => state.settings.cutOverSpeed,
  version: state => state.settings.version,
  homeMenus: state => state.home.menus,
  homeCards: state => state.home.datas,
}
export default getters
