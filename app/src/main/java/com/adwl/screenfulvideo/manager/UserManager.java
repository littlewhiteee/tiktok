package com.adwl.screenfulvideo.manager;

import com.adwl.screenfulvideo.api.entity.mine.User;
import com.adwl.screenfulvideo.utils.EmptyUtils;
import com.adwl.screenfulvideo.utils.Remember;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * created by wmm on 2020/9/8
 */
public class UserManager {
    private static final String PREF_KEY_USER = "_n_a_";
    private static UserManager mInstance;

    private User mUser;
    private Gson mGson;

    public static UserManager get() {
        if (mInstance == null) {
            mInstance = new UserManager();
        }
        return mInstance;
    }

    private UserManager() {
        init();
    }

    private void init() {
        mGson = new Gson();
    }

    public User getUser() {
        if (mUser == null) {
            String json = "";
            String userJson = Remember.getString(PREF_KEY_USER, "");
            if (!EmptyUtils.isEmpty(userJson)) {
                json = userJson;
            }
            mUser = mGson.fromJson(json, User.class);
        }
        return mUser;
    }

    public boolean isMineByUserId(int userId) {
        return getUser() != null && getUser().id == userId;
    }

    public String getToken() {
        User user = getUser();
        if (user == null || EmptyUtils.isEmpty(user.token)) {
            return "";
        }
        return String.format(Locale.CHINA, "Bearer %s", user.token);
    }


    /**
     * 更新User
     *
     * @param user user
     */
    public void updateUser(User user) {
        if (user != null) {
            mUser = user;
            String authJson = mGson.toJson(mUser);
            Remember.putString(PREF_KEY_USER, authJson);
        }
    }

    public void clear() {
        Remember.remove(PREF_KEY_USER);
        mUser = null;
    }

    /**
     * 用户登录
     */
    public boolean isLogin() {
        User user = getUser();
        if (user == null) {
            return false;
        }

        if (EmptyUtils.isEmpty(getToken())) {
            return false;
        }

        return !EmptyUtils.isEmpty(getToken()) && getUser() != null;
    }


    private List<OnUserChangeListener> onUserChangeListeners;

    public void addOnUserChangeListener(OnUserChangeListener listener) {
        if (onUserChangeListeners == null) {
            onUserChangeListeners = new ArrayList<>();
        }
        onUserChangeListeners.add(listener);
    }

    public void removeOnUserChangeListener(OnUserChangeListener listener) {
        if (onUserChangeListeners == null || onUserChangeListeners.isEmpty()) {
            return;
        }
        onUserChangeListeners.remove(listener);
    }

    public interface OnUserChangeListener {
        void onUserChange(User user);
    }
}
