package api.music.download.fragment;

import java.util.ArrayList;
import java.util.List;

public class Fragments {

    public static final List<BaseSearchMusicFragment>
    getDownloadFragment(String query, String filterWord,
                        DownloadAndPlayListener downloadAndPlayMusicListener,
                        int titleColor, int subTitleColor, int playResId, int downResId) {


        ItemUIConfig itemUIConfig = new ItemUIConfig.Builder()
                .setTitleColor(titleColor).setSubTitleColor(subTitleColor)
                .setPlayResId(playResId).setDownloadResId(downResId)
                .build();

        return getDownloadFragment(query, filterWord, downloadAndPlayMusicListener, itemUIConfig);
    }


    public static final List<BaseSearchMusicFragment>
    getDownloadFragment(String query, String filterWord,
                        DownloadAndPlayListener downloadAndPlayMusicListener, ItemUIConfig itemUIConfig) {
        List<BaseSearchMusicFragment> fragmentList = new ArrayList<>();

        BaseSearchMusicFragment ytFragment = getYTFragment(query);
        BaseSearchMusicFragment nhacFragment = getNHACFragment(query);
        BaseSearchMusicFragment archiveFragment = getArchiveFragment(query);
        BaseSearchMusicFragment jmaFragment = getJmaFragment(query);

        fragmentList.add(ytFragment);
        fragmentList.add(nhacFragment);
        fragmentList.add(archiveFragment);
        fragmentList.add(jmaFragment);

        for (BaseSearchMusicFragment baseFragment : fragmentList) {
            baseFragment.filterWord(filterWord);
            baseFragment.setDownloadAndPlayMusicListener(downloadAndPlayMusicListener);
            baseFragment.setItemUIConfig(itemUIConfig);
        }


        return fragmentList;

    }


    public static BaseSearchMusicFragment getYTFragment(String query) {
        return SearchYTFragment.newInstance(SearchYTFragment.class, query);
    }

    public static BaseSearchMusicFragment getNHACFragment(String query) {
        return SearchNhacFragment.newInstance(SearchNhacFragment.class, query);
    }


    public static BaseSearchMusicFragment getMp3JuiceFragment(String query) {
        return SearchMp3JuiceFragment.newInstance(query);
    }


    public static BaseSearchMusicFragment getFreeMp3Fragment(String query) {
        return SearchFreeMp3Fragment.newInstance(query);
    }


    public static BaseSearchMusicFragment getArchiveFragment(String query) {
        return SearchFreeMusicArchiveFragment.newInstance(SearchFreeMusicArchiveFragment.class, query);
    }


    public static BaseSearchMusicFragment getJmaFragment(String query) {
        return SearchJmaFragment.newInstance(SearchJmaFragment.class, query);
    }


}
