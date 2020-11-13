package com.adwl.screenfulvideo.api.entity.home;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.IntDef;

import com.adwl.screenfulvideo.api.entity.mine.User;
import com.adwl.screenfulvideo.manager.UserManager;
import com.adwl.screenfulvideo.utils.EmptyUtils;
import com.adwl.screenfulvideo.utils.NumUtils;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * created by wmm on 2020/10/19
 */
public class Joke implements Parcelable{

    public int id;
    public User user;
    @SerializedName("create_time")
    public int createTime;
    public String content;
    public int down;
    public int up;
    public int likes;
    public int reviews;
    public int share;
    public List<Imgs> imgs;
    @SerializedName("is_like")
    public boolean isCollect;
    @SerializedName("is_up")
    public boolean isUp;
    @SerializedName("is_down")
    public boolean isDown;
    public int status;
    public ShortVideo video;


    public String getStatusStr() {
        switch (status) {
            case 1:
                return "审核中";
            case 2:
                return "发布失败";
            default:
                return "发布成功";
        }
    }

    public boolean isShowJokeStatus() {
        return UserManager.get().getUser() != null &&
                UserManager.get().getUser().id == user.id &&
                (status == 1 || status == 2);
    }

    //审核中
    public boolean isIssuing() {
        return status == 1;
    }

    //发布失败
    public boolean isIssueFail() {
        return status == 2;
    }



    // 评论数
    public String getCommentCountStr() {
        if (reviews > 10000) {
            return String.format(Locale.CHINA, "%sw", NumUtils.formatNum(reviews / 10000f));
        }
        return String.valueOf(Math.max(reviews, 0));
    }

    // 收藏数
    public String getCollectCountStr() {
        if (likes > 10000) {
            return String.format(Locale.CHINA, "%sw", NumUtils.formatNum(likes / 10000f));
        }
        return String.valueOf(Math.max(likes, 0));
    }

    // 分享数
    public String getShareCountStr() {
        if (share > 10000) {
            return String.format(Locale.CHINA, "%sw", NumUtils.formatNum(share / 10000f));
        }
        return String.valueOf(Math.max(share, 0));
    }

    //up数
    public String getUpCountStr() {
        if (up > 10000) {
            return String.format(Locale.CHINA, "%sw", NumUtils.formatNum(up / 10000f));
        }
        return String.valueOf(Math.max(up, 0));
    }

    //down数
    public String getDownCountStr() {
        if (down > 10000) {
            return String.format(Locale.CHINA, "%sw", NumUtils.formatNum(down / 10000f));
        }
        return String.valueOf(Math.max(down, 0));
    }

    public String getUserName() {
        if (user == null || TextUtils.isEmpty(user.name)) {
            return "";
        }
        return user.name;
    }

    public String getContent() {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        return content;
    }

    public boolean isPicEmpty() {
        return getImageLength() < 1;
    }

    public boolean isSinglePic() {
        return getImageLength() == 1;
    }

    public boolean isMultiPic() {
        return getImageLength() > 1;
    }

    public Imgs getSingleImg() {
        if (EmptyUtils.isEmpty(imgs)) {
            return null;
        }
        return imgs.get(0);
    }

    public int getSingThumbImgWidth() {
        if (EmptyUtils.isEmpty(imgs)) {
            return 0;
        }

        if (imgs.get(0).urls != null && imgs.get(0).urls.origin != null) {
            return imgs.get(0).urls.thumb.w;
        }

        try {
            return Integer.parseInt(imgs.get(0).w);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getSingThumbImgHeight() {
        if (EmptyUtils.isEmpty(imgs)) {
            return 0;
        }
        if (imgs.get(0).urls != null && imgs.get(0).urls.origin != null) {
            return imgs.get(0).urls.thumb.h;
        }
        try {
            return Integer.parseInt(imgs.get(0).h);
        } catch (Exception e) {
            return 0;
        }
    }


    public boolean isGif() {
        return imgs.get(0).isGif();
    }

    public int getImageLength() {
        if (imgs == null || imgs.isEmpty()) {
            return 0;
        }
        return imgs.size();
    }

    public String getAuthorAvatar() {
        if (user == null || TextUtils.isEmpty(user.avatar)) {
            return "";
        }
        return user.avatar;
    }

    public boolean isVideo() {
        return video != null && !TextUtils.isEmpty(video.url);
    }

    public static final int JOKE_TEXT = 0; // 纯文本
    public static final int JOKE_SINGLE_PIC = 1; // 单图片
    public static final int JOKE_MULTI_PIC = 2; // 多图片
    public static final int JOKE_VIDEO = 3; // 视频



    @Retention(RetentionPolicy.SOURCE)
    @IntDef({JOKE_TEXT, JOKE_SINGLE_PIC, JOKE_MULTI_PIC, JOKE_VIDEO})
    @interface JokeDateType {
    }

    public static class Imgs implements Parcelable {
        public int id;
        public String fmt;
        public String w;
        public String h;
        public Urls urls;

        public boolean isGif() {
            return TextUtils.equals("gif", fmt);
        }

        public boolean isBigPic() {
            return urls.origin.h / urls.origin.w > 2;
        }


        public Img getThumbImg() {
            return urls.thumb;
        }

        public Img getOriginImg() {
            return urls.origin;
        }


        public boolean isThumbBigPic() {
            return urls.thumb.h / urls.thumb.w > 2;
        }

        public String getOriginUrl() {
            if (urls == null ||
                    urls.origin == null ||
                    EmptyUtils.isEmpty(urls.origin.urls)) {
                return "";
            }
            return urls.origin.urls.get(0);
        }


        public String getThumbUrl() {
            if (urls == null ||
                    urls.thumb == null ||
                    EmptyUtils.isEmpty(urls.thumb.urls)) {
                return "";
            }
            return urls.thumb.urls.get(0);
        }

        public double getThumbRatio() {
            if (urls == null ||
                    urls.thumb == null ||
                    EmptyUtils.isEmpty(urls.thumb.urls)) {
                return 1;
            }
            return urls.thumb.w / urls.thumb.h;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.fmt);
            dest.writeString(this.w);
            dest.writeString(this.h);
            dest.writeParcelable(this.urls, flags);
        }

        public Imgs() {
        }

        protected Imgs(Parcel in) {
            this.id = in.readInt();
            this.fmt = in.readString();
            this.w = in.readString();
            this.h = in.readString();
            this.urls = in.readParcelable(Urls.class.getClassLoader());
        }

        public static final Creator<Imgs> CREATOR = new Creator<Imgs>() {
            @Override
            public Imgs createFromParcel(Parcel source) {
                return new Imgs(source);
            }

            @Override
            public Imgs[] newArray(int size) {
                return new Imgs[size];
            }
        };
    }


    public static class Urls implements Parcelable {
        public Img origin;
        public Img thumb;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.origin, flags);
            dest.writeParcelable(this.thumb, flags);
        }

