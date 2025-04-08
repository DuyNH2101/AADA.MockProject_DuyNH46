package com.example.aadamockproject_duynh46.domain.usecase.base;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class SingleUseCase<T> extends UseCase {

    protected abstract Single<T> buildUseCaseSingle();

    public void execute(
            OnSuccess<T> onSuccess,
            OnError onError,
            OnFinished onFinished
    ) {
        disposeLast();
        lastDisposable = buildUseCaseSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished::onFinish)
                .subscribe(onSuccess::onSuccess, onError::onError);

        if (lastDisposable != null) {
            compositeDisposable.add(lastDisposable);
        }
    }

    public interface OnSuccess<T> {
        void onSuccess(T t);
    }

    public interface OnError {
        void onError(Throwable t);
    }

    public interface OnFinished {
        void onFinish();
    }
}

