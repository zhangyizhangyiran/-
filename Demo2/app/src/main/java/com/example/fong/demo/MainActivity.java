package com.example.fong.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.fong.demo.adapter.MainAdapter;
import com.example.fong.demo.bean.UpToData;
import com.example.fong.demo.mvp.Presenter.MainPresenter;
import com.example.fong.demo.mvp.view.Mainview;
import com.example.fong.demo.ui.DetailPagesActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 由Activity实现View里方法，包含一个Presenter的引用
 */

public class MainActivity extends AppCompatActivity implements MainAdapter.OnRecyclerViewItemClickListener, Mainview<UpToData> {


    public int id;
    public MainAdapter mainAdapter;
    @Bind(R.id.rc_main)
    RecyclerView rcMain;
    @Bind(R.id.sw)
    SwipeRefreshLayout sw;
    public MainPresenter mainPresenter;
    private UpToData mainModelBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //注册EventBus成为订阅者接受消息
        EventBus.getDefault().register(this);
        //引用MainPresenter调用其方法
        mainPresenter = new MainPresenter(this);
        //访问网络加载数据
        mainPresenter.loadData();
        //设置SwipeRefreshLayout
        setSwipeRefreshLayout();

    }

    /**
     * 定义方法接受消息
     * onEvent 方法名自定义
     * Subscribe 注解指定方法接受到消息
     */
    @Subscribe
    public void onEvent(Integer s) {
        Toast.makeText(MainActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();
    }

    private void setSwipeRefreshLayout() {
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        sw.setProgressViewOffset(true, 50, 200);
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        sw.setSize(SwipeRefreshLayout.DEFAULT);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        sw.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        sw.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                //在UI线程执行
                                runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                //刷新数据重新请求数据
                                                mainPresenter.loadData();
                                                sw.setRefreshing(false);

                                            }
                                        }
                                );

                            }
                        };
                        //3秒后
                        timer.schedule(task, 1000);
                    }
                }
        );
    }

//    /**
//     * 获取新闻列表和详情数据
//     */
//    public void initHttp() {
//        HttpHelpApi httpHelpApi = new HttpHelpApi();
//        httpHelpApi.initHttp(MainActivity.this);
//        httpHelpApi.setApiEntity(new HttpHelpApi.SetEntity() {
//            @Override
//            public void setEntityClass(HttpPostService apiService) {
//                Observable<UpToData> observable = apiService.getNewsAll();
//                observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                new Subscriber<UpToData>() {
//                                    @Override
//                                    public void onCompleted() {
//                                        unsubscribe();
//                                        //取消刷新
//                                        sw.setRefreshing(false);
//
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        unsubscribe();
//                                    }
//
//                                    @Override
//                                    public void onNext(UpToData upToData) {
//                                        MainActivity.this.upToData = upToData;
//                                        //设置RecyclerVire
//                                        setRecyclerVire();
//                                        //开始刷新
//                                        sw.setRefreshing(true);
//                                    }
//                                }
//                        );
//            }
//        });
//    }

    //recyclerview设置
    private void setRecyclerVire() {
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcMain.setLayoutManager(layoutManager);
        //设置Adapter
        mainAdapter = new MainAdapter(mainModelBean, MainActivity.this);
        rcMain.setAdapter((mainAdapter));
        //条目点击回调
        mainAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        //获取详情页对应的id 获取对应的详情数据
        id = mainModelBean.getStories().get(position).getId();
        String s = String.valueOf(id);
        //跳转到获取详情数据界面,通过intent传值
        Intent intent = new Intent(MainActivity.this, DetailPagesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", s);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消Eventbus订阅
        EventBus.getDefault().unregister(this);
        //分离视图
        mainPresenter.detachView();

    }

    /*
    * MVP模式下的回调方法
    * showData
    * showProgress
    * hideProgress
    */

    //数据加载成功回调
    @Override
    public void showData(UpToData mainModelBean) {
        this.mainModelBean = mainModelBean;
        setRecyclerVire();
        //适配器刷新
        mainAdapter.notifyDataSetChanged();
    }

    //视图加载中回调
    @Override
    public void showProgress() {
        // TODO: 17/2/5 等待加载
    }

    //数据获取失败或加载完成回调
    @Override
    public void hideProgress() {
        Toast.makeText(MainActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
    }

    private long exitTime = 0;

    //判断返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
