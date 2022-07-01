package music.api.net.core;

import android.content.Context;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import music.api.net.cache.HttpCache;

public class JsoupParse implements IHttpRequest {
    @Override
    public <T> void get(Context context, String url, Map<String, String> heads, Map<String, String> params, HttpCallBack<T> callback, boolean cache) {
        String cacheContent = HttpCache.getInstance().getData(url);
        if (cache && !TextUtils.isEmpty(cacheContent)) {
            if (cacheContent.contains("box-stnd-10pad-20marg play-lrg-list")) {
                Element element = new Element(cacheContent);
                callback.onSuccess((T) element);
            }
        }

        Observable.just(url).map(url1 -> {
            Document document = Jsoup.connect(url1).get();
            Element element = document.body();
            return element;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(element -> {
                            String newContent = element.toString();
                            if (TextUtils.isEmpty(newContent)) {
                                callback.onFailure(new RuntimeException("data error"));
                                return;
                            }

//                            if (!newContent.contains("box-stnd-10pad-20marg play-lrg-list")) {
//                                callback.onFailure(new RuntimeException("data Error"));
//                             return;
//                            }

                            if (cache) {
                                if (newContent.equals(cacheContent)) {
                                    return;
                                }
                                HttpCache.getInstance().cache(url, newContent);
                            }

                            callback.onSuccess((T) element);
                        },
                        throwable -> callback.onFailure(new RuntimeException(throwable.toString())));
    }

    @Override
    public <T> void post(Context context, String url, Map<String, String> heads, Map<String, String> params, HttpCallBack<T> callback, boolean cache) {

    }
}
