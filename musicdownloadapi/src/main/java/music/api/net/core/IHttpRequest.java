package music.api.net.core;

import android.content.Context;

import java.util.Map;

public interface IHttpRequest {
    <T> void get(Context context, String url, Map<String, String> heads, Map<String, String> params,
                 final HttpCallBack<T> callback, final boolean cache);

    <T> void post(Context context, String url,Map<String, String> heads,  Map<String, String> params,
                  final HttpCallBack<T> callback, final boolean cache);
}
