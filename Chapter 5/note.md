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
