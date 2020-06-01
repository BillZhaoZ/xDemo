package com.taobao.xdemo.rxjava;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;

/**
 * @author bill
 * @Date on 2020/5/28
 * @Desc:
 */
public class RxJavaManager {

    public static final String TAG = "rxJava";

    /**
     * 1、创建一个下游  观察者Observer
     *
     * 其中onCompleted、onError和onNext是必须要实现的方法，其含义如下。
     * • onCompleted:事件队列完结。RxJava不仅把每个事件单独处理，其还会把它们看作一个队列。当不
     * 会再有新的 onNext发出时，需要触发 onCompleted()方法作为完成标志。
     * • onError:事件队列异常。在事件处理过程中出现异常时，onError()会被触发，同时队列自动终
     * 止，不允许再有事件发出。
     * • onNext:普通的事件。将要处理的事件添加到事件队列中。
     * • onStart:它会在事件还未发送之前被调用，可以用于做一些准备工作。例如数据的清零或重置。这是 一个可选方法，默认情况下它的实现为空。
     */
    public Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "onSubscribe === subscribe === " + d);
        }

        @Override
        public void onNext(Integer value) {
            Log.d(TAG, "onNext === " + value);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError === error");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete === ");
        }
    };

    /**
     * 2、创建一个上游  被观察者 Observable：
     *
     * 它决定什么时候触发事件以及触发怎样的事件。RxJava 使用 create 方法来创建一个Observable，并为它
     * 定义事件触发规则
     */
    public Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
            emitter.onNext(1);

            emitter.onComplete();

            emitter.onNext(2);
            emitter.onNext(3);

            Log.d(TAG, "subscribe === ");
        }
    });

    /**
     * 定时器
     */
    public Observable<Long> observableTime = Observable.interval(2, TimeUnit.SECONDS);
    public Observer<Long> observerTime = new Observer<Long>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Long aLong) {
            Log.d(TAG, "observerTime === onNext === " + aLong);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    /**
     * ranger
     */
    public Observable<Integer> observableRanger = Observable.range(0, 5);
    public Observer<Integer> observerRanger = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer aLong) {
            Log.d(TAG, "observerRanger === onNext === " + aLong);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    /**
     * flatMap
     */
    public static void actionFlatMap() {
        List<Integer> list = Arrays.asList(1, 2, 3);

        // 这里用了lambda。
        Observable.fromIterable(list)
            .flatMap(integer -> {
                log("开始执行，第" + integer + "圆球的任务" + getThreadName());
                return getObservable(integer);
            }).subscribe(s -> log("已完成" + s + getThreadName()));
    }

    public static Observable<String> getObservable(final int integer) {
        return Observable.create((ObservableOnSubscribe<String>)emitter -> {
            emitter.onNext("第" + integer + "圆球的第1个棱形任务");
            if (integer != 1) {
                // 第2和第3个圆球的第二个任务延时。
                Thread.sleep(5 * 1000);
            }
            emitter.onNext("第" + integer + "圆球的第2个棱形任务");
            emitter.onComplete();
        }).subscribeOn(Schedulers.newThread());
    }

    // 返回当前的线程名
    public static String getThreadName() {
        return "  |  ThreadName=" + Thread.currentThread().getName();
    }

    private static void log(String log) {
        Log.d(TAG, log);
    }

}