        public Urls() {
        }

        protected Urls(Parcel in) {
            this.origin = in.readParcelable(Img.class.getClassLoader());
            this.thumb = in.readParcelable(Img.class.getClassLoader());
        }

        public static final Creator<Urls> CREATOR = new Creator<Urls>() {
            @Override
            public Urls createFromParcel(Parcel source) {
                return new Urls(source);
            }

            @Override
            public Urls[] newArray(int size) {
                return new Urls[size];
            }
        };
    }

    public static class Img implements Parcelable {
        public int h;
        public List<String> urls;
        public int w;

        public int compareWH() {
            return w - h;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.h);
            dest.writeStringList(this.urls);
            dest.writeInt(this.w);
        }

        public Img() {
        }

        protected Img(Parcel in) {
            this.h = in.readInt();
            this.urls = in.createStringArrayList();
            this.w = in.readInt();
        }

        public static final Creator<Img> CREATOR = new Creator<Img>() {
            @Override
            public Img createFromParcel(Parcel source) {
                return new Img(source);
            }

            @Override
            public Img[] newArray(int size) {
                return new Img[size];
            }
        };

    }

    public static class ShortVideo implements Parcelable {
        //        "id": 491001,
//                "url": "http://app.funny.clply.cn/03984acb1f1afe7c023c3a478d19fb0f.mp4",
//                "cover": "http://app.funny.clply.cn/03984acb1f1afe7c023c3a478d19fb0f.jpg",
//                "w": "720",
//                "h": "1280",
//                "filesize": "0",
//                "duration": "10"
        public int id;
        public String url;
        public String cover;
        public String w;
        public String h;
        public String filesize;
        public String duration;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.url);
            dest.writeString(this.cover);
            dest.writeString(this.w);
            dest.writeString(this.h);
            dest.writeString(this.filesize);
            dest.writeString(this.duration);
        }

        public ShortVideo() {
        }

        protected ShortVideo(Parcel in) {
            this.id = in.readInt();
            this.url = in.readString();
            this.cover = in.readString();
            this.w = in.readString();
            this.h = in.readString();
            this.filesize = in.readString();
            this.duration = in.readString();
        }

        public static final Creator<ShortVideo> CREATOR = new Creator<ShortVideo>() {
            @Override
            public ShortVideo createFromParcel(Parcel source) {
                return new ShortVideo(source);
            }

            @Override
            public ShortVideo[] newArray(int size) {
                return new ShortVideo[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.createTime);
        dest.writeString(this.content);
        dest.writeInt(this.down);
        dest.writeInt(this.up);
        dest.writeInt(this.likes);
        dest.writeInt(this.reviews);
        dest.writeInt(this.share);
        dest.writeList(this.imgs);
        dest.writeByte(this.isCollect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isUp ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDown ? (byte) 1 : (byte) 0);
        dest.writeInt(this.status);
        dest.writeParcelable(this.video, flags);
    }

    public Joke() {
    }

    protected Joke(Parcel in) {
        this.id = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createTime = in.readInt();
        this.content = in.readString();
        this.down = in.readInt();
        this.up = in.readInt();
        this.likes = in.readInt();
        this.reviews = in.readInt();
        this.share = in.readInt();
        this.imgs = new ArrayList<Imgs>();
        in.readList(this.imgs, Imgs.class.getClassLoader());
        this.isCollect = in.readByte() != 0;
        this.isUp = in.readByte() != 0;
        this.isDown = in.readByte() != 0;
        this.status = in.readInt();
        this.video = in.readParcelable(ShortVideo.class.getClassLoader());
    }

    public static final Creator<Joke> CREATOR = new Creator<Joke>() {
        @Override
        public Joke createFromParcel(Parcel source) {
            return new Joke(source);
        }

        @Override
        public Joke[] newArray(int size) {
            return new Joke[size];
        }
    };
}
