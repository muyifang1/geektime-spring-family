package geektime.spring.data.reactive.redisdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class RedisDemoApplication implements ApplicationRunner {
    private static final String KEY = "COFFEE_MENU";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    @Bean
    ReactiveStringRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveStringRedisTemplate(factory);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 取出对hash操作的operation <key,hashkey,hashvalue>
        ReactiveHashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        CountDownLatch cdl = new CountDownLatch(1);

        // 调用jdbc查询coffee表，并把每一个结果映射成Coffee对象
        List<Coffee> list = jdbcTemplate.query(
                "select * from t_coffee", (rs, i) ->
                Coffee.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .price(rs.getLong("price"))
                        .build()
        );

        // 通过一个Flux从我List当中取出所有元素，把他们以另一个线程单线程的publish出去
        Flux.fromIterable(list)
                // 指定单线程publish
                .publishOn(Schedulers.single())
                // publish后打印 list ok
                .doOnComplete(() -> log.info("list ok"))
                // 通过flatMap 把list每个对象放入Redis的hash的Map中
                .flatMap(c -> {
                    log.info("try to put {},{}", c.getName(), c.getPrice());
                    // key为"COFFEE_MENU"，hashKey=咖啡名字，hashValue=价格
                    return hashOps.put(KEY, c.getName(), c.getPrice().toString());
                })
                // flat完成后 输出setOK
                .doOnComplete(() -> log.info("set ok"))
                // 设置缓存一分钟有效期
                .concatWith(redisTemplate.expire(KEY, Duration.ofMinutes(1)))
                .doOnComplete(() -> log.info("expire ok"))
                // 如果遇到错误，输出异常 直接返回Mono 为false
                .onErrorResume(e -> {
                    log.error("exception {}", e.getMessage());
                    return Mono.just(false);
                })
                // 最后做一个subscribe 每个元素打印boolean值
                .subscribe(b -> log.info("Boolean: {}", b),
                        e -> log.error("Exception {}", e.getMessage()),
                        // countDownLatch 目的是等待所有子线程完毕 主线程再结束。
                        () -> cdl.countDown());
        log.info("Waiting");
        cdl.await();
    }
}
