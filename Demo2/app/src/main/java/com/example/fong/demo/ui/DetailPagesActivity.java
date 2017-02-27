package com.example.fong.demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.fong.demo.R;
import com.example.fong.demo.bean.News;
import com.example.fong.demo.mvp.Presenter.MainPresenter;
import com.example.fong.demo.mvp.view.Mainview;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Fong on 17/1/5.
 */
public class DetailPagesActivity extends AppCompatActivity implements Mainview<News> {


    public static String id = null;
    @Bind(R.id.web_dp)
    WebView webDp;
    @Bind(R.id.detali_tv)
    TextView detaliTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pages);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        //获取数据
        getHttpNews();

    }

    private void getHttpNews() {
        MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.loadDetailPagesData();

    }

    /**
     * MVP回调
     */
    @Override
    public void showData(News mainModelBean) {
        //跳转webviw
        startWebView(mainModelBean);
        detaliTv.setVisibility(View.GONE);

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {


    }

    public void startWebView(News news) {
        webDp.loadUrl(news.getShare_url());
        //webview拦截器
        webDp.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                detaliTv.setVisibility(View.GONE);

            }
        });


    }

}
