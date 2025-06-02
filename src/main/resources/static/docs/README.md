# 网站导航服务

<img src="https://img.shields.io/badge/springboot-2.7.3-brightgreen" alt="springboot"/>   <img src="https://img.shields.io/badge/jdk-1.8-blue" alt="java"/>   <img src="https://img.shields.io/badge/vue-2.6.12-blueviolet" alt="vue"/>   <img src="https://img.shields.io/badge/elementui-2.15.14-brown" alt="element-ui"/>   <img src="https://img.shields.io/badge/h2database-2.1.214-yellow" alt="java"/> 

## 简介

前后端完整的网址导航服务，相较一般的靠配置生成的网址导航服务，本系统具有更强的后台管理能力，技术上主打一个轻量级，无任何依赖服务。

**Github:** https://github.com/tuituidan/team-nav

**Gitee:** https://gitee.com/tuituidan/team-nav


## 功能说明

- 首页
  - 最高支持三级分类的显示，可搜索，点击左侧菜单支持高亮定位右侧卡片。
  - 支持设置自定义主题。
  - 附件的在线查看和下载。
  - 头部可显示通知公告，支持倒计时模式。
  - 设置常用卡片分类，收藏个人常用网址。
  - 申请提交卡片。
- 后台管理
  - 仅支持管理员角色进入后台管理（管理员角色可设置多人），其他人只能通过首页的卡片申请进行卡片添加，由管理员审核后生效。
- 后台管理-分类管理
  - 三级分类树的基本增删改查及排序。
  - 对需要归档的分类支持移除到历史分类中，移除后不再显示到首页，可随时从历史分类中还原回来。
  - 可为卡片分类分配角色，未分配角色为公共分类，对所有人公开，不管登录与否，设置角色后必须登录且有对应角色的人才能看到（人员角色设置下面在角色和用户管理模块中）。
- 后台管理-卡片管理
  - 卡片基本的增删改查及排序。
  - 管理员进行卡片审核。
  - 三种卡片类型：
    - 普通卡片：一般的内容型卡片，承载少量信息，可添加链接进行点击跳转。
    - 静态网站：上传发布成静态网站的压缩包（根目录带index.html）。
    - 动态卡片：内容将根据配置的http请求或sql动态更新（开发中）。
  - 支持私密信息添加，仅登录后人员才能查看。
  - 卡片图标支持上传，选择自定义图片，自动获取链接`favicon.ico`、文字图标四种方式。
  - 支持附件上传。
- 后台管理-角色管理
  - 角色基本增删改查，管理员角色不支持修改删除。
  - 可以给角色分配卡片分类，不支持给管理员分配卡片分类，因为他本来就能看到所有。
  - 可以给角色分配人员。
- 后台管理-用户管理
  - 用户基本增删改查及重置密码
- 后台管理-通知公告
  - 通知公告的基本增删改查及排序。
  - 通知内容支持富文本编辑，如果选择截止时间，会自动出现倒计时，一般用于项目发布倒计时，同时有多个通知公告时，会定时切换，切换时间在系统设置中设置。
- 后台管理-系统设置
  - 基本设置，包括设置当前网站服务名、LOGO、公告切换时间等设置。
  - 卡片自定义图标上传。
  - 邮箱设置，用于发送随机密码给新用户
  - 数据备份还原，备份是将所有资源文件及数据库数据导出为一个zip包，还原则是将zip包重新上传拆解为相应数据。
  - 浏览器书签导入：在浏览器书签管理中导出书签，将得到的书签html文件上传，在预览窗口根据规则进行调整后即可导入成功。


## 在线体验

演示地址：

文档地址：

## 技术栈

#### 前端主要技术

vue2+ElementUI，前后端分离开发，合并部署（1.x版本UI框架是ViewUI，升级有分类图标不兼容，需要重新设置一下）。

#### 后端主要技术

Springboot、SpringDataJpa、SpringSecurity、h2database

## 部署说明

通过常规maven命令构建，打包执行命令`mvn clean package -Dmaven.npm.skip=false`，会自动将前端构建到后端的jar包中，这样整个服务就一个jar包（当然如果想前后端分离部署也是可以的，这里不再赘述），再无其他依赖服务，仅需要jdk环境即可启动。

