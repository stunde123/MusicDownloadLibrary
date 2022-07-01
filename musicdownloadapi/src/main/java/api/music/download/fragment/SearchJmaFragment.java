package api.music.download.fragment;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.List;

import api.music.download.MusicApi;
import api.music.download.bean.MDA_MusicBean;
import music.api.net.core.HttpCallBack;

public class SearchJmaFragment extends BaseSearchMusicFragment {
    @Override
    protected void initData() {
        search(query);
    }

    @Override
    protected void downloadMusic(MDA_MusicBean bean) {
        String title = TextUtils.isEmpty(bean.title) ? "" : bean.title;
        String artistName = TextUtils.isEmpty(bean.artistName) ? "" : bean.artistName;
        String image = TextUtils.isEmpty(bean.image) ? "" : bean.image;
        String durationStr = bean.durationstr;
        String url = bean.downloadUrl;
        if (mMusicListener != null) {
            mMusicListener.download("jma", title, artistName, url, image, durationStr);
        }

    }

    @Override
    protected void playMusic(MDA_MusicBean bean) {
        String title = TextUtils.isEmpty(bean.title) ? "" : bean.title;
        String artistName = TextUtils.isEmpty(bean.artistName) ? "" : bean.artistName;
        String image = TextUtils.isEmpty(bean.image) ? "" : bean.image;
        String durationStr = bean.durationstr;
        String url = bean.downloadUrl;
        if (mMusicListener != null) {
            mMusicListener.play("jma", title, artistName, url, image);
        }

    }

    @Override
    public void search(String query) {
        if (TextUtils.isEmpty(query)) {
            return;
        }
        List list = musicListAdapter.getData();
        if (list != null && !list.isEmpty()) {
            musicListAdapter.getData().clear();
            musicListAdapter.notifyDataSetChanged();
        }
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.autoRefresh();
        MusicApi.getInstance().jmaSearch(this.getContext(), query, new HttpCallBack<List<MDA_MusicBean>>() {
            @Override
            public void onSuccess(List<MDA_MusicBean> musicBeans) {
                dealData(musicBeans);
                if (musicBeans.isEmpty()) {
                    return;
                }

                if (mMusicListener != null) {
                    mMusicListener.eventResult("jma", query, musicBeans.toString());
                }
                musicListAdapter.setNewData(musicBeans);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.setEnableRefresh(false);
                smartRefreshLayout.setEnableLoadMore(false);
            }

            @Override
            public void onFailure(Exception e) {
                smartRefreshLayout.finishRefresh();
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
