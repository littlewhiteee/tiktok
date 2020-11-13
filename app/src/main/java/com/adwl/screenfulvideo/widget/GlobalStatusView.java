package com.adwl.screenfulvideo.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import com.adwl.screenfulvideo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * created by wmm
 */
public class GlobalStatusView extends LinearLayout {
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_LOAD_SUCCESS = 2;
    public static final int STATUS_LOAD_FAILED = 3;
    public static final int STATUS_EMPTY_DATA = 4;

    @BindView(R.id.status_view_icon)
    ImageView statusIcon;
    @BindView(R.id.status_view_text)
    TextView statusDes;
    @BindView(R.id.status_view_btn)
    TextView statusBtn;
    @BindView(R.id.status_loading_view)
    ProgressBar loadingView;

    private Build mBuild;

    @IntDef({STATUS_LOADING, STATUS_LOAD_SUCCESS, STATUS_LOAD_FAILED, STATUS_EMPTY_DATA})
    public @interface ViewStatus {
    }

    private @ViewStatus int status;

    public GlobalStatusView(Context context) {
        this(context, null, 0);
    }


    public GlobalStatusView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlobalStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_global_status_view, this, true);
        ButterKnife.bind(this);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(VERTICAL);
//        setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_F5F3F8));
        setVisibility(GONE);
    }

    public void setBuild(Build build) {
        this.mBuild = build;
        if (mBuild.marginTop > 0 || mBuild.marginBottom > 0) {
            FrameLayout.LayoutParams layoutParams;
            if (getLayoutParams() instanceof FrameLayout.LayoutParams) {
                Timber.tag("long_GlobalStatusView").e("getLayoutParams()");
                layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            } else {
                Timber.tag("long_GlobalStatusView").e("new LayoutParams()");
                layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            }
            layoutParams.bottomMargin = mBuild.marginBottom;
            layoutParams.topMargin = mBuild.marginTop;
            setLayoutParams(layoutParams);
            requestLayout();
        }
    }

    public void setStatus(@ViewStatus int status) {
        this.status = status;
        loadingView.setVisibility(GONE);
        switch (status) {
            case STATUS_LOADING:
                if (getVisibility() != VISIBLE) {
                    setVisibility(VISIBLE);
                }
                statusIcon.setVisibility(GONE);
                statusDes.setVisibility(GONE);
                statusBtn.setVisibility(GONE);
                loadingView.setVisibility(VISIBLE);
                break;
            case STATUS_LOAD_FAILED:
                if (getVisibility() != VISIBLE) {
                    setVisibility(VISIBLE);
                }
                if (mBuild != null) {
                    if (mBuild.failIcon != 0) {
                        statusIcon.setVisibility(VISIBLE);
                        statusIcon.setImageResource(mBuild.failIcon);
                    } else {
                        statusIcon.setVisibility(GONE);
                    }
                    if (TextUtils.isEmpty(mBuild.failText)) {
                        statusDes.setVisibility(GONE);
                    } else {
                        statusDes.setVisibility(VISIBLE);
                        statusDes.setText(mBuild.failText);
                    }

                    if (TextUtils.isEmpty(mBuild.failBtn)) {
                        statusBtn.setVisibility(GONE);
                    } else {
                        statusBtn.setVisibility(VISIBLE);
                        statusBtn.setText(mBuild.failBtn);
                    }
                    if (listener != null) {
                        statusBtn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.retry();
                            }
                        });
                    }
                }
                break;
            case STATUS_EMPTY_DATA:
                if (getVisibility() != VISIBLE) {
                    setVisibility(VISIBLE);
                }
                if (mBuild != null) {
                    if (mBuild.emptyIcon != 0) {
                        statusIcon.setVisibility(VISIBLE);
                        statusIcon.setImageResource(mBuild.emptyIcon);
                    } else {
                        statusIcon.setVisibility(GONE);
                    }
                    if (TextUtils.isEmpty(mBuild.emptyText)) {
                        statusDes.setVisibility(GONE);
                    } else {
                        statusDes.setVisibility(VISIBLE);
                        statusDes.setText(mBuild.emptyText);
                    }
                    if (TextUtils.isEmpty(mBuild.emptyBtn)) {
                        statusBtn.setVisibility(GONE);
                    } else {
                        statusBtn.setVisibility(VISIBLE);
                        statusBtn.setText(mBuild.emptyBtn);
                    }
                    if (listener != null) {
                        statusBtn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.retry();
                            }
                        });
                    }
                }
                break;
            case STATUS_LOAD_SUCCESS:
            default:
                if (getVisibility() != GONE) {
                    setVisibility(GONE);
                }
                break;
        }
    }

    private StatusViewListener listener;

    public void setStatusViewListener(StatusViewListener listener) {
        this.listener = listener;
    }

    public interface StatusViewListener {
        void retry();
    }

    public static class Build {

        private int loadingRes;
        private int failIcon;
        private int emptyIcon;
        private String failText;
        private String failBtn;
        private String emptyText;
        private String emptyBtn;
        private int marginTop; //px
        private int marginBottom; //px

        public Build() {
            this.failIcon = R.drawable.net_fail;
            this.failText = "网络连接遇到问题";
            this.failBtn = "重新加载";
            marginTop = 0;
            marginBottom = 0;
        }


        public Build failIcon(int failIcon) {
            this.failIcon = failIcon;
            return this;
        }

        public Build failText(String failText) {
            this.failText = failText;
            return this;
        }

        public Build failBtn(String failBtn) {
            this.failBtn = failBtn;
            return this;
        }

        public Build emptyIcon(int emptyIcon) {
            this.emptyIcon = emptyIcon;
            return this;
        }

        public Build emptyText(String emptyText) {
            this.emptyText = emptyText;
            return this;
        }

        public Build emptyBtn(String emptyBtn) {
            this.emptyBtn = emptyBtn;
            return this;
        }

        public Build marginTop(int marginTop) {
            this.marginTop = marginTop;
            return this;
        }

        public Build marginBottom(int marginBottom) {
            this.marginBottom = marginBottom;
            return this;
        }
    }

}
