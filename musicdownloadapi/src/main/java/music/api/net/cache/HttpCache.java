package music.api.net.cache;

import android.content.Context;

import com.tencent.mmkv.MMKV;

public class HttpCache {

    private static class Inner {
        public static HttpCache mHttpCache = new HttpCache();
    }

    private HttpCache() {
    }

    public static HttpCache getInstance() {
        return Inner.mHttpCache;
    }

    private MMKV mmkv = null;

    public void init(Context context) {
        MMKV.initialize(context);
        mmkv = MMKV.defaultMMKV();
    }

    public void cache(String url, String data) {
        if (mmkv == null) {
            return;
        }
        mmkv.encode(url, data);
    }

    public String getData(String url) {
        if (mmkv == null) {
            return "";
        }
        return mmkv.decodeString(url);
    }


}
