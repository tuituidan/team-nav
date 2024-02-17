const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  dict: state => state.dict.dict,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  sidebarRouters:state => state.permission.sidebarRouters,
}
export default getters
