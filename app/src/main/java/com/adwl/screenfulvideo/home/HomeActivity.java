package com.adwl.screenfulvideo.home;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.adwl.screenfulvideo.R;
import com.adwl.screenfulvideo.base.TopbarBaseActivity;
import com.adwl.screenfulvideo.home.fragment.HomeFragment;
import com.adwl.screenfulvideo.mine.MineFragment;
import com.adwl.screenfulvideo.widget.FragmentTabHost;

import java.util.Locale;

/**
 * created by wmm on 2020/11/11
 */
public class HomeActivity extends TopbarBaseActivity {
    private FragmentTabHost mTabHost;

    private FragmentManager fragmentManager;

    private int currentTab;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initTopBar() {
        hideTopbar();
    }

    private static final int TAB_HOME_ORDER = 0;
    private static final int TAB_MINE_ORDER = 1;

    public enum HomeTab {
        HOME(TAB_HOME_ORDER, R.string.tab_home, R.drawable.selector_tab_home, HomeFragment.class),
        USER(TAB_MINE_ORDER, R.string.tab_user, R.drawable.selector_tab_user, MineFragment.class);

        private int order;
        private int nameRes;
        private int iconRes;
        private Class<?> clazz;

        HomeTab(int order, int nameRes, int iconRes, Class<?> clazz) {
            this.order = order;
            this.nameRes = nameRes;
            this.iconRes = iconRes;
            this.clazz = clazz;
        }

        public int getOrder() {
            return order;
        }

        public int getNameRes() {
            return nameRes;
        }

        public int getIconRes() {
            return iconRes;
        }

        public Class<?> getClazz() {
            return clazz;
        }
    }


    private void initViews() {
        initTopBar();
        fragmentManager = getSupportFragmentManager();
        mTabHost = findViewById(android.R.id.tabhost);

        mTabHost.setup(this, fragmentManager, R.id.content_layout);
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        initTabs();
        currentTab = HomeTab.HOME.getOrder();
        mTabHost.setCurrentTab(currentTab);

        mTabHost.getTabWidget().getChildAt(TAB_HOME_ORDER).setOnClickListener(v -> {
            toHome();
        });

        mTabHost.getTabWidget().getChildAt(TAB_MINE_ORDER).setOnClickListener(v -> {
            toMine();

        });
    }

    private void toMine() {
        if (currentTab == HomeTab.USER.getOrder()) {
//            String tag = mTabHost.getCurrentTabTag();
//            Fragment fragment = fragmentManager.findFragmentByTag(tag);
//            if (fragment instanceof UserCenterFragment) {
//                ((UserCenterFragment) fragment).scrollToFirstPosition();
//                return;
//            }
            return;
        }
        currentTab = HomeTab.USER.getOrder();
        mTabHost.setCurrentTab(currentTab);
    }

    private void toHome() {
        if (currentTab == HomeTab.HOME.getOrder()) {
            return;
        }
        currentTab = HomeTab.HOME.getOrder();
        mTabHost.setCurrentTab(currentTab);

    }


    private void initTabs() {
        for (HomeTab homeTab : HomeTab.values()) {
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(homeTab.getNameRes()));

            View indicator = LayoutInflater.from(this).inflate(R.layout.home_tab_indicator, mTabHost.getTabWidget(), false);
            ImageView tabIcon = indicator.findViewById(R.id.tab_image);

            TextView tabText = indicator.findViewById(R.id.tab_text);



            tabText.setText(homeTab.nameRes);

            tabIcon.setImageResource(homeTab.getIconRes());

            tab.setIndicator(indicator);

            tab.setContent(tag -> new View(HomeActivity.this));

            mTabHost.addTab(tab, homeTab.getClazz(), null);
        }
    }

    long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, String.format(Locale.CHINA, "再按一次退出%s", getString(R.string.app_name)), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
