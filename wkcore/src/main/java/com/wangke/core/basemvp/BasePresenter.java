package com.wangke.core.basemvp;


import android.support.annotation.UiThread;


public abstract class BasePresenter <V extends BaseView, M extends BaseModel>{

    protected V view;
    protected M model;

    public BasePresenter() {
        model = createModel();
    }

    @UiThread
    void attachView(V view) {
        this.view = view;
    }

    @UiThread
    void detachView() {
        this.view = null;
    }

    public abstract M createModel();

}
