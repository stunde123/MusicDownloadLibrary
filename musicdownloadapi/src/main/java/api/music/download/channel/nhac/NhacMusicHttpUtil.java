package api.music.download.channel.nhac;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
class NhacMusicHttpUtil {
    private static OkHttpClient okHttpClient;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
           ;
        okHttpClient = builder.build();
    }

    public static <T> void get(String url, CallBack callBack) {
        Request request = new Request.Builder().get().url(url).build();
        launchRequest(request, callBack);
    }

    public static <T> void post(String url, Map<String, String> body, CallBack callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : body.keySet()) {
            builder.add(key, body.get(key));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().post(requestBody).url(url)
                     .build();
        launchRequest(request, callBack);
    }

    private static void launchRequest(Request request, CallBack callBack) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody requestBody = response.body();
                    String string = requestBody.string();
                    callBack.onSuccess(string);
                } else {
                    callBack.onFail();
                }
            }
        });

    }


}
