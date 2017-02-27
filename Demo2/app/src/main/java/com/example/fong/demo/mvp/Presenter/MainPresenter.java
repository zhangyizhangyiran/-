package com.example.fong.demo.mvp.Presenter;

import com.example.fong.demo.dagger.DaggerMainCompoent;
import com.example.fong.demo.dagger.MainCompoent;
import com.example.fong.demo.dagger.MainMoulde;
import com.example.fong.demo.mvp.model.MainModel;
import com.example.fong.demo.mvp.view.Mainview;
import com.example.fong.demo.ui.DetailPagesActivity;

import javax.inject.Inject;

/**
 * Created by Fong on 17/2/5.
 * View和Model的桥梁，它从Model层检索数据后，返回给View层
 */
public class MainPresenter implements Presenter<Mainview>, IMainPresenter {
    public Mainview mainview;
    public final MainModel mMainModel;
    //dagger2注解
    @Inject
    DetailPagesActivity detailPagesActivity;

    public MainPresenter(Mainview mainview) {
        attachView(mainview);

        mMainModel = new MainModel(this);
        //注入presenter对象
        MainCompoent builder = DaggerMainCompoent.builder()
                .mainMoulde(new MainMoulde())
                .build();
        builder.in(this);

    }

    public void loadData() {

        mMainModel.initHttp();
    }

    public void loadDetailPagesData() {
        mMainModel.initHttpDetailPages(detailPagesActivity.id);
    }

    @Override
    public void attachView(Mainview view) {
        this.mainview = view;
    }

    @Override
    public void detachView() {
        this.mainview = null;
    }

    @Override
    public void loadDataSuccess(Object Data) {
        mainview.showData(Data);

    }

    @Override
    public void loadDataFailure() {
        mainview.hideProgress();

    }
}
