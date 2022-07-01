package api.music.download.channel.archive;

import android.content.Context;

import org.jsoup.nodes.Element;

import java.util.List;

import api.music.download.bean.MDA_MusicBean;
import api.music.download.MusicApi;
import music.api.net.core.HttpCallBack;

public class FreeMusicArchive {


    public static void search(Context context, String query, HttpCallBack<List<MDA_MusicBean>> callBack) {
        String url = "https://freemusicarchive.org/search?quicksearch=" + query + "&pageSize=200";
        MusicApi.getInstance().freeMusicArchive(context, url, new HttpCallBack<Element>() {
            @Override
            public void onSuccess(Element result) {
                try {
                    List<MDA_MusicBean> list = ParseHtml.parse(result);
                    callBack.onSuccess(list);
                    return;
                } catch (Exception e) {
                    System.out.println("---e " + e.toString());
                }
                callBack.onFailure(new RuntimeException("parse error"));
            }

            @Override
            public void onFailure(Exception e) {
                callBack.onFailure(e);
            }
        });
    }

}
