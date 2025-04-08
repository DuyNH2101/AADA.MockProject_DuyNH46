package com.example.aadamockproject_duynh46.domain.usecase.base;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class FlowableUseCase<T> extends UseCase{
    protected abstract Flowable<T> buildUseCaseFlowable();

    public void execute(
            OnSuccess<T> onSuccess,
            OnError onError,
            OnFinished onFinished
    ) {
        disposeLast();
        lastDisposable = buildUseCaseFlowable()
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
