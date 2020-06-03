package com.taobao.xdemo.rxjava

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.subscribers.SerializedSubscriber

/**
 * @author bill
 * @Date on 2020/6/1
 * @Desc:
 */
class RxBus private constructor() {
    /**
     * 相当于Rxjava1.x中的Subject
     */
    private val mBus: FlowableProcessor<Any>

    /**
     * 发送消息
     *
     * @param o
     */
    fun post(o: Any) {
        SerializedSubscriber(mBus).onNext(o)
    }

    /**
     * 确定接收消息的类型
     *
     * @param aClass
     * @param <T>
     * @return
    </T> */
    fun <T> toFlowable(aClass: Class<T>?): Flowable<T> {
        return mBus.ofType(aClass)
    }

    /**
     * 判断是否有订阅者
     *
     * @return
     */
    fun hasSubscribers(): Boolean {
        return mBus.hasSubscribers()
    }

    companion object {
        @Volatile
        private var sRxBus: RxBus? = null

        @get:Synchronized
        val default: RxBus?
            get() {
                if (sRxBus == null) {
                    synchronized(RxBus::class.java) {
                        if (sRxBus == null) {
                            sRxBus = RxBus()
                        }
                    }
                }
                return sRxBus
            }
    }

    init {
        //调用toSerialized()方法，保证线程安全
        mBus = PublishProcessor.create<Any>().toSerialized()
    }
}