package com.example.zakhariystasenko.exchangerate.utils;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SimpleSingleObserver<T> implements SingleObserver<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onSuccess(T value) {

    }

    @Override
    public void onError(Throwable e) {

    }
}
