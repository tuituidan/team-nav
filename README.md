# 团队服务导航

<img src="https://img.shields.io/badge/springboot-2.7.3-brightgreen" alt="springboot"/>   <img src="https://img.shields.io/badge/jdk-1.8-blue" alt="java"/> 

## 简介

团队导航服务

## 功能说明

- 网址导航前端展示+搜索功能
- 网址导航后台基本功能：分类和卡片的增删改查
- 分类和卡片支持拖拽调整顺序
- 暂时不需要的分类支持移除功能，移除后页面不可见，可从历史分类中还原回来
- 卡片图标支持根据标题文字生成、根据链接自动获取favicon、用户手动上传三种方式
- 支持项目原型压缩包上传，并生成访问地址
- 支持添加秘密卡片分类，该分类下的卡片需要登录才能看见
- 原型可通过配置Nginx访问


## 技术栈

#### 前端

- js框架：Vue（前后端不分离，配合后端thymeleaf完成模块代码组合）
- UI框架：ViewUI（iview升级版）

#### 后端

- Springboot
  - SpringDataJpa：持久层框架
  - thymeleaf：页面渲染
  - SpringSecurity：后台登录
- 数据库：H2
- 其他
  - ApacheCommons相关工具包
  - Jsoup：解析目标网址的favicon.ico
  - zip4j：对原型压缩包进行解压

## 部署说明

#### 方式一：手动部署

从[Releases](https://github.com/tuituidan/team-nav/releases)下载team-nav.tar.gz放到服务器上解压，对应修改config/application.yml文件，使用使用team-nav/bin/start.sh 启动项目即可。

#### 方式二：docker部署

docker启动

```
docker run -d -p 8082:8080 \
-v /opt/team-nav/logs:/logs \
-v /opt/team-nav/database:/database \
-v /opt/team-nav/ext-resources:/ext-resources \
-e nav-name="团队导航服务" \
registry.cn-chengdu.aliyuncs.com/tuituidan/team-nav:1.0.0

```

参数说明：

- logs、database、ext-resources分别将日志，数据库文件，额外资源（自定义图标和原型文件等）挂载出来
- nav-name：导航服务的名字，可不传，默认"团队导航服务"

其他参数参考config/application.yml

#### nginx部署（非必须）

如果不希望因为本服务停掉导致原型也无法访问，可以在后台系统设置中设置原型通过nginx访问，nginx配置示例如下：

```
# 将/ext-resources/modules的请求地址拦截并访问到目录/opt/team-nav/team-nav/ext-resources/modules
location /ext-resources/modules {
	alias   /opt/team-nav/team-nav/ext-resources/modules;
	index  index.html index.htm;
}
```



## 版本说明

- 1.0.0

  - springboot从2.2.10.RELEASE升级到2.7.3
  - 默认的h2数据库从v1升级到v2，不兼容v1，建议直接部署team-nav:1.0.0，然后将数据手动迁移到新库

- 0.0.1-SNAPSHOT

  没有正式发布

## license

100%开源，MIT协议，可自由修改

## 页面展示

首页

![](./docs/home.png)

后台-分类管理

![](./docs/admin-category.png)

后台-历史分类

![](./docs/admin-category-history.png)

后台-分类添加

![](./docs/admin-category-add.png)

后台-卡片管理

![](./docs/admin-card.png)

后台-卡片编辑

![](./docs/admin-card-edit.png)

后台-系统设置

![](./docs/admin-system.png)