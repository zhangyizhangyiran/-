package com.example.fong.demo.api;

import android.content.Context;
import android.util.Log;

import com.example.fong.demo.BuildConfig;
import com.example.fong.demo.global.Constant;
import com.example.fong.demo.utils.AbFileUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.AppUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 简单封装网络请求 结合 Retrifit Rxjava okhttp
 */
public class HttpHelpApi {


    public HttpPostService apiService;
    public int maxCacheTime = 60;


    /**
     * 初始化网络请求 Retrofit
     */
    public void initHttp(final Context context) {

        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //通过builder可以设置log拦截器网络拦截器等等
        //打印请求log日志
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        // 缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(AbFileUtil.getCacheDir(), "httpCache");

        //Log.d("OkHttp", "缓存目录---" + cacheFile.getAbsolutePath());
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!AppUtil.isNetworkAvailable(context)) {//如果网络不可用或者设置只用网络
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();

                } else if (AppUtil.isNetworkAvailable(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build();
                }

                Response response = chain.proceed(request);
                //如果网络可用
                if (AppUtil.isNetworkAvailable(context)) {
                    //Log.d("OkHttp", "网络可用响应拦截");
                    response = response.newBuilder()
                            //覆盖服务器响应头的Cache-Control,用我们自己的,因为服务器响应回来的可能不支持缓存
                            .header("Cache-Control", "public,max-age=" + maxCacheTime)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    Log.d("OkHttp", "网络不可用响应拦截");
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    response = response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;

            }
        };
        builder.cache(cache);
        builder.interceptors().add(cacheInterceptor);//添加本地缓存拦截器，用来拦截本地缓存
        builder.networkInterceptors().add(cacheInterceptor);//添加网络拦截器，用来拦截网络数据
        //设置头部
//        Interceptor headerInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                Request.Builder requestBuilder = originalRequest.newBuilder()
//                        .header("myhead", "myhead")
//                        .header("Content-Type", "application/json")
//                        .header("Accept", "application/json")
//                        .method(originalRequest.method(), originalRequest.body());
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        };
//        builder.addInterceptor(headerInterceptor );

        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.connectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.WEBBASEAPI)
                .build();
        apiService = retrofit.create(HttpPostService.class);

//        Observable<RetrofitEntity> observable = apiService.getAllVedioBy();
//        observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        new Subscriber<RetrofitEntity>() {
//                            @Override
//                            public void onCompleted() {
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.e("tag", e.toString());
//                            }
//
//                            @Override
//                            public void onNext(RetrofitEntity retrofitEntity) {
//
//
//                            }
//
//
//                        });
    }

    //接口回调
    public void setApiEntity(SetEntity setEntity) {

        setEntity.setEntityClass(apiService);
    }

    //定义接口
    public interface SetEntity {
        void setEntityClass(HttpPostService apiService);
    }
}
