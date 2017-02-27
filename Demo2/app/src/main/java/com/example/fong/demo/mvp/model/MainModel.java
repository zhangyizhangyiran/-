package com.example.fong.demo.mvp.model;

import com.example.fong.demo.MyApplication;
import com.example.fong.demo.api.HttpHelpApi;
import com.example.fong.demo.api.HttpPostService;
import com.example.fong.demo.bean.News;
import com.example.fong.demo.bean.UpToData;
import com.example.fong.demo.mvp.Presenter.IMainPresenter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangyi
 * 业务具体处理，包括负责存储、检索、操纵数据等
 */

public class MainModel {
    IMainPresenter mIMainPresenter;
    public MainModel(IMainPresenter iMainPresenter ) {
        this.mIMainPresenter = iMainPresenter;

    }

    /**
     * 获取新闻列表和详情数据
     */
    public void initHttp() {
        HttpHelpApi httpHelpApi = new HttpHelpApi();
        httpHelpApi.initHttp(MyApplication.getInstance());
        httpHelpApi.setApiEntity(new HttpHelpApi.SetEntity() {
            @Override
            public void setEntityClass(HttpPostService apiService) {
                Observable<UpToData> observable = apiService.getNewsAll();
                observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<UpToData>() {
                                    @Override
                                    public void onCompleted() {
                                        unsubscribe();
                                        //取消刷新


                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        unsubscribe();

                                        mIMainPresenter.loadDataFailure();
                                    }

                                    @Override
                                    public void onNext(UpToData upToData) {
                                        // TODO: 17/2/5 回调给presenter层
                                        mIMainPresenter.loadDataSuccess(upToData);

                                    }
                                }
                        );
            }
        });
    }


    /***
     *
     * 获取详情页URL
     *
     */

    public void initHttpDetailPages(final String id) {
        HttpHelpApi httpHelpApi = new HttpHelpApi();
        httpHelpApi.initHttp(MyApplication.getInstance());
        httpHelpApi.setApiEntity(new HttpHelpApi.SetEntity() {
            @Override
            public void setEntityClass(HttpPostService apiService) {
                Observable<News> observable = apiService.getNews(id);
                observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(

                                new Subscriber<News>() {

                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(News Data) {

                                        mIMainPresenter.loadDataSuccess(Data);

                                    }

                                    @Override
                                    public void onStart() {
                                        super.onStart();

                                    }
                                }
                        );
            }
        });
    }


}
