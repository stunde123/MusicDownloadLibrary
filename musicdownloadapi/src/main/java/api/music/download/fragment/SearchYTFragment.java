package api.music.download.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import java.util.ArrayList;
import java.util.List;

import api.music.download.bean.MDA_MusicBean;
import api.music.download.channel.yt.YTAudioBean;
import api.music.download.channel.yt.YTGetAudios;
import api.music.download.channel.yt.YTManager;
import api.music.download.channel.yt.YTSearch;

public class SearchYTFragment extends BaseSearchMusicFragment {



    @Override
    protected void initData() {
        search(query);
    }


    @Override
    protected void downloadMusic(MDA_MusicBean musicBean) {
        if (!TextUtils.isEmpty(musicBean.downloadUrl)) {
            if (mMusicListener != null) {
                String title = musicBean.title;
                String artistName = musicBean.artistName;
                String image = musicBean.image;
                String durationStr = musicBean.durationstr;
                mMusicListener.download("yt", title, artistName, musicBean.downloadUrl, image, durationStr);
            }
            return;
        }

        String listenUrl = musicBean.getListenUrl();
        showloading();
        YTManager.getInstance().getAudiosYT(listenUrl, new YTGetAudios.onAudiosListener() {
            @Override
            public void onSuccess(List<YTAudioBean> list) {
                dismissloading();
                if (list == null || list.isEmpty()) {
                    return;
                }

                YTAudioBean ytAudioBean = list.get(0);
                String url = ytAudioBean.url;
                musicBean.downloadUrl = url;
                String title = musicBean.title;
                String artistName = musicBean.artistName;
                String image = musicBean.image;
                String durationstr = musicBean.durationstr;
                if (mMusicListener != null) {
                    mMusicListener.download("yt", title, artistName, url, image, durationstr);
                }
            }

            @Override
            public void onError() {
                dismissloading();
            }
        });
    }

    @Override
    protected void playMusic(MDA_MusicBean musicBean) {
        String listenUrl = musicBean.getListenUrl();
        showloading();
        YTManager.getInstance().getAudiosYT(listenUrl, new YTGetAudios.onAudiosListener() {
            @Override
            public void onSuccess(List<YTAudioBean> list) {
                dismissloading();
                YTAudioBean ytAudioBean = list.get(0);
                String url = ytAudioBean.url;
                musicBean.downloadUrl = url;

                String title = musicBean.title;
                String artistName = musicBean.artistName;
                String image = musicBean.image;


                if (mMusicListener != null) {
                    mMusicListener.play("yt", title, artistName, url, image);
                }

            }

            @Override
            public void onError() {
                dismissloading();
            }
        });
    }

    @Override
    public void search(String query) {
        if (!TextUtils.isEmpty(query)) {
            if (musicListAdapter.getData() != null) {
                musicListAdapter.getData().clear();
                musicListAdapter.notifyDataSetChanged();
            }
            this.query = query;
            smartRefreshLayout.setEnableRefresh(true);
            smartRefreshLayout.autoRefresh();
            YTManager.getInstance().searchYT(query, onSearchListener);
        }
    }

    @Override
    public void loadMore() {
        smartRefreshLayout.autoLoadMore();
        YTManager.getInstance().searchMoreYT(query, onSearchListener);
    }

    @Override
    public void refresh() {
        YTManager.getInstance().searchYT(query, onSearchListener);
    }

    YTSearch.onSearchListener onSearchListener = new YTSearch.onSearchListener() {
        @Override
        public void onSuccess(List<StreamInfoItem> list) {
            if (null == list || list.size() == 0) {
                onEmpty();
                return;
            }
            List<MDA_MusicBean> musicBeans = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                StreamInfoItem streamInfoItem = list.get(i);
                MDA_MusicBean bean = new MDA_MusicBean();
                bean.id = streamInfoItem.toString();
                bean.channel = MDA_MusicBean.CHANNEL_YT;
                bean.setTitle(streamInfoItem.getName());
                bean.setArtistName(streamInfoItem.getUploaderName());
                bean.setDurationstr(secToTime(streamInfoItem.getDuration()));
//                bean.setDownloadUrl(streamInfoItem.getUrl());
                bean.setListenUrl(streamInfoItem.getUrl());
                bean.setImage(streamInfoItem.getThumbnailUrl());
                musicBeans.add(bean);
            }
            dealData(musicBeans);
            if (musicBeans.isEmpty()) {
                return;
            }
            if (mMusicListener != null) {
                mMusicListener.eventResult("yt", query, musicBeans.toString());
            }

            List<MDA_MusicBean> musicListAdapterData = musicListAdapter.getData();
            if (musicListAdapterData == null || musicListAdapterData.isEmpty()) {
                musicListAdapter.setNewData(musicBeans);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.setEnableRefresh(false);
            } else {
                musicListAdapter.addData(musicBeans);
                smartRefreshLayout.finishLoadMore();
            }
        }

        @Override
        public void onError() {

        }

        @Override
        public void onEmpty() {

        }
    };

}
