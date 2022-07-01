package api.music.download.fragment;

import android.os.Bundle;

import java.util.List;

import api.music.download.bean.MDA_MusicBean;
import api.music.download.channel.mp3juice.Mp3Juice;

public class SearchMp3JuiceFragment extends BaseSearchMusicFragment {

    private String query = "";

    public static SearchMp3JuiceFragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString("query", query);
        SearchMp3JuiceFragment fragment = new SearchMp3JuiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        query = args.getString("query");
        search(query);
    }

    @Override
    protected void downloadMusic(MDA_MusicBean bean) {
        showloading();
        Mp3Juice.getMusicDownUrl(getContext(), bean.id, new Mp3Juice.MusicUrlListener() {
            @Override
            public void onError() {
                dismissloading();
                showShortToast("Error");
            }

            @Override
            public void onSuccess(String url) {
                dismissloading();
                bean.downloadUrl = url;
                String title = bean.title;
                String artistName = bean.artistName;
                String image = bean.image;
                String durationStr = bean.durationstr;
                if (mMusicListener != null) {
                    mMusicListener.download("mp3_juice", title, artistName, url, image, durationStr);
                }
            }
        });
    }

    @Override
    protected void playMusic(MDA_MusicBean bean) {
        showloading();
        Mp3Juice.getMusicDownUrl(getContext(), bean.id, new Mp3Juice.MusicUrlListener() {
            @Override
            public void onError() {
                dismissloading();
                showShortToast("Error");
            }

            @Override
            public void onSuccess(String url) {
                dismissloading();
                bean.downloadUrl = url;
                String title = bean.title;
                String artistName = bean.artistName;
                String image = bean.image;
                if (mMusicListener != null) {
                    mMusicListener.play("mp3_juice", title, artistName, url, image);
                }
            }
        });
    }

    @Override
    public void search(String query) {

        Mp3Juice.search(this.getContext(), query, new Mp3Juice.SearchResultListener() {
            @Override
            public void onError(boolean empty) {

            }

            @Override
            public void onSuccess(List<MDA_MusicBean> musicBeans) {
                dealData(musicBeans);
                if (musicBeans.isEmpty()) {
                    return;
                }

                if (mMusicListener != null) {
                    mMusicListener.eventResult("nhac", query, musicBeans.toString());
                }
                musicListAdapter.setNewData(musicBeans);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.setEnableRefresh(false);
                smartRefreshLayout.setEnableLoadMore(false);
            }
        });


    }

    @Override
    public void loadMore() {

    }

    @Override
    public void refresh() {

    }
}
