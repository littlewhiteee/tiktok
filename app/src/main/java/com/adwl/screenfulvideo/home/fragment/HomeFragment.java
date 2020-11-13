package com.adwl.screenfulvideo.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.adwl.screenfulvideo.R;
import com.adwl.screenfulvideo.base.BaseLoadFragment;
import com.adwl.screenfulvideo.base.BaseMVPFragment;
import com.adwl.screenfulvideo.fullvideo.PreloadManager;
import com.adwl.screenfulvideo.fullvideo.TikTokController;
import com.adwl.screenfulvideo.fullvideo.TikTokRenderViewFactory;
import com.adwl.screenfulvideo.fullvideo.TiktokBean;
import com.adwl.screenfulvideo.fullvideo.Utils;
import com.adwl.screenfulvideo.fullvideo.VerticalViewPager;
import com.adwl.screenfulvideo.home.adapter.HomeAdapter;
import com.adwl.screenfulvideo.home.contract.HomeFragmentContract;
import com.adwl.screenfulvideo.home.listener.VideoMenuListener;
import com.adwl.screenfulvideo.home.presenter.HomeFragmentPresenter;
import com.adwl.screenfulvideo.manager.ToastManager;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import timber.log.Timber;

/**
 * created by wmm on 2020/11/11
 */
