# 玩转Spring全家桶

[极客时间](https://time.geekbang.org)视频课程《玩转Spring全家桶》课程课件及代码示例。

# 十一章 微服务 #
### 了解 12-Factors ###
1. 基准代码 Codebase
2. 依赖 Dependencies 显示声明依赖关系
3. 配置 Config 在环境中存储配置，做到代码与配置分离
4. 后端服务 Backing services 把后端服务当做附加资源
5. 构建，发布，运行 Build, release, run
6. 进程 Processes 以一个或多个无状态的进程运行应用，无状态应用易于扩展，思路是创建更多实例
7. 端口绑定 Port Binding 通过端口绑定提供服务Tomcat或者Jetty，负载均衡在端口前做Proxy
8. 并发 Concurrency 通过进程模型进行扩展，要考虑是否可以两台机器部署运行
9. 易处理 Disposability 快速启动和优雅终止可最大化健壮性
10. 开发环境与线上环境等价 Dev / Prod parity
11. 日志 Logs
12. 管理进程 Admin proccesses


# Spring IOC #
## 反射 ##
1. Class klazz = Class.forName();
2. Class klazz = 类名.class;
3. CLass klazz = 对象名.getClass();
4. Constructor ctor = klazz.getDeclareConstructor();
5. Object obj = ctor.newInstance();

## 读取Bean 定义信息 BeanDefinitionReader ##
1. xml
2. 注解
3. json

## BeanFactory 根据上述信息 利用java反射 进行实例化 ##
1. 中间有增强处理 beanFactoryPostProcessor

## 实例化 ##
1. 实例化
2. 填充属性
3. 执行aware接口需要实现的方法
4. BeanPostProcessor:before
5. init-method方法
6. BeanPostProcessor:after
7. 得到完整对象

##  #
