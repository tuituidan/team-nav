/*
 * @author tuituidan
 * @date 2020/12/27
 */
const http = axios.create({
    timeout: 800000
});

http.interceptors.request.use(config => {
    config.headers['X-Requested-With'] = 'XMLHttpRequest';
    return config;
});
http.interceptors.response.use(res => {
    return res;
}, err => {
    if (err.response && err.response.status === 401) {
        location.href = '/login';
        return Promise.reject(null);
    }
    return Promise.reject(err);
});

const CustomPlugin = {};
CustomPlugin.install = Vue => {
    Vue.prototype.$http = http;
    Vue.prototype.$tools = {
        showLoading(msg) {
            Vue.prototype.$Spin.show({
                render: (h) => {
                    return h('div', [
                        h('Icon', {
                            props: {
                                type: 'ios-loading',
                                size: 18
                            }
                        }),
                        h('div', msg)
                    ])
                }
            });
        }
    };
    Vue.prototype.$notice = {
        suc(msg) {
            Vue.prototype.$Notice.success({
                title: '成功',
                duration: 2,
                desc: msg
            });
        },
        err(msg) {
            Vue.prototype.$Notice.error({
                title: '错误',
                duration: 5,
                desc: msg
            });
        },
        warn(msg) {
            Vue.prototype.$Notice.warning({
                title: '警告',
                duration: 3,
                desc: msg
            });
        },
        info(msg) {
            Vue.prototype.$Notice.info({
                title: '提示',
                duration: 2,
                desc: msg
            });
        }
    };
};
Vue.use(CustomPlugin);
