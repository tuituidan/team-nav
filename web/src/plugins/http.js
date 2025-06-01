import axios from 'axios'
import {Message} from 'element-ui'
import QS from 'qs';
import Vue from 'vue';
import login, {isRelogin} from '@/components/login-dialog'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 60000
})

// request拦截器
service.interceptors.request.use(config => {
  config.headers['X-Requested-With'] = 'XMLHttpRequest';
  config.paramsSerializer = param => QS.stringify(param, {indices: false});
  return config;
}, error => {
  console.log(error)
  Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
    if (res.request.responseType === 'blob') {
      return res;
    }
    return res.data;
  },
  err => {
    let {message} = err;
    if (message === "Network Error") {
      Message.error('后端接口连接异常');
      return Promise.reject(err)
    }
    if (message.includes("timeout")) {
      Message.error('系统接口请求超时');
      return Promise.reject(err)
    }
    if (err.response && err.response.status === 401) {
      if (!isRelogin.show) {
        isRelogin.show = true;
        const constructor = Vue.extend(login);
        const instance = new constructor();
        instance.$mount();
      }
      return Promise.reject(err)
    }
    if (err.response && err.response.status === 403) {
      location.href = '/';
      return Promise.reject(err)
    }
    Message.error(err.response.data);
    return Promise.reject(err)
  }
)
service.save = (url, data) => data.id ? service.patch(`${url}/${data.id}`, data) : service.post(url, data);

export default service
