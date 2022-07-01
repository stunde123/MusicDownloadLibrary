package api.music.download.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import api.music.download.bean.MDA_MusicBean;
import api.music.download.channel.nhac.CallBack;
import api.music.download.channel.nhac.NhacMusic;
import api.music.download.channel.nhac.NhacMusicListBean;

public class SearchNhacFragment extends BaseSearchMusicFragment {

    @Override
    protected void initData() {
        search(query);
    }

    int index = 1;

    private void loadMusic() {
        NhacMusic.search(query, index, new CallBack<List<NhacMusicListBean.DataBean>>() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(List<NhacMusicListBean.DataBean> dataBeans) {

                List<MDA_MusicBean> musicBeans = new ArrayList<>();
                for (NhacMusicListBean.DataBean dataBean : dataBeans) {
                    MDA_MusicBean bean = new MDA_MusicBean();
                    bean.id = dataBean.getId() + "";
                    bean.channel = MDA_MusicBean.CHANNEL_NHAC;
                    bean.setTitle(dataBean.getTitle());
                    bean.setArtistName(dataBean.getArtist_title());
                    bean.setImage(dataBean.getImage_url());
                    musicBeans.add(bean);
                }

                dealData(musicBeans);
                if (musicBeans.isEmpty()) {
                    return;
                }

                if (mMusicListener != null) {
                    mMusicListener.eventResult("nhac", query, musicBeans.toString());
                }


                List<MDA_MusicBean> musicListAdapterData = musicListAdapter.getData();
                if (index == 1 || musicListAdapterData == null || musicListAdapterData.isEmpty()) {
                    musicListAdapter.setNewData(musicBeans);
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.setEnableRefresh(false);
                } else {
                    musicListAdapter.addData(musicBeans);
                    smartRefreshLayout.finishLoadMore();
                }
                index++;
            }
        });
    }


    @Override
    protected void downloadMusic(MDA_MusicBean bean) {
        if (!TextUtils.isEmpty(bean.downloadUrl)) {
            String title = bean.title;
            String artistName = bean.artistName;
            String image = bean.image;
            String durationStr = bean.durationstr;
            if (mMusicListener != null) {
                mMusicListener.download("nhac", title, artistName, bean.downloadUrl, image, durationStr);
            }
            return;
        }

        String id = bean.id;
        showloading();
        NhacMusic.getSongInfo(id, new CallBack<String>() {
            @Override
            public void onFail() {
                dismissloading();
                showShortToast("Error");
            }

            @Override
            public void onSuccess(String downurl) {
                dismissloading();
                bean.downloadUrl = downurl;
                String title = bean.title;
                String artistName = bean.artistName;
                String image = bean.image;
                String durationStr = bean.durationstr;
                if (mMusicListener != null) {
                    mMusicListener.download("nhac", title, artistName, downurl, image, durationStr);
                }

            }
        });
    }

    @Override
    protected void playMusic(MDA_MusicBean bean) {
        String id = bean.id;
        showloading();
        NhacMusic.getSongInfo(id, new CallBack<String>() {
            @Override
            public void onFail() {
                dismissloading();
                showShortToast("Error");
            }

            @Override
            public void onSuccess(String s) {
                dismissloading();
                bean.downloadUrl = s;
                String title = bean.title;
                String artistName = bean.artistName;
                String image = bean.image;


                if (mMusicListener != null) {
                    mMusicListener.play("nhac", title, artistName, s, image);
                }
            }
        });
    }

    @Override
    public void search(String query) {
        if (!TextUtils.isEmpty(query)) {
            index = 1;
            List<MDA_MusicBean> musicListAdapterData = musicListAdapter.getData();
            if (musicListAdapterData != null) {
                musicListAdapterData.clear();
                musicListAdapter.notifyDataSetChanged();
            }
            this.query = query;
            smartRefreshLayout.setEnableRefresh(true);
            smartRefreshLayout.autoRefresh();
            loadMusic();
        }
    }


    @Override
    public void loadMore() {
        loadMusic();
    }

    @Override
    public void refresh() {
        index = 1;
        loadMusic();
    }
}
