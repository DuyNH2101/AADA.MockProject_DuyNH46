package com.example.aadamockproject_duynh46.domain.usecase.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class UseCase {

    protected Disposable lastDisposable;
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void disposeLast() {
        if (lastDisposable != null && !lastDisposable.isDisposed()) {
            lastDisposable.dispose();
        }
    }

    public void dispose() {
        compositeDisposable.clear();
    }
}

