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
http.save = (url, data) => data.id ? http.patch(`${url}/${data.id}`, data) : http.post(url, data);

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
        },
        /**
         * 格式日期.
         *
         * @param {date} date 是否输出日志
         * @param {string} fmt  模块名称
         * @returns {string} 格式化后的字符串
         */
        formatDate(date, fmt) {
            // 如果date为null、‘’或undefined直接返回为空
            if (!date) {
                return '';
            }
            let newDate = date;
            let newFmt = fmt || 'yyyy-MM-dd';
            if (!(newDate instanceof Date)) {
                newDate = new Date(newDate);
            }
            const o = {
                // 月份
                'M+': newDate.getMonth() + 1,
                // 日
                'd+': newDate.getDate(),
                // 小时
                'H+': newDate.getHours(),
                // 分
                'm+': newDate.getMinutes(),
                // 秒
                's+': newDate.getSeconds(),
                // 季度
                'q+': Math.floor((newDate.getMonth() + 3) / 3),
                // 毫秒
                'S': newDate.getMilliseconds()
            };
            for (const time in o) {
                if (isNaN(o[time])) {
                    return '';
                }
            }
            if (/(y+)/.test(newFmt)) {
                newFmt = newFmt.replace(RegExp.$1, (String(newDate.getFullYear())).substr(4 - RegExp.$1.length));
            }
            for (const k in o) {
                if (new RegExp(`(${k})`).test(newFmt)) {
                    newFmt = newFmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : ((`00${o[k]}`).substr((`${o[k]}`).length)));
                }
            }
            return newFmt;
        },
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
Vue.directive('clickoutside', {
    bind(el, binding) {
        function documentHandler(e) {
            if (el.contains(e.target)) {
                return false;
            }
            if (binding.expression) {
                binding.value(e);
            }
        }
        el.__vueClickOutside__ = documentHandler;
        document.addEventListener('click', documentHandler);
    },
    unbind(el) {
        document.removeEventListener('click', el.__vueClickOutside__);
        delete el.__vueClickOutside__;
    }
});
