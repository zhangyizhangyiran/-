package com.example.fong.demo.api;

import com.example.fong.demo.bean.News;
import com.example.fong.demo.bean.RetrofitEntity;
import com.example.fong.demo.bean.UpToData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 *
 */
public interface HttpPostService {
    /**
     * 闪屏页图片
     **/
    @GET("4/start-image/1080*1776")
    Observable<RetrofitEntity> getAllVedioBy();

    /**
     * 获取新闻列表
     **/
    @GET("4/news/latest")
    Observable<UpToData> getNewsAll();

    /**
     * 新闻详情
     **/
    @GET("4/news/{id}")
    Observable<News> getNews(@Path("id") String id);

}
