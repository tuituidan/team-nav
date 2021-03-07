/*
 * @author zhujunhan
 * @date 2020/12/11
 */
new Vue({
    el: '#app',
    data() {
        return {
            isCollapsed: false,
            datas: []
        }
    },
    mounted() {
        this.loadTree();
    },
    computed: {
        rotateIcon() {
            return [
                'menu-icon',
                this.isCollapsed ? 'rotate-icon' : ''
            ];
        },
        menuitemClasses() {
            return [
                'menu-item',
                this.isCollapsed ? 'collapsed-menu' : ''
            ]
        }
    },
    methods: {
        loadTree() {
            this.$http.get('/api/v1/card/tree')
                .then(res => {
                    this.datas = res.data;
                })
                .catch(err => {
                    console.error(err);
                })
        },
        gotoAdmin() {
            location.href = '/admin/category';
        },
        collapsedSider() {
            this.$refs.sidebar.toggleCollapse();
        },
        choosedMenu(id) {
            document.getElementById(id).scrollIntoView({behavior: 'smooth', block: 'start'});
        },
        dropdownClick(id) {
            this.choosedMenu(id);
        },
        cardClickHandler(item) {
            if (item.url) {
                window.open(item.url);
                return;
            }
            this.$notice.info('点了个寂寞');
        }
    }
});
