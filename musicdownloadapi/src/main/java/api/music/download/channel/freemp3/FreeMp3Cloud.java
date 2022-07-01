//package api.music.download.channel.freemp3;
//
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import api.music.download.bean.MDA_MusicBean;
//import api.music.download.channel.nhac.NhacMusicListBean;
//
//
//public class FreeMp3Cloud {
//    private static String url = "https://www.freemp3cloud.com/downloader";
//    private static String __RequestVerificationToken = "";
//    private static Handler mHandler = new Handler(Looper.getMainLooper());
//
//    private FreeMp3Cloud() {
//
//    }
//
//    public static void init() {
//        resolveToken();
//    }
//
//    private static void resolveToken() {
//        FreeMp3HttpUtil.initFreemp3(new CallBack<String>() {
//            @Override
//            public void onFail() {
//
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                __RequestVerificationToken = s;
//            }
//        });
//    }
//
//    private static <T> void searchSong(String word, SearchListener listener) {
//        if (TextUtils.isEmpty(__RequestVerificationToken)) {
//            listener.onError(false);
//            return;
//        }
//
//        Map<String, String> map = new HashMap<>();
//        map.put("searchSong", word);
//        map.put("__RequestVerificationToken", __RequestVerificationToken);
//        FreeMp3HttpUtil.post(url, map, new CallBack<String>() {
//            @Override
//            public void onFail() {
//                mHandler.post(() -> listener.onError(false));
//            }
//
//            @Override
//            public void onSuccess(String html) {
//                Document document = Jsoup.parse(html);
//                Element bodyElement = document.body();
//                Element wrapElement = bodyElement.getElementsByClass("wrap").first();
//                Element resultElement = wrapElement.getElementsByClass("s-results").first();
//                Elements playItems = resultElement.getElementsByClass("play-item");
//                List<MDA_MusicBean> musicBeans = new ArrayList<>();
//
//
//                for (Element playItem : playItems) {
//                    Element artistElement = playItem.getElementsByClass("s-artist").first();
//                    Element titleElement = playItem.getElementsByClass("s-title").first();
//                    Element timeElement = playItem.getElementsByClass("s-time-hq").first();
//
//                    Element playElement = playItem.getElementsByClass("play-ctrl").first();
////                    Element downElement = playItem.getElementsByClass("downl").first();
//
//                    String artist = artistElement.text();
//                    String title = titleElement.text();
//                    String time = timeElement.text();
//                    String play = playElement.attr("data-src");
////                    String down = downElement.attr("href");
//
//                    MDA_MusicBean bean = new MDA_MusicBean();
//                    bean.channel = MDA_MusicBean.CHANNEL_FREEMP3;
//                    bean.id = String.valueOf(play.hashCode());
//                    bean.setTitle(title + "-" + artist);
//                    bean.setDownloadUrl(play);
//                    bean.setListenUrl(play);
//                    bean.setDurationstr(time);
//                    musicBeans.add(bean);
//                }
//                mHandler.post(() -> {
//                    if (musicBeans.isEmpty())
//                        listener.onError(true);
//                    else
//                        listener.onSuccess(musicBeans);
//                });
//            }
//        });
//
//
//    }
//
//    public static void search(String word, SearchListener searchListener) {
//        searchSong(word, searchListener);
//    }
//
//
//    public interface SearchListener {
//        void onError(boolean empty);
//
//        void onSuccess(List<MDA_MusicBean> list);
//    }
//
//
//}
