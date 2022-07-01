package music.api.net.core;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import api.music.download.util.Utils;
import music.api.net.cache.HttpCache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpCommonApi implements IHttpRequest {
    protected OkHttpClient okHttpClient;
    protected Gson gson = new Gson();

    public OKHttpCommonApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS);

        okHttpClient = builder.build();

    }

    @Override
    public <T> void get(Context context, String url, Map<String, String> heads, Map<String, String> params, HttpCallBack<T> callback, boolean cache) {
        final String jointUrl = Utils.jointParams(url, params);

        String cacheContent = HttpCache.getInstance().getData(url);
        if (cache && !TextUtils.isEmpty(cacheContent)) {
            String rex = "^\\{.*\\}$|^\\[.*\\]$";
            if (Pattern.compile(rex).matcher(cacheContent).find()) {
                T objResult = (T) gson.fromJson(cacheContent, Utils.analysisInterfaceTypeInfo(callback));
                callback.onSuccess(objResult);
            }
        }


        Request.Builder requestBuilder = new Request.Builder().get().url(jointUrl).tag(context);
        putHeads(requestBuilder, heads);

        Request request = requestBuilder.build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String newContent = response.body().string();
                    if (TextUtils.isEmpty(newContent)) {
                        callback.onFailure(new RuntimeException("data error"));
                        return;
                    }
                    String rex = "^\\{.*\\}$|^\\[.*\\]$";
                    if (!Pattern.compile(rex).matcher(newContent).find()) {
                        callback.onFailure(new RuntimeException("data error"));
                        return;
                    }
                    if (cache) {
                        if (newContent.equals(cacheContent)) {
                            return;
                        }
                        HttpCache.getInstance().cache(url, newContent);
                    }
                    T objResult = (T) gson.fromJson(newContent, Utils.analysisInterfaceTypeInfo(callback));
                    callback.onSuccess(objResult);
                    return;
                }
                callback.onFailure(new RuntimeException(response.code() + "-" + response.message()));
            }
        });
    }

    public void putHeads(Request.Builder requestBuilder, Map<String, String> heads) {
        if (heads != null && !heads.isEmpty()) {
            for (String key : heads.keySet()) {
                requestBuilder.addHeader(key, heads.get(key));
            }
        }
    }

    @Override
    public <T> void post(Context context, String url, Map<String, String> heads, Map<String, String> params, HttpCallBack<T> callback, boolean cache) {

        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBuilder.add(key, params.get(key));
        }
        RequestBody requestBody = formBuilder.build();
        Request.Builder requestBuilder = new Request.Builder().post(requestBody).url(url).tag(context);

        putHeads(requestBuilder, heads);

        if (cache) {
            final String jointUrl = Utils.jointParams(url, params);
            String content = HttpCache.getInstance().getData(jointUrl);
            if (!TextUtils.isEmpty(content)) {
                T objResult = (T) gson.fromJson(content, Utils.analysisInterfaceTypeInfo(callback));
                callback.onSuccess(objResult);
            }
        }


        Request request = requestBuilder.build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String content = response.body().string();

                    if (cache) {
                        HttpCache.getInstance().cache(url, content);
                    }

                    T objResult = (T) gson.fromJson(content, Utils.analysisInterfaceTypeInfo(callback));
                    callback.onSuccess(objResult);
                    return;
                }
                callback.onFailure(new RuntimeException(response.code() + "-" + response.message()));

            }
        });


    }
}
