package com.example.fong.demo.mvp.Presenter;

/**
 * Created by Fong on 17/2/5.
 */
public interface Presenter<V> {
    void attachView(V view);
    void detachView();
}
