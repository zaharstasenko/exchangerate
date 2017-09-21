package com.example.zakhariystasenko.exchangerate;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zakhariy.stasenko on 9/20/2017.
 */

public class EmptyObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
