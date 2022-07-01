package api.music.download.fragment;

import android.text.TextUtils;

import java.util.List;
import java.util.PriorityQueue;

import api.music.download.bean.MDA_MusicBean;
import api.music.download.channel.archive.FreeMusicArchive;
import music.api.net.core.HttpCallBack;

public
class SearchFreeMusicArchiveFragment extends BaseSearchMusicFragment {


    @Override
    protected void initData() {
        search(query);
    }

    @Override
    protected void downloadMusic(MDA_MusicBean bean) {
        String title = bean.title;
        String artistName = bean.artistName;
//        String image = bean.image;
        String durationStr = bean.durationstr;
        String url = bean.downloadUrl;
        if (mMusicListener != null) {
            mMusicListener.download("archive_html", title, artistName, url, "", durationStr);
        }


    }

    @Override
    protected void playMusic(MDA_MusicBean bean) {
        String title = bean.title;
        String artistName = bean.artistName;
        String image = bean.image;
        String url = bean.downloadUrl;
        if (mMusicListener != null) {
            mMusicListener.play("archive_html", title, artistName, url, "");
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
        smartRefreshLayout.autoRefresh();
        FreeMusicArchive.search(this.getContext(), query, new HttpCallBack<List<MDA_MusicBean>>() {
            @Override
            public void onSuccess(List<MDA_MusicBean> musicBeans) {
                dealData(musicBeans);
                smartRefreshLayout.post(() -> post(musicBeans));
            }

            @Override
            public void onFailure(Exception e) {
                smartRefreshLayout.post(() -> smartRefreshLayout.finishRefresh());
            }
        });


    }

    private void post(List<MDA_MusicBean> musicBeans) {
        if (musicBeans.isEmpty()) {
            return;
        }

        if (mMusicListener != null) {
            mMusicListener.eventResult("archive", query, musicBeans.toString());
        }
        musicListAdapter.setNewData(musicBeans);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);
    }


    @Override
    public void loadMore() {

    }

    @Override
    public void refresh() {

    }
}
