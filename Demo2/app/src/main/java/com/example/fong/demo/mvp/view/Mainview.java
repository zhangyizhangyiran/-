package com.example.fong.demo.mvp.view;

/**
 * Created by Fong on 17/2/5.
 */
public interface Mainview<V> {
    /**
     * Created  on 2015/9/23.
     * 处理业务需要哪些方法
     */
    void showData(V mainModelBean);
    void showProgress();
    void hideProgress();

}
