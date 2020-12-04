## Project Recator 一些核心概念 ##
1. Operators - Publisher(消息发布者) / Subscriber(消息订阅者) 可以理解成生产者和消费者
- 不同于Java集合pull数据机制，Publisher会主动把信息推送给Subscriber
- 重点理解： Nothing Happens Until You subscribe()
2. Flux[0..N] -onNext()、onComplete()、onError()
  - Flux是n个元素的序列，预设了遇到下一个元素执行方法，全部完成后方法，出错方法。都是可以传入Lambda表达式
3. Mono[0..1] -onNext()、onComplete()、onError()
  - Mono代表0或者1个元素同样提供上述方法
4. Backpressure 反压力概念 生产者一次产生多个信息，可以指定只request几个
  - Subsription
  - onRequest()、onCancel()、onDispose() 设置每次请求多少个元素，设置取消或者终止
5. 线程调度 Schedulers 可以指定这些元素publisher到哪个线程上面做subscribe
  - immediate() 使用当前线程 / single() 复用一个线程 / newSingle() 新起一个线程
  - elastic() 缓存线程池中做后续操作/ parallel() 固定线程池 / newParallel() 新建线程池
6. 错误处理
  - onError 相当于try/catch
  - onErrorReturn 指定返回值作为异常结果
  - onErrorResume 特定Lambda来做一个异常处理
  - doOnError / doFinally

## Spring Data Redis ##
- Spring Data Redis 同时支持 Jedis客户端 和 Lettuce客户端
- Lettuce 能够支持Reactive方式
- Spring Data Redis 中主要的支持
  - ReactiveRedisConnection 建立一个Reactive链接
  - ReactiveRedisConnectionFactory 上述Reactive链接通过这个Factory来构造
  - ReactiveRedisTemplate 通过template操作，但是返回结果不是具体值，而是mode或者Flux
    - opsForXxx()

## Spring Data MongoDB ##
- MongoDB 官方提供了支持Reactive的驱动
  - mongodb-driver-reactivestreams
- Spring Data MongoDB 中主要的支持
  - ReactiveMongoClientFactoryBean
  - ReactiveMongoDatabaseFactory
  - ReactiveMongoTemplate

## Spring Data R2DBC ##
- R2DBC spring-data-R2DBC 并没有release，项目还在孵化当中
  - Reactive Relational Database Connectivity
- 支持的数据库
  - Postgres
  - H2
  - Microsoft SQL Server
- 注意暂不支持MySQL。原因是WebFlux无论怎样reactive在传统关系型数据库面前都是同步

### Spring Data R2DBC 主要的类 ###
- ConnectionFactory
- DatabaseClient
  - execute().sql(SQL)
  - select() / insert()
  - inTransaction(db -> {})
- R2dbcExceptionTranslator
  - SqlErrorCodeR2dbcExceptionTranslator

### Spring AOP 常用注解 ###
`@EnableAspectJAutoProxy` 开启Aspect支持
`@Aspect` 声明当前类是一个切面，还需要把当前类声明成Bean
`@PointCut` 指定PointCut
`@Before` 指定前序执行
`@After` / `@AfterReturning` / `@AfterThrowing` 指定后续执行
`@Around` 参数必须是 ProceedingJoinPoint
`@Order` 指定执行顺序，Order数越小，优先级越高
- 推荐使用 `@Pointcut("execution(public * *(..))")` 方式 通过表达式可以万能进行拦截
