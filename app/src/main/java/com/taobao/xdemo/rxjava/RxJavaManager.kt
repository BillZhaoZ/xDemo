package com.taobao.xdemo.rxjava

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author bill
 * @Date on 2020/5/28
 * @Desc:
 */
class RxJavaManager {
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
    var observer: Observer<Int> = object : Observer<Int> {
        override fun onSubscribe(d: Disposable) {
            Log.d(TAG, "onSubscribe === subscribe === $d")
        }

        override fun onNext(value: Int) {
            Log.d(TAG, "onNext === $value")
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, "onError === error")
        }

        override fun onComplete() {
            Log.d(TAG, "onComplete === ")
        }
    }

    /**
     * 2、创建一个上游  被观察者 Observable：
     *
     * 它决定什么时候触发事件以及触发怎样的事件。RxJava 使用 create 方法来创建一个Observable，并为它
     * 定义事件触发规则
     */
    var observable = Observable.create<Int> { emitter ->
        emitter.onNext(1)
        emitter.onComplete()
        emitter.onNext(2)
        emitter.onNext(3)
        Log.d(TAG, "subscribe === ")
    }

    /**
     * 定时器
     */
    var observableTime = Observable.interval(2, TimeUnit.SECONDS)
    var observerTime: Observer<Long> = object : Observer<Long> {
        override fun onSubscribe(d: Disposable) {}
        override fun onNext(aLong: Long) {
            Log.d(TAG, "observerTime === onNext === $aLong")
        }

        override fun onError(e: Throwable) {}
        override fun onComplete() {}
    }

    /**
     * ranger
     */
    var observableRanger = Observable.range(0, 5)
    var observerRanger: Observer<Int> = object : Observer<Int> {
        override fun onSubscribe(d: Disposable) {}
        override fun onNext(aLong: Int) {
            Log.d(TAG, "observerRanger === onNext === $aLong")
        }

        override fun onError(e: Throwable) {}
        override fun onComplete() {}
    }

    companion object {
        const val TAG = "rxJava"

        /**
         * flatMap
         */
        fun actionFlatMap() {
            val list = Arrays.asList(1, 2, 3)

            // 这里用了lambda。
            Observable.fromIterable(list)
                    .flatMap { integer: Int ->
                        log("开始执行，第" + integer + "圆球的任务" + threadName)
                        getObservable(integer)
                    }.subscribe { s: String -> log("已完成$s$threadName") }
        }

        fun getObservable(integer: Int): Observable<String> {
            return Observable.create(ObservableOnSubscribe { emitter: ObservableEmitter<String> ->
                emitter.onNext("第" + integer + "圆球的第1个棱形任务")
                if (integer != 1) {
                    // 第2和第3个圆球的第二个任务延时。
                    Thread.sleep(5 * 1000.toLong())
                }
                emitter.onNext("第" + integer + "圆球的第2个棱形任务")
                emitter.onComplete()
            } as ObservableOnSubscribe<String>).subscribeOn(Schedulers.newThread())
        }

        // 返回当前的线程名
        val threadName: String
            get() = "  |  ThreadName=" + Thread.currentThread().name

        private fun log(log: String) {
            Log.d(TAG, log)
        }
        /**
         * 分组操作符
         */
    }
}