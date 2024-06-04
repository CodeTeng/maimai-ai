package com.teng.maidada;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/5/29 15:46
 */
public class RxJavaTest {

    @Test
    void test() {
        Flowable<String> flowable = Flowable.range(0, 50).map(String::valueOf);
        Single<List<Integer>> list = Flowable.range(0, Integer.MAX_VALUE).toList();
        Single<List<Integer>> list1 = Flowable.range(0, Integer.MAX_VALUE).filter(i -> i > 10).toList();
        Flowable<String> flowable1 = Flowable.just("A", "B", "C");
        Flowable<String> flowable2 = Flowable.just("D", "E", "F");
        Flowable<String> concat = Flowable.concat(flowable1, flowable2);

        flowable.observeOn(Schedulers.io())
                .doOnNext(item -> {
                    System.out.println("来数据了" + item.toString());
                })
                .doOnError(e -> {
                    System.out.println("出错了" + e.getMessage());
                })
                .doOnComplete(() -> {
                    System.out.println("数据处理完了");
                }).subscribe();

    }

    @Test
    void rxJavaTest() throws InterruptedException {
        // 创建一个流，每秒发射一个递增的整数（数据流变化）
        io.reactivex.Flowable<Long> flowable = io.reactivex.Flowable.interval(1, TimeUnit.SECONDS)
                .map(i -> i + 1).subscribeOn(io.reactivex.schedulers.Schedulers.io());// 制定创建流的线程池

        // 订阅 Flowable 流，并打印每个接受到的数字
        flowable.observeOn(io.reactivex.schedulers.Schedulers.io())
                .doOnNext(item -> System.out.println(item.toString()))
                .subscribe();

        // 主线程睡眠
        Thread.sleep(10000L);
    }
}
