/*
 * @author tuituidan
 * @date 2020/12/11
 */
new Vue({
    el: '#app',
    data() {
        return {
            isCollapsed: localStorage.AdminCollapsed === 'true',
            title: '首页',
            menus: [
                {
                    title: '分类管理',
                    name: 'category',
                    icon: 'ios-apps',
                    href: '/admin/category'
                },
                {
                    title: '卡片列表',
                    name: 'card',
                    icon: 'ios-list',
                    href: '/admin/card'
                }, {
                    title: '系统设置',
                    name: 'settings',
                    icon: 'md-settings',
                    href: '/admin/settings'
                }
            ]
        }
    },
    created() {
        if(countdown){
            this.menus.splice(2, 0, {
                title: '发布倒计时',
                name: 'countdown',
                icon: 'md-alarm',
                href: '/admin/countdown'
            });
        }
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
        },
        activeMenuName() {
            const urlItem = this.menus.find(item => item.href === location.pathname);
            if (urlItem) {
                return urlItem.name;
            }
            return "category";
        }
    },
    methods: {
        collapsedSider() {
            this.$refs.sidebar.toggleCollapse();
            localStorage.AdminCollapsed = this.isCollapsed;
        },
        choosedMenu(name) {
            //根据name查找对应的菜单对象
            let menu = null;
            this.menus.forEach(_menu => {
                if (_menu.name === name) {
                    menu = _menu;
                } else if (_menu.children) {
                    _menu.children.forEach(child => {
                        if (child.name === name) {
                            menu = child;
                        }
                    })
                }
            });
            location.href = menu.href;
        },
        dropdownClick(name) {
            this.choosedMenu(name);
        },
        logout() {
            delete localStorage.autoLogin;
            location.href = '/logout';
        },
    }
});
