package com.taobao.xdemo.rxjava;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.taobao.xdemo.rxjava.RxJavaManager.TAG;

/**
 * @author bill
 * @Date on 2020/5/28
 * @Desc:
 */
public class RxJavaTest {

    public static void main(String[] args) {

        //建立连接
       /* RxJavaManager rxJavaManager = new RxJavaManager();
        rxJavaManager.observable.subscribe(rxJavaManager.observer);*/

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });

    }
}
