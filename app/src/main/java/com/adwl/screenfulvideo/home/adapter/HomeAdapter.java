package com.adwl.screenfulvideo.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.adwl.screenfulvideo.R;
import com.adwl.screenfulvideo.fullvideo.PreloadManager;
import com.adwl.screenfulvideo.fullvideo.TikTokView;
import com.adwl.screenfulvideo.fullvideo.TiktokBean;
import com.adwl.screenfulvideo.home.listener.VideoMenuListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends PagerAdapter {

    /**
     * View缓存池，从ViewPager中移除的item将会存到这里面，用来复用
     */
    private List<View> mViewPool = new ArrayList<>();
    private VideoMenuListener mVideoMenuListener = null;

    /**
     * 数据源
     */
    private List<TiktokBean> mVideoBeans;

    public HomeAdapter(List<TiktokBean> videoBeans) {
        this.mVideoBeans = videoBeans;
    }

    @Override
    public int getCount() {
        return mVideoBeans == null ? 0 : mVideoBeans.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        View view = null;
        if (mViewPool.size() > 0) {//取第一个进行复用
            view = mViewPool.get(0);
            mViewPool.remove(0);
        }

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_tik_tok, container, false);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        TiktokBean item = mVideoBeans.get(position);
        //开始预加载
        PreloadManager.getInstance(context).addPreloadTask(item.videoPlayUrl, position);
        Glide.with(context)
                .load(item.coverImgUrl)
                .placeholder(android.R.color.white)
                .into(viewHolder.mThumb);
        viewHolder.mTitle.setText(item.title);
        viewHolder.mTitle.setOnClickListener(v -> Toast.makeText(context, "点击了标题", Toast.LENGTH_SHORT).show());

        viewHolder.ivVideoLike.setOnClickListener((View view1) -> {
            if (mVideoMenuListener != null) {
                mVideoMenuListener.like(position,viewHolder.mTitle);
            }
        });
        viewHolder.ivVideoComment.setOnClickListener(view1 -> {
            if (mVideoMenuListener != null) {
                mVideoMenuListener.comment(position);
            }
        });
        viewHolder.ivVideoForward.setOnClickListener(view1 -> {
            if (mVideoMenuListener != null) {
                mVideoMenuListener.forward(position);
            }
        });
        viewHolder.mPosition = position;
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View itemView = (View) object;
        container.removeView(itemView);
        TiktokBean item = mVideoBeans.get(position);
        //取消预加载
        PreloadManager.getInstance(container.getContext()).removePreloadTask(item.videoDownloadUrl);
        //保存起来用来复用
        mViewPool.add(itemView);
    }

    /**
     * 借鉴ListView item复用方法
     */
    public static class ViewHolder {

        public int mPosition;
        public TextView mTitle;//标题
        public ImageView mThumb;//封面图
        public TikTokView mTikTokView;
        public FrameLayout mPlayerContainer;
        public  ImageView ivVideoLike, ivVideoComment, ivVideoForward;

        ViewHolder(View itemView) {
            mTikTokView = itemView.findViewById(R.id.tiktok_View);
            mTitle = mTikTokView.findViewById(R.id.tv_title);
            ivVideoLike = mTikTokView.findViewById(R.id.iv_video_like);
            ivVideoComment = mTikTokView.findViewById(R.id.iv_video_comment);
            ivVideoForward = mTikTokView.findViewById(R.id.iv_video_Forward);
            mThumb = mTikTokView.findViewById(R.id.iv_thumb);
            mPlayerContainer = itemView.findViewById(R.id.container);
            itemView.setTag(this);
        }
    }

    public void setVideoMenuClickListener(VideoMenuListener listener) {
        mVideoMenuListener = listener;
    }
}
