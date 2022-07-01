package api.music.download;

import android.content.Context;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.Looper;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.music.download.bean.MDA_MusicBean;
import api.music.download.channel.jmd.JamTrack;
import api.music.download.channel.mp3juice.Mp3JuiceBean;
import api.music.download.channel.mp3juice.Mp3JuiceMusicDetail;
import api.music.download.channel.yt.YTManager;
import api.music.download.fragment.Timeutils;
import api.music.download.util.SafetyUtil;
import music.api.net.cache.HttpCache;
import music.api.net.core.HttpCallBack;
import music.api.net.core.IHttpRequest;
import music.api.net.core.JsoupParse;
import music.api.net.core.OKHttpCommonApi;
import music.api.net.core.JmaApi;

public class MusicApi {

    private final String YOUTUBE_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";
    public Context CONTEXT;
    private Handler H = new Handler(Looper.getMainLooper());
    private IHttpRequest commonHttpRequest = new OKHttpCommonApi();
    private JsoupParse mJsoupParse = new JsoupParse();
    private JmaApi jmaApi;


    private MusicApi() {

    }

    private static MusicApi MUSICAPI;

    public static MusicApi getInstance() {
        if (MUSICAPI == null) {
            MUSICAPI = new MusicApi();
        }
        return MUSICAPI;
    }

    public void init(Context context) {
        CONTEXT = context;
        YTManager.getInstance().init(context, YOUTUBE_USER_AGENT);
        HttpCache.getInstance().init(context);
//        FreeMp3Cloud.init();
        SafetyUtil.init(context);
        jmaApi = new JmaApi(context);
    }


    public void mp3JuiceSearch(Context context, String url, Map<String, String> heads, HttpCallBack<Mp3JuiceBean> httpCallBack) {
        commonHttpRequest.get(context, url, heads, new HashMap<>(), new HttpCallBack<Mp3JuiceBean>() {
            @Override
            public void onSuccess(Mp3JuiceBean result) {
                H.post(() -> httpCallBack.onSuccess(result));
            }

            @Override
            public void onFailure(Exception e) {
                H.post(() -> httpCallBack.onFailure(e));
            }
        }, true);

    }


    public void mp3JuiceDownUrl(Context context, String url, Map<String, String> heads, HttpCallBack<String> httpCallBack) {
        commonHttpRequest.get(context, url, heads, new HashMap<>(), new HttpCallBack<Mp3JuiceMusicDetail>() {
            @Override
            public void onSuccess(Mp3JuiceMusicDetail mp3JuiceMusicDetail) {
                if (mp3JuiceMusicDetail.isSuccessful()) {
                    List<Mp3JuiceMusicDetail.VidInfoBean> vidInfoBeans = mp3JuiceMusicDetail.getVidInfo();
                    if (vidInfoBeans != null && !vidInfoBeans.isEmpty()) {
                        Mp3JuiceMusicDetail.VidInfoBean vidInfoBean = vidInfoBeans.get(0);
                        String url = vidInfoBean.getDloadUrl();
                        H.post(() -> httpCallBack.onSuccess(url));
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                H.post(() -> httpCallBack.onFailure(e));
            }
        }, false);

    }


    public void freeMusicArchive(Context context, String url, HttpCallBack<Element> httpCallBack) {
        mJsoupParse.get(context, url, null, null, new HttpCallBack<Object>() {
            @Override
            public void onSuccess(Object result) {
                Element element = (Element) result;
                httpCallBack.onSuccess(element);
            }

            @Override
            public void onFailure(Exception e) {
                httpCallBack.onFailure(e);
            }
        }, false);

    }

    public void jmaSearch(Context context, String query,  HttpCallBack<List<MDA_MusicBean>> httpCallBack) {

//        Call<List<JamTrack>> search(
//        @Query("query") String query,
//        @Query("type") String type,
//        @Query("limit") int limit,
//        @Query("identities") String identities);
        // query, "track", pageSize, "www"

        String url = JmaApi.JAM_URL + "/api/search?query=" + query + "&type=track&limit=100&identities=www";
        jmaApi.get(context, url, new HashMap<>(), new HashMap<>(), new HttpCallBack<List<JamTrack>>() {
            @Override
            public void onSuccess(List<JamTrack> body) {
                if (body.isEmpty()) {
                    return;
                }

                List<MDA_MusicBean> list = new ArrayList<>();


                for (JamTrack track : body) {
                    if (track.isUnavailable()) {
                        continue;
                    }
                    MDA_MusicBean bean = new MDA_MusicBean();
                    bean.id = Long.toString(track.id);
                    bean.image = track.getCover();
                    bean.listenUrl = track.getStreamUrl();
                    bean.durationstr = Timeutils.formatDuration(track.duration);
                    bean.downloadUrl = track.getStreamUrl();
                    bean.title = track.name;
                    bean.channel = MDA_MusicBean.CHANNEL_JAMENDO;
                    list.add(bean);
                }

                if (list.isEmpty()) {
                    return;
                }
                H.post(() -> httpCallBack.onSuccess(list));
            }

            @Override
            public void onFailure(Exception e) {
                H.post(() -> httpCallBack.onFailure(e));
            }
        }, true);

    }

    public void mp3juicesCCSearch(){
        commonHttpRequest.post();
    }


}
