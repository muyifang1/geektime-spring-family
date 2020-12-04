package geektime.spring.reactor.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@Slf4j
public class SimpleReactorDemoApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SimpleReactorDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Flux.range(1, 6) // 创造1~6序列
				.doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
				.publishOn(Schedulers.elastic()) // publishOn后面代码会执行在 elastic线程池当中
				.doOnComplete(() -> log.info("Publisher COMPLETE 1")) // publish结束后 打印complete 1
				.map(i -> {  // 这个map操作 仅仅是封装Flux，实际动作在后面.subscribe 被调用是执行
					log.info("Publish {}, {}", Thread.currentThread(), i); // 演示map转换在 具体哪个线程上执行
//					return 10 / (i - 3); // 制造一个异常
					return i;
				})
				.doOnComplete(() -> log.info("Publisher COMPLETE 2"))
				.subscribeOn(Schedulers.single()) // subscribeOn 让订阅简历在 single单独线程上。
				.onErrorResume(e -> {    // onErrorResume 指定异常处理
					log.error("Exception {}", e.toString());
					return Mono.just(-1);
				})
//				.onErrorReturn(-1)  // onErrorReturn 使用默认值-1 作为异常返回值
				.subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
						e -> log.error("error {}", e.toString()),
						() -> log.info("Subscriber COMPLETE"),
						s -> s.request(4)  // 共有6个元素，但是一次取回4个request
				);
		Thread.sleep(2000);
	}
}