> 如果已经部署了1.X版本的也是直接用2.0版本的jar启动，或者重新拉取镜像启动即可，数据会自动矫正

#### 方式一：手动部署

从[Releases](https://github.com/tuituidan/team-nav/releases)下载team-nav.tar.gz放到服务器上解压，对应修改config/application.yml文件，使用team-nav/bin/start.sh 启动项目即可。

#### 方式二：docker部署

docker启动

```
docker run -d -p 8082:8080 \
-v /opt/team-nav/logs:/logs \
-v /opt/team-nav/database:/database \
-v /opt/team-nav/ext-resources:/ext-resources \
-e nav-name="团队导航服务" \
registry.cn-chengdu.aliyuncs.com/tuituidan/team-nav:2.0.5

```

参数说明：

- logs、database、ext-resources分别将日志，数据库文件，额外资源（自定义图标、网址文件、附件等）挂载出来。
- nav-name：导航服务的名字，可不传，默认"团队导航服务"。

其他参数参考config/application.yml，可以将此文件挂载出来修改配置，也可通过`docker`的`-e`参数替换`application.yml`中的配置。

#### nginx部署（非必须）

如果不希望因为本服务停掉导致上传的静态网站无法访问，可以在后台系统设置中设置静态网站通过nginx访问，nginx配置示例如下：

```nginx
# 将/ext-resources/modules的请求地址拦截并访问到目录/opt/team-nav/team-nav/ext-resources/modules
location /ext-resources/modules {
	alias   /opt/team-nav/ext-resources/modules;
	index  index.html index.htm;
}
```

#### 数据库支持

默认使用`H2`数据库，在V2.0.4开始支持`mysql`和`postgresql`，相应配置分别参考源码中`application-h2.yml`、`application-mysql.yml`、`application-postgresql.yml`三个配置文件。

```yaml
# application-mysql.yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/team_nav?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: 123456
  jpa:
    database: mysql
```

```yaml
# application-postgresql.yml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://127.0.0.1:5432/team_nav
    username: root
    password: 123456
  jpa:
    database: postgresql
```

手动部署方式，修改对应配置文件后通过`spring.profiles.active=mysql/postgresql`即可启用对应配置，`Docker`部署也一样，通过`-e`参数即可替换相应配置。

## license

100%开源，MIT协议，可自由修改

## 页面展示

#### 首页

![首页](https://foruda.gitee.com/images/1748847830957034703/2aab0c00_1328008.png)

#### 首页-主题切换

![主题切换](https://foruda.gitee.com/images/1748847899719581246/b83cde3f_1328008.png)

#### 首页-布局设置

![布局设置](https://foruda.gitee.com/images/1748847923731713322/40d65b2e_1328008.png)

#### 首页-未登录

![首页-未登录](https://foruda.gitee.com/images/1748847954167416693/9e6c57b3_1328008.png)

#### 后台管理-分类管理

![后台管理-分类管理](https://foruda.gitee.com/images/1748847976883050517/1cc3ed5d_1328008.png)

#### 后台管理-卡片管理

![后台管理-卡片管理](https://foruda.gitee.com/images/1748848000531793018/405cc875_1328008.png)

#### 后台管理-卡片添加

![后台管理-卡片添加](https://foruda.gitee.com/images/1748848025599846369/b5457622_1328008.png)

#### 后台管理-角色管理

![后台管理-角色管理](https://foruda.gitee.com/images/1748848051496772289/5a6a4a1f_1328008.png)

#### 后台管理-用户管理

![后台管理-用户管理-用户添加](https://foruda.gitee.com/images/1748848077975467078/24f472da_1328008.png)

![后台管理-用户管理](https://foruda.gitee.com/images/1748848104553441622/7355ee09_1328008.png)

#### 后台管理-通知公告

![后台管理-通知公告](https://foruda.gitee.com/images/1748848133265740572/9f0aa9f5_1328008.png)

#### 后台管理-系统设置

![后台管理-系统设置](https://foruda.gitee.com/images/1748848310938163504/cbfbde91_1328008.png)

![后台管理-系统设置-书签导入](https://foruda.gitee.com/images/1748848489807219414/cc8dbbea_1328008.png)


## 结语

作为一个java后端，并不怎么擅长写前端样式，页面可能就稍微丑丑的，欢迎有兴趣的大佬帮忙优化。
