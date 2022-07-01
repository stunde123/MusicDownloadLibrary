package api.music.download.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import api.music.download.bean.MDA_MusicBean;

public class SearchFreeMp3Fragment extends BaseSearchMusicFragment {

    public static SearchFreeMp3Fragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString("query", query);
        SearchFreeMp3Fragment fragment = new SearchFreeMp3Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    private String query = "";

    @Override
    protected void initData() {
        Bundle args = getArguments();
        query = args.getString("query");
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);
        search(query);
    }

    @Override
    protected void downloadMusic(MDA_MusicBean bean) {
        if (TextUtils.isEmpty(bean.downloadUrl)) {
            return;
        }
        String title = bean.title;
        String artistName = bean.artistName;
        String image = bean.image;
        String durationstr = bean.durationstr;
        if (mMusicListener != null) {
            mMusicListener.download("freemp3", title, artistName, bean.downloadUrl, image, durationstr);
        }
    }

    @Override
    protected void playMusic(MDA_MusicBean bean) {
        String url = bean.downloadUrl;
        String title = bean.title;
        String artistName = bean.artistName;
        String image = bean.image;
        if (mMusicListener != null) {
            mMusicListener.play("freemp3", title, artistName, url, image);
        }
    }

    @Override
    public void search(String query) {
//        FreeMp3Cloud.search(query, new FreeMp3Cloud.SearchListener() {
//            @Override
//            public void onError(boolean empty) {
//
//            }
//
//            @Override
//            public void onSuccess(List<MDA_MusicBean> musicBeans) {
//
//                if (musicBeans == null || musicBeans.isEmpty()) {
//                    return;
//                }
//                dealData(musicBeans);
//                if (musicBeans.isEmpty()) {
//                    return;
//                }
//                musicListAdapter.setNewData(musicBeans);
//            }
//        });


    }

    @Override
    public void loadMore() {

    }

    @Override
    public void refresh() {

    }
}
