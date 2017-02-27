package com.example.fong.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fong.demo.R;
import com.example.fong.demo.bean.UpToData;

import org.greenrobot.eventbus.EventBus;

/**
 *
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    public final UpToData upToData;
    public final Context context;
    ImageView item_lv;
    public OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public String s;


    public MainAdapter(UpToData upToData, Context context) {
        this.upToData = upToData;
        this.context = context;

    }

    //返回布局
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
        return myViewHolder;
    }

    //绑定viewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //获取图片URL地址
        s = upToData.getStories().get(position).getImages().get(0);

        Log.i("TAG", s);
        Glide.with(context)
                .load(s)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(item_lv);

        holder.item_tv.setText(upToData.getStories().get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EventBus发送消息
                EventBus.getDefault().post(position);
                mOnItemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return upToData.getStories().size();
    }


    //ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item_tv;


        public MyViewHolder(View itemView) {
            super(itemView);
            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
            item_lv = (ImageView) itemView.findViewById(R.id.item_lv);


        }
    }


    //条目点击接口回调
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //条目点击接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

}
