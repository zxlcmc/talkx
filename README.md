# TalkX

TalkX 是一个基于大模型实现的聊天平台，界面适配移动端，并且衍生出了 [编程插件](https://github.com/big-mouth-cn/talkx-plugin-for-jetbrains)、[小智ESP智能机器人](https://github.com/big-mouth-cn/xiaozhi-esp32-for-talkx)等能力。

TalkX 在生产环境上稳定运行时间超过两年，当搭建中间件配置后，即可支持集群生产部署。

## 什么场景下需要部署TalkX？
- 私有化部署自己的“Chat”聊天应用。
- 私有化部署小智ESP32服务端，自定义大模型、记忆及声音复刻等。（🔥仅需创建数据库即可快速体验）
- 研究与学习大模型的应用。
- 在此基础上进行二次开发。

![talkx.png](other%2Ftalkx.png)

## 快速部署

### 1、安装Java运行环境：1.8+
打开官网：https://www.oracle.com/java/technologies/downloads/#java8

> 下载对应操作系统的JDK，然后按提示安装。

### 2、下载安装包并解压：
> 打开 [releases](https://github.com/big-mouth-cn/talkx/releases) 页面，下载需要的版本。

### 3、执行数据库脚本：
数据库要求：MySQL 5.6.x+

> 创建数据库并导入初始化脚本：[talkx.sql](src%2Fmain%2Fresources%2Ftalkx.sql)
### 4、修改配置文件：
> 首先打开目录：conf/

> 然后直接复制一份配置文件 [default.application.yml](src%2Fmain%2Fconf%2Fdefault.application.yml) 然后重命名为 application.yml，按下图所示修改配置。

![iShot_2025-03-18_19.19.18.png](other%2FiShot_2025-03-18_19.19.18.png)
### 5、启动：
> 进入目录：bin/
#### 【Windows】
> 运行：start.bat
#### 【macOS、Linux】
> 运行：start.sh

### 6、登录TalkX：
> 打开 http://127.0.0.1:8086 IP地址需要按实际修改。
> 然后左下角登录，手机号码的验证码默认是：`123456`


## 配置文件说明