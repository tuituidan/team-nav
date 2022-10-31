/*
 * @author tuituidan
 * @date 2020/12/11
 */
new Vue({
    el: '#app',
    data() {
        return {
            keywords: '',
            isCollapsed: localStorage.IndexCollapsed === 'true',
            datas: [],
            users: []
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
            this.$http.get(`/api/v1/card/tree?keywords=${encodeURIComponent(this.keywords)}`)
                .then(res => {
                    this.datas = res.data;
                })
                .catch(err => {
                    console.error(err);
                    this.$notice.err(err.response.data);
                })
        },
        gotoAdmin() {
            location.href = '/admin/category';
        },
        collapsedSider() {
            this.$refs.sidebar.toggleCollapse();
            localStorage.IndexCollapsed = this.isCollapsed;
        },
        choosedMenu(id) {
            const rows = document.getElementById(id);
            rows.scrollIntoView(
                {behavior: 'smooth', block: 'start'});
            setTimeout(()=>{
                if (rows.getBoundingClientRect().top > 100) {
                    rows.classList.add('blink-box');
                    setTimeout(() => {
                        rows.classList.remove('blink-box');
                    }, 3200);
                }
            }, 500);

        },
        dropdownClick(id) {
            this.choosedMenu(id);
        },
        cardClickHandler(item) {
            if (item.url) {
                window.open(item.url);
            }
        }
    }
});
