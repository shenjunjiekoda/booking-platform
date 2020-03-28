# booking-platform

这是一个为教师与教材部设计的方便教材申购领取平台，

平台分为两个角色，教师和教材部，教师负责申请订书，教材部在接收订书请求后向出版社订书，获得结果后在平台录入实际到货数量。

如需开启邮件功能，请修改后台resource/application.yml文件中的spring.mail下属性

前台使用d2-admin-start-kit框架搭建，后台使用SpringBoot搭建。

前台集成:

## 预览图



## 快速开始
**mysql**
运行sql目录下的ddl

**redis**
本地运行redis

**前台:**
进入目录:booking-platform\booking-platform-web

```
npm install
```

```
npm run serve
```

##后台:##
视情况修改配置文件

import gradle changes
或执行
```
gradle build
```
运行src/main/java/cn/shenjunjie/booking/BookingPlatformApplication.java的main方法




