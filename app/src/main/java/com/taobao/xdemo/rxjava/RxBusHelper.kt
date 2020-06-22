package com.taobao.xdemo.rxjava

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

object RxBusHelper {
    /**
     * 发布消息
     *
     * @param o
     */
    @JvmStatic
    fun post(o: Any?) {
        if (o != null) {
            RxBus.default?.post(o)
        }
    }

    /**
     * 接收消息,并在主线程处理
     *
     * @param aClass
     * @param disposables 用于存放消息
     * @param listener
     * @param <T>
    </T> */
    fun <T> doOnMainThread(aClass: Class<T>?, disposables: CompositeDisposable,
                           listener: OnEventListener<T>) {
        RxBus.default?.toFlowable(aClass)?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ t: T -> listener.onEvent(t) }
                ) { throwable: Throwable? -> listener.onError(ErrorBean(ErrorCode.ERROR_CODE_RXBUS, ErrorCode.ERROR_DESC_RXBUS)) }?.let { disposables.add(it) }
    }

    fun <T> doOnMainThread(aClass: Class<T>?, listener: OnEventListener<T>) {
        RxBus.default?.toFlowable(aClass)?.observeOn(AndroidSchedulers.mainThread())?.subscribe({ t: T -> listener.onEvent(t) }
        ) { throwable: Throwable? -> listener.onError(ErrorBean(ErrorCode.ERROR_CODE_RXBUS, ErrorCode.ERROR_DESC_RXBUS)) }
    }

    /**
     * 接收消息,并在子线程处理
     *
     * @param aClass
     * @param disposables
     * @param listener
     * @param <T>
    </T> */
    fun <T> doOnChildThread(aClass: Class<T>?, disposables: CompositeDisposable,
                            listener: OnEventListener<T>) {
        RxBus.default?.toFlowable(aClass)?.subscribeOn(Schedulers.newThread())
                ?.subscribe({ t: T -> listener.onEvent(t) }
                ) { throwable: Throwable? -> listener.onError(ErrorBean(ErrorCode.ERROR_CODE_RXBUS, ErrorCode.ERROR_DESC_RXBUS)) }?.let { disposables.add(it) }
    }

    fun <T> doOnChildThread(aClass: Class<T>?, listener: OnEventListener<T>) {
        RxBus.default?.toFlowable(aClass)?.subscribeOn(Schedulers.newThread())?.subscribe({ t: T -> listener.onEvent(t) }
        ) { throwable: Throwable? -> listener.onError(ErrorBean(ErrorCode.ERROR_CODE_RXBUS, ErrorCode.ERROR_DESC_RXBUS)) }
    }

    interface OnEventListener<T> {
        fun onEvent(t: T)
        fun onError(errorBean: ErrorBean?)
    }
}