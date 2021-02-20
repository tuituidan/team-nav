/*
 * @author tuituidan
 * @date 2020/12/27
 */
const http = axios.create({
    timeout: 800000
});
http.globalBefore = function () {
    Vue.prototype.$Spin.show();
};
// 结束
http.globalAfter = function () {
    Vue.prototype.$Spin.hide();
};

Vue.prototype.$http = http;


Vue.prototype.$notice = {
    suc(msg){
        Vue.prototype.$Notice.success({
            title: '成功',
            duration: 2,
            desc: msg
        });
    },
    err(msg){
        Vue.prototype.$Notice.error({
            title: '错误',
            duration: 3,
            desc: msg
        });
    },
    warn(msg){
        Vue.prototype.$Notice.warning({
            title: '警告',
            duration: 2,
            desc: msg
        });
    },
    info(msg){
        Vue.prototype.$Notice.info({
            title: '提示',
            duration: 2,
            desc: msg
        });
    }
}
