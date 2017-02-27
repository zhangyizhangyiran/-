package com.example.fong.demo.mvp.Presenter;

/**
 * Created by Fong on 17/2/5.
 * 此接口作用是连接Model
 */
public interface IMainPresenter<V> {
    void loadDataSuccess(V Data);

    void loadDataFailure();
}
