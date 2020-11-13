package com.adwl.screenfulvideo.api.base;


import com.adwl.screenfulvideo.BuildConfig;
import com.adwl.screenfulvideo.api.UserAgentManager;
import com.adwl.screenfulvideo.api.constants.ApiHost;
import com.adwl.screenfulvideo.api.interceptor.HeaderInterceptor;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * created by wmm on 2020/9/8
 */
public class BaseApi {
    public static final String K_UA = "ua";

    private static final int TIMEOUT_DEFAULT = 30 * 1000; // 30秒
    private static final int TIMEOUT_CONNECT = TIMEOUT_DEFAULT;
    private static final int TIMEOUT_READ = TIMEOUT_DEFAULT;
    private static final int TIMEOUT_WRITE = TIMEOUT_DEFAULT;


    protected static Retrofit retrofit() {
        return gsonRetrofitBuilder().build();
    }

    protected static Retrofit stringRetrofit() {
        return stringRetrofitBuilder().build();
    }

    protected static Retrofit.Builder retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(ApiHost.API_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClientBuilder().build());
    }

    protected static Retrofit.Builder gsonRetrofitBuilder() {
        return retrofitBuilder().addConverterFactory(GsonConverterFactory.create(gsonBuilder().create()));
    }

    protected static Retrofit.Builder stringRetrofitBuilder() {
        return retrofitBuilder().addConverterFactory(new StringConverterFactory());
    }


    protected static OkHttpClient.Builder okHttpClientBuilder() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new HeaderInterceptor(getUA()));
    }

    public static GsonBuilder gsonBuilder() {
        TypeAdapter booleanAsIntAdapter = new BooleanAsIntAdapter();
        return new GsonBuilder().registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .setDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    private static String getUA() {
        return UserAgentManager.getUserAgent();
    }

    public static class BooleanAsIntAdapter extends TypeAdapter<Boolean> {

        @Override
        public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }

        @Override
        public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return Boolean.parseBoolean(in.nextString());
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }
    }
}
