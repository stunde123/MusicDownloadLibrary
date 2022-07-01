package api.music.download.channel.yt;

import android.content.Context;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.ServiceList;

import api.music.download.Constants;

/**
 * 1.导入aar
 * 2.applicaion调用init
 * 3.使用的地方调用search和getAudios接口
 */
public class YTManager {
    private static YTManager instance = null;
    private YTSearch mYTSearch;
    private YTGetAudios mYTGetAudios;

    private YTManager() {
        mYTSearch = new YTSearch();
        mYTGetAudios = new YTGetAudios();
    }

    public static YTManager getInstance() {
        if (null == instance) {
            synchronized (YTManager.class) {
                if (null == instance) {
                    instance = new YTManager();
                }
            }
        }
        return instance;
    }

    public void setUa(String ua) {
        DownloaderImpl.getInstance().setUa(ua);
    }

    public void init(Context context, String ua) {
        NewPipe.init(DownloaderImpl.init(null, ua),
                Constants.getPreferredLocalization(context),
                Constants.getPreferredContentCountry(context));
    }

    public void searchYT(String query, YTSearch.onSearchListener listener) {
        mYTSearch.search(ServiceList.YouTube.getServiceId(), query, listener);
    }

    public void searchMoreYT(String query, YTSearch.onSearchListener listener) {
        mYTSearch.searchMore(ServiceList.YouTube.getServiceId(), query, listener);
    }

    public void getAudiosYT(String url, YTGetAudios.onAudiosListener listener) {
        mYTGetAudios.getAudios(ServiceList.YouTube.getServiceId(), url, listener);
    }

//    public void searchSound(String query, YTSearch.onSearchListener listener) {
//        new YTSearch().search(ServiceList.SoundCloud.getServiceId(), query, listener);
//    }
//
//    public void getAudiosSound(String url, YTGetAudios.onAudiosListener listener) {
//        new YTGetAudios().getAudios(ServiceList.SoundCloud.getServiceId(), url, listener);
//    }

}
