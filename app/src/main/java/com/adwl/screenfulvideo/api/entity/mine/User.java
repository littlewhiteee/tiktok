package com.adwl.screenfulvideo.api.entity.mine;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * created by wmm on 2020/10/10
 */
public class User implements Parcelable {
//    "id": 200000302,
//            "name": "游客_94S78888",
//            "gender": 1,
//            "reg_time": 1598864777,
//            "sign": "这个人很懒，什么也没留下",
//            "fans": 0,
//            "like": 0,
//            "follow": 0,
//            "avatar": "http://img.pipidexia.com/uploads/images/user/0a3ef4bdb4e1a30f1c5ecc2ba90361ce.jpg",
//            "cover": "",
//            "token": "6T8JQC2F0DJ351SCY6K2ZMFRFHOFRMGMUVYNHHPDLOSMWNEG1P7V77JN996MC9KI4A5406D63UHRMLBJXJXB9T6V2SRIRZYXIRDXG9BG799N45MHDFL4BGHLSLT1Y90B",
//            "has_pwd": 1,
//            "is_follow": 0

//    "id": 8146717,
//            "name": "游客",
//            "gender": 0,
//            "reg_time": 0,
//            "sign": "",
//            "fans": 0,
//            "like": 27708,
//            "follow": 0,
//            "avatar": "http://img.pipidexia.com/uploads/images/user/default.png",
//            "cover": "",
//            "token": "",
//            "has_pwd": 0,
//            "is_follow": 0

    public int id;
    public String name;
    public int gender;
    @SerializedName("reg_time")
    public int regTime;
    public String sign;
    public int fans;
    public int like;
    public int follow;
    public String avatar;
    public String cover;
    public String token;
    @SerializedName("has_pwd")
    public boolean hasPwd;
    @SerializedName("is_follow")
    public boolean isFollow;


    public String getSign() {
        if (TextUtils.isEmpty(sign)) {
            return "这个人很懒，什么也没留下";
        }
        return sign;
    }

    public String getSexStr() {
        if (gender == 2) {
            return "小姐姐";
        }
        if (gender == 1) {
            return "小哥哥";
        }
        return "外星人";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.gender);
        dest.writeInt(this.regTime);
        dest.writeString(this.sign);
        dest.writeInt(this.fans);
        dest.writeString(this.avatar);
        dest.writeString(this.cover);
        dest.writeString(this.token);
        dest.writeByte(this.hasPwd ? (byte) 1 : (byte) 0);
        dest.writeInt(this.like);
        dest.writeInt(this.follow);
        dest.writeByte(this.isFollow ? (byte) 1 : (byte) 0);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.gender = in.readInt();
        this.regTime = in.readInt();
        this.sign = in.readString();
        this.fans = in.readInt();
        this.avatar = in.readString();
        this.cover = in.readString();
        this.token = in.readString();
        this.hasPwd = in.readByte() != 0;
        this.like = in.readInt();
        this.follow = in.readInt();
        this.isFollow = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


}
