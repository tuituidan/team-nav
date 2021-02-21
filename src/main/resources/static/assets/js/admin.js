/*
 * @author zhujunhan
 * @date 2020/12/11
 */
new Vue({
    el: '#app',
    data() {
        return {
            isCollapsed: false,
            title: '首页',
            activeMenuName: sessionStorage.activeAdminMenuName || 'category',
            openMenuName: sessionStorage.openAdminMenuName ? [sessionStorage.openAdminMenuName] : [],
            menus: [
                {
                    title: '分类列表',
                    name: 'category',
                    icon: 'ios-apps',
                    href: '/admin/category'
                },
                {
                    title: '卡片列表',
                    name: 'card',
                    icon: 'ios-list',
                    href: '/admin/card'
                },
                {
                    title: '发布设置',
                    name: 'merge-requests',
                    icon: 'ios-time',
                    href: ''
                }
            ]
            // ------------------------------  菜单操作结束  --------------------------------
        }
    },
    mounted() {
        const submenus = this.menus.find(item => item.name === 'merge-requests');
        submenus.children = [];
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
    // ------------------------------  菜单操作结束  --------------------------------
    methods: {
        quit() {
            location.href = '/api/v1/login/out';
        },
        collapsedSider() {
            this.$refs.sidebar.toggleCollapse();
        },
        choosedMenu(name) {
            // 设置选中菜单name
            sessionStorage.activeAdminMenuName = name;

            //根据name查找对应的菜单对象
            let menu = null;
            this.menus.forEach(_menu => {
                if (_menu.name === name) {
                    menu = _menu;
                    sessionStorage.openAdminMenuName = null;
                } else if (_menu.children) {
                    _menu.children.forEach(child => {
                        if (child.name === name) {
                            menu = child;
                            sessionStorage.openAdminMenuName = _menu.name;
                        }
                    })
                }
            })
            location.href = menu.href;
        },
        dropdownClick(name) {
            this.choosedMenu(name);
        }
        // ------------------------------  菜单操作结束  --------------------------------
    }
});
