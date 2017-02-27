package com.example.fong.demo.dagger;

import com.example.fong.demo.mvp.Presenter.MainPresenter;

import dagger.Component;

/**
 * Created by Fong on 17/2/6.
 * 建立类与module之间的关系
 */
@Component(modules = MainMoulde.class)
public interface MainCompoent {
    void in(MainPresenter presenter);
}
