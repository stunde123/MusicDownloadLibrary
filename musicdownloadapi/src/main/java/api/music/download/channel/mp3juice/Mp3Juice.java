package api.music.download.channel.mp3juice;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.music.download.bean.MDA_MusicBean;
import api.music.download.MusicApi;
import music.api.net.core.HttpCallBack;

public class Mp3Juice {
    public static void search(Context context, String word, SearchResultListener listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Host", "mp3-juice.com");
        heads.put("Sec-Fetch-Site", "same-origin");
        heads.put("Sec-Fetch-Mode", "cors");
        heads.put("Sec-Fetch-Dest", "empty");
        heads.put("Referer", "https://mp3-juice.com/");
        heads.put("sec-ch-ua-mobile", "?0");
        heads.put("X-Requested-With", "XMLHttpRequest");
        heads.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        heads.put("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");

        String url = "https://mp3-juice.com/api.php?q=" + word;

        MusicApi.getInstance().mp3JuiceSearch(context, url, heads, new HttpCallBack<Mp3JuiceBean>() {
            @Override
            public void onSuccess(Mp3JuiceBean result) {
                List<Mp3JuiceBean.ItemsBean> itemsBeans = result.getItems();

                List<MDA_MusicBean> musicBeans = new ArrayList<>();
                for (Mp3JuiceBean.ItemsBean dataBean : itemsBeans) {
                    MDA_MusicBean bean = new MDA_MusicBean();
                    bean.id = dataBean.getId() + "";
                    bean.channel = MDA_MusicBean.CHANNEL_MP3JUICE;
                    bean.setTitle(dataBean.getTitle());
                    bean.setArtistName(dataBean.getTitle());
                    bean.setImage(dataBean.getThumbHigh());
                    musicBeans.add(bean);
                }
                listener.onSuccess(musicBeans);

            }

            @Override
            public void onFailure(Exception e) {
                listener.onError(true);
            }
        });


    }

    public static void getMusicDownUrl(Context context, String id, MusicUrlListener callBack) {
        String url = "https://api.mp3yt.link/api/json/mp3/" + id;


        Map<String, String> heads = new HashMap<>();
        heads.put("Host", "api.mp3yt.link");
        heads.put("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
        heads.put("sec-ch-ua-mobile", "?0");
        heads.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        heads.put("Origin", "https://mp3-juice.com");
        heads.put("Referer", "https://mp3-juice.com/");
        heads.put("Sec-Fetch-Site", "cross-origin");
        heads.put("Sec-Fetch-Mode", "cors");
        heads.put("Sec-Fetch-Dest", "empty");


        MusicApi.getInstance().mp3JuiceDownUrl(context, url, heads, new HttpCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                callBack.onError();
            }
        });


    }


    public interface SearchResultListener {
        void onError(boolean empty);

        void onSuccess(List<MDA_MusicBean> list);
    }

    public interface MusicUrlListener {
        void onError();

        void onSuccess(String url);
    }
}
