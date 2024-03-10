const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  loginUser: state => state.user.userInfo,
  cutOverSpeed: state => state.settings.cutOverSpeed,
  showDoc: state => state.settings.showDoc,
  version: state => state.settings.version,
  homeMenus: state => state.home.menus,
  homeCards: state => state.home.datas,
  cardIconShape: state => state.settings.cardIconShape,
}
export default getters