public class HomeFragment extends BaseMVPFragment<HomeFragmentPresenter> implements HomeFragmentContract.View {
    @BindView(R.id.vvp)
    VerticalViewPager mViewPager;
    @BindView(R.id.rv)
    SmartRefreshLayout mSmartRefresh;
    private int mCurPos;
    private ArrayList<String> playUrls;
    private ArrayList<TiktokBean> playBean;
    private ArrayList<TiktokBean> playBeanMoreData;
    private HomeAdapter mHomeAdapter;
    private PreloadManager mPreloadManager;
    private VideoView mVideoView;
    private TikTokController mController;
    /**
     * VerticalViewPager是否反向滑动
     */
    private boolean isFirstLoad = true;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isFirstLoad) {
            initUrls();  //演示数据
            //  mPresenter.getVideoList();  todo 接口数据
            initView();
            initViewPager();
            initVideoView();
            mSmartRefresh.setEnableLoadMore(false);
            mSmartRefresh.setOnLoadMoreListener(refreshLayout -> {
                mSmartRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                        mSmartRefresh.finishLoadMore(true);
                        if (mViewPager.getCurrentItem() < playBean.size() - 1) {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                        setSmartRefreshEnable(0);
                    }
                }, 2000);


            });
            mPreloadManager = PreloadManager.getInstance(getContext());
            mViewPager.setCurrentItem(0);
            mViewPager.post(new Runnable() {
                @Override
                public void run() {
                    startPlay(0);
                }
            });
        }
        setSmartRefreshEnable(0);
        mHomeAdapter.setVideoMenuClickListener(new VideoMenuListener() {

            @Override
            public void like(int position, TextView number) {
                number.setText("点赞了,刷新第" + position + "页数据");
            }

            @Override
            public void comment(int position) {
                ToastManager.showToast(getContext(), "评论" + position, false);
            }

            @Override
            public void forward(int position) {
                ToastManager.showToast(getContext(), "转发" + position, false);
            }
        });
    }

    private void setSmartRefreshEnable(int index) {
        int index1 = mViewPager.getCurrentItem();
        mSmartRefresh.setEnableLoadMore(playBean.size() - 1 == index1);
        mSmartRefresh.setEnableRefresh(index1 == 0);
    }


    private void initView() {
        mVideoView = new VideoView(getActivity());
        mController = new TikTokController(getActivity());
        mPreloadManager = PreloadManager.getInstance(getContext());
        mViewPager.post(() -> startPlay(0));

    }

    private void initVideoView() {
        mVideoView.setLooping(true);
        //以下只能二选一，看你的需求
        mVideoView.setRenderViewFactory(TikTokRenderViewFactory.create());
//      mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
        mVideoView.setVideoController(mController);
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(4);
        mHomeAdapter = new HomeAdapter(playBean);
        mViewPager.setAdapter(mHomeAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            private int mCurItem;

            /**
             * VerticalViewPager是否反向滑动
             */
            private boolean mIsReverseScroll;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == mCurItem) {
                    return;
                }
                mIsReverseScroll = position < mCurItem;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setSmartRefreshEnable(position);
                if (position == mCurPos) return;
                startPlay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {
                    mCurItem = mViewPager.getCurrentItem();
                }

                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mPreloadManager.resumePreload(mCurPos, mIsReverseScroll);
                } else {
                    mPreloadManager.pausePreload(mCurPos, mIsReverseScroll);
                }
            }
        });
    }

    private void initUrls() {
        playUrls = new ArrayList<>();
        playBean = new ArrayList<>();
        playUrls.clear();
        playBean.clear();
        playUrls.add("http://app.funny.clply.cn/10018932-95EECC73-E1FA-411E-A658-54F552EF64B2.mp4");
        playUrls.add("http://app.funny.clply.cn/4142f462005516fbfda87efa13b597b7.mp4");
        playUrls.add("http://app.funny.clply.cn/ca74f36a23e5e4ecec6100cfd4beb376.mp4");
        playUrls.add("http://app.funny.clply.cn/17bf381c7eac0d6ed035216c9d16820f.mp4");
        playUrls.add("http://mark.funny.clply.cn/0107851d94617e5c125d17c121751977.mp4");
        for (int i = 0; i < playUrls.size(); i++) {
            TiktokBean mTiktokBean = new TiktokBean();
            mTiktokBean.videoPlayUrl = playUrls.get(i);
            mTiktokBean.title = "我是标题我是标题我是标题我是标题" + i;
            mTiktokBean.coverImgUrl = "https://img-blog.csdn.net/20160413112832792?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center";
            playBean.add(mTiktokBean);
        }
    }

    private void startPlay(int position) {
        int count = mViewPager.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemView = mViewPager.getChildAt(i);
            HomeAdapter.ViewHolder viewHolder = (HomeAdapter.ViewHolder) itemView.getTag();
            if (viewHolder.mPosition == position) {
                mVideoView.release();
                Utils.removeViewFormParent(mVideoView);

                TiktokBean tiktokBean = playBean.get(position);
                String playUrl = mPreloadManager.getPlayUrl(tiktokBean.videoPlayUrl);
                L.i("startPlay: " + "position: " + position + "  url: " + playUrl);
                mVideoView.setUrl(playUrl);
                mController.addControlComponent(viewHolder.mTikTokView, true);
                viewHolder.mPlayerContainer.addView(mVideoView, 0);
                mVideoView.start();
                mCurPos = position;
                break;
            }
        }
    }

    public void addData() {
        playBeanMoreData = new ArrayList<>();
        playBeanMoreData.clear();
        int size = playBean.size();
        for (int i = 0; i < 10; i++) {
            TiktokBean mTiktokBean = new TiktokBean();
            mTiktokBean.videoPlayUrl = "https://www.w3school.com.cn/example/html5/mov_bbb.mp4";
            mTiktokBean.title = "大家好,我是新数据";
            mTiktokBean.coverImgUrl = "https://img-blog.csdn.net/20160413112832792?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center";
            playBeanMoreData.add(mTiktokBean);
        }
        playBean.addAll(playBeanMoreData);
        //使用此方法添加数据，使用notifyDataSetChanged会导致正在播放的视频中断
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public HomeFragmentPresenter createPresenter() {
        return new HomeFragmentPresenter(this);
    }

    @Override
    public void getVideoListSuccess() {

    }


    @Override
    public void showLoading(String loadingWay) {

    }

    @Override
    public void dismissLoading(String loadingWay) {

    }

    @Override
    public void showToast(String content) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFirstLoad = false;
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.release();
        }
    }
}
