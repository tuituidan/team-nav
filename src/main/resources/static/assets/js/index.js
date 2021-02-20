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
            activeMenuName: sessionStorage.activeMenuName || 'home',
            openMenuName: sessionStorage.openMenuName ? [sessionStorage.openMenuName] : [],
            menus: [
                {
                    title: '首页',
                    name: 'home',
                    icon: 'ios-home',
                    href: '/'
                },
                {
                    title: '我参与的项目',
                    name: 'projects',
                    icon: 'ios-cube',
                    href: '/projects'
                },
                {
                    title: '我的合并请求',
                    name: 'merge-requests',
                    icon: 'md-git-pull-request',
                    children: []
                },
                {
                    title: '统计信息',
                    name: 'report-manage',
                    icon: 'ios-stats',
                    children: [
                        {
                            title: '统计报表',
                            name: 'about22',
                            href: '/home'
                        }
                    ]
                }
            ],
            datas: [{
                title: '常用导航',
                items: [{
                    title: '开发环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '稳定环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '系统管理',
                    url: 'http://172.25.16.223:8088/test'
                }]
            }, {
                title: '基础服务',
                items: [{
                    title: '研发计划',
                    url: '积分的故事PDFIG水电费IG配送费东莞扣水电费开公司大佛刚开始的放开公司的控股开始都分开公司答复刚开始的'
                }, {
                    title: '环境搭建',
                    url: 'http://172.25.16.223:8088/test/sgas/asgag/agadfg/asdgasd/g'
                }, {
                    title: '重构情况',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '权限配置',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '问题记录',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '对接进度',
                    url: 'http://172.25.16.223:8088/test'
                }]
            }, {
                title: '微服务支持平台',
                items: [{
                    title: 'Eureka',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: 'Gateway',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: 'Master',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: 'Monitor',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '镜像仓库',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '镜像仓库UI',
                    url: 'http://172.25.16.223:8088/test'
                }]
            }, {
                title: '业务支撑服务',
                items: [{
                    title: 'Eureka',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: 'Gateway',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: 'Master',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: 'Monitor',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '镜像仓库',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '镜像仓库UI',
                    url: 'http://172.25.16.223:8088/test'
                }]
            },{
                title: '常用导航1',
                items: [{
                    title: '开发环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '稳定环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '系统管理',
                    url: 'http://172.25.16.223:8088/test'
                }]
            },{
                title: '常用导航2',
                items: [{
                    title: '开发环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '稳定环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '系统管理',
                    url: 'http://172.25.16.223:8088/test'
                }]
            },{
                title: '常用导航3',
                items: [{
                    title: '开发环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '稳定环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '系统管理',
                    url: 'http://172.25.16.223:8088/test'
                }]
            },{
                title: '常用导航5',
                items: [{
                    title: '开发环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '稳定环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '系统管理',
                    url: 'http://172.25.16.223:8088/test'
                }]
            },{
                title: '常用导航6',
                items: [{
                    title: '开发环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '稳定环境',
                    url: 'http://172.25.16.223:8088/test'
                }, {
                    title: '系统管理',
                    url: 'http://172.25.16.223:8088/test'
                }]
            }]
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
            sessionStorage.activeMenuName = name;

            //根据name查找对应的菜单对象
            let menu = null;
            this.menus.forEach(_menu => {
                if (_menu.name === name) {
                    menu = _menu;
                    sessionStorage.openMenuName = null;
                } else if (_menu.children) {
                    _menu.children.forEach(child => {
                        if (child.name === name) {
                            menu = child;
                            sessionStorage.openMenuName = _menu.name;
                        }
                    })
                }
            })
            // location.href = menu.href;
            document.getElementById(name).scrollIntoView({behavior: 'smooth', block:'start'});
        },
        dropdownClick(name) {
            this.choosedMenu(name);
        }
        // ------------------------------  菜单操作结束  --------------------------------
    }
});
