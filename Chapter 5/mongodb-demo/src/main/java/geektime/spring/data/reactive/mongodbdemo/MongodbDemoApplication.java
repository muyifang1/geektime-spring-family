package geektime.spring.data.reactive.mongodbdemo;

import geektime.spring.data.reactive.mongodbdemo.converter.MoneyReadConverter;
import geektime.spring.data.reactive.mongodbdemo.converter.MoneyWriteConverter;
import geektime.spring.data.reactive.mongodbdemo.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootApplication
@Slf4j
public class MongodbDemoApplication implements ApplicationRunner {
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	// 设置coundDownLatch计数
	private CountDownLatch cdl = new CountDownLatch(2);

	public static void main(String[] args) {
		SpringApplication.run(MongodbDemoApplication.class, args);
	}

	@Bean
	public MongoCustomConversions mongoCustomConversions() {
		return new MongoCustomConversions(
				Arrays.asList(new MoneyReadConverter(),
						new MoneyWriteConverter()));
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		startFromInsertion(() -> log.info("Runnable"));
		// 开始方法，在这里通过Runnable调用 decreaseHighPrice() 才能确保在Insert后执行。
		startFromInsertion(() -> {
			log.info("Runnable");
			decreaseHighPrice();
		});

		log.info("after starting");
		// 这里调用decreaseHighPrice并不能保证 在insert之后执行，注意多线程
//		decreaseHighPrice();

		// countDownLatch 让主线程等待子线程结束
		cdl.await();
	}

	private void startFromInsertion(Runnable runnable) {
		// 对之前创建的coffee对象进行 插入
		mongoTemplate.insertAll(initCoffee())
				// 插入结果 publish 到一个 elastic 线程池中
				.publishOn(Schedulers.elastic())
				// 输出每个对象
				.doOnNext(c -> log.info("Next: {}", c))
				// 全部完成后 执行runnable
				.doOnComplete(runnable)
				// 所有结束后，减少一个countDown计数
				.doFinally(s -> {
					cdl.countDown();
					log.info("Finnally 1, {}", s);
				})
				// 以上操作返回结果为Flux，下面是对Flux进行count,其实是把Flux转换成了Mono
				.count()
				// 注意这个subscribe对象是mono
				.subscribe(c -> log.info("Insert {} records", c));
	}

	/**
	 * 把 30元以上的coffee 减价5元，同时修改updateTime时间戳，返回被修改的数量
	 */
	private void decreaseHighPrice() {
		mongoTemplate.updateMulti(query(where("price").gte(3000L)),
				new Update().inc("price", -500L)
						.currentDate("updateTime"), Coffee.class)
				.doFinally(s -> {
					cdl.countDown();
					log.info("Finnally 2, {}", s);
				})
				.subscribe(r -> log.info("Result is {}", r));
	}

	private List<Coffee> initCoffee() {
		Coffee espresso = Coffee.builder()
				.name("espresso")
				.price(Money.of(CurrencyUnit.of("CNY"), 20.0))
				.createTime(new Date())
				.updateTime(new Date())
				.build();
		Coffee latte = Coffee.builder()
				.name("latte")
				.price(Money.of(CurrencyUnit.of("CNY"), 30.0))
				.createTime(new Date())
				.updateTime(new Date())
				.build();

		return Arrays.asList(espresso, latte);
	}
}
