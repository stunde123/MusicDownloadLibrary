package music.api.net.core;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebSettings;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import api.music.download.BuildConfig;
import api.music.download.MusicApi;
import api.music.download.channel.jmd.JamTrack;
import api.music.download.util.Utils;
import music.api.net.cache.HttpCache;
import music.api.net.cookie.PersistentCookieStore;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class JmaApi extends OKHttpCommonApi {
    public static String JAM_URL = "https://www.jamendo.com";

    private Context mContext;

    public JmaApi(Context context) {
        super();
        mContext = context;
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        okHttpClient = builder
                .addInterceptor(sCheckProxy)
                .addInterceptor(sKEY)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cookieJar(new CookieManager(context))
                .build();
    }

    public OkHttpClient client() {
        return okHttpClient;
    }


    private class CookieManager implements CookieJar {
        private final PersistentCookieStore cookieStore;

        public CookieManager(Context context) {
            cookieStore = new PersistentCookieStore(context);
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }


    public final Interceptor sCheckProxy = chain -> {
        Request request = chain.request();
        if (isUseProxy()) {
            return new Response.Builder()
                    .protocol(Protocol.HTTP_1_1)
                    .code(400).request(request)
                    .message("")
                    .body(ResponseBody.create(MediaType.parse("text/plain"), "error"))
                    .build();
        }
        return chain.proceed(request);
    };

    public boolean isUseProxy() {
        if (BuildConfig.DEBUG) {
            return false;
        }
        String httpProxyHost = System.getProperty("http.proxyHost");
        String httpProxyPort = System.getProperty("http.proxyPort");
        int httpPort = Integer.parseInt(httpProxyPort == null ? "-1" : httpProxyPort);

        String httpsProxyHost = System.getProperty("https.proxyHost");
        String httpsProxyPort = System.getProperty("https.proxyPort");
        int httpsPort = Integer.parseInt(httpsProxyPort == null ? "-1" : httpsProxyPort);

        return (!TextUtils.isEmpty(httpProxyHost) && httpPort > 0)
                || (!TextUtils.isEmpty(httpsProxyHost) && httpsPort > 0);
    }

    private Interceptor sKEY = chain -> {
        Request request = chain.request();

        HttpUrl url = request.url();
        if (JAM_URL.contains(url.host())) {
//            int version = MusicApp.config.jamAppVersion;
            int version = 79005380;

            String path = url.encodedPath();
            request = request.newBuilder().header("user-agent",
                    getUserAgent())
                    .header("x-jam-call", getJamCall(path))
                    .header("x-requested-with", "com.jamendo")
                    .header("x-jam-version", Integer.toString(version, 36))
                    .header("sec-fetch-site", "cross-site")
                    .header("sec-fetch-mode", "cors")
                    .build();
            return chain.proceed(request);
        }


        return chain.proceed(request);
    };

    public String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(mContext);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        try {
            StringBuffer sb = new StringBuffer();
            for (int i = 0, length = userAgent.length(); i < length; i++) {
                char c = userAgent.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    sb.append(String.format("\\u%04x", (int) c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A403 Safari/8536.25";
        }
    }

    private String getJamCall(String url) {
        double salt = Math.random();

        return "$" + getSha1(url + salt) + "*" + salt + "~";
    }

    private String getSha1(String str) {

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(Charset.forName("UTF-8")));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

// analysisInterfaceTypeInfo
//    @Override
//    public <T> void get(Context context, String url, Map<String, String> heads, Map<String, String> params, HttpCallBack<T> callback, boolean cache) {
//        final String jointUrl = Utils.jointParams(url, params);
//
//        String cacheContent = HttpCache.getInstance().getData(url);
//        if (cache && !TextUtils.isEmpty(cacheContent)) {
//            String rex = "^\\{.*\\}$";
//            if (Pattern.compile(rex).matcher(cacheContent).find()) {
//                T objResult = (T) gson.fromJson(cacheContent, Utils.analysisInterfaceTypeInfo(callback));
//                callback.onSuccess(objResult);
//            }
//        }
//
//
//        Request.Builder requestBuilder = new Request.Builder().get().url(jointUrl).tag(context);
//        putHeads(requestBuilder, heads);
//
//
//        Request request = requestBuilder.build();
//        Call call = okHttpClient.newCall(request);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                callback.onFailure(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String newContent = response.body().string();
//                    if (TextUtils.isEmpty(newContent)) {
//                        callback.onFailure(new RuntimeException("data error"));
//                        return;
//                    }
//                    String objRex = "^\\{.*\\}$";
//                    String arrRex = "^\\[.*\\]$";
//
//
//                    if (!Pattern.compile(objRex).matcher(newContent).find() && !Pattern.compile(arrRex).matcher(newContent).find()) {
//                        callback.onFailure(new RuntimeException("data error"));
//                        return;
//                    }
//                    if (cache) {
//                        if (newContent.equals(cacheContent)) {
//                            return;
//                        }
//                        HttpCache.getInstance().cache(url, newContent);
//                    }
//                    T objResult = (T) gson.fromJson(newContent, Utils.analysisInterfaceTypeInfo(callback));
//                    callback.onSuccess(objResult);
//                    return;
//                }
//                callback.onFailure(new RuntimeException(response.code() + "-" + response.message()));
//            }
//        });
//    }
}
