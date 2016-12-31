package com.baichang.android.request;

import android.content.Context;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by iscod.
 * Time:2016/12/30-15:08.
 */

public class HttpRxHelper {

    //变换操作符省略subscribeOnOn和observeOn的设置
//    @SuppressWarnings("unchecked")
//    private final Observable.Transformer schedulersTransformer = new Observable.Transformer() {
//        @Override
//        public Object call(Object observable) {
//            return ((Observable) observable).subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread());
//        }
//    };

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers(final Context context) {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (context != null) {
                                    RequestDialogUtils.show(context);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

//    @SuppressWarnings("unchecked")
//    public <T> Observable.Transformer<T, T> applySchedulers() {
//        return (Observable.Transformer<T, T>) schedulersTransformer;
//    }
}
