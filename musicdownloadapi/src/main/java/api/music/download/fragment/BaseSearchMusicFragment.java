package api.music.download.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Iterator;
import java.util.List;

import api.music.download.R;
import api.music.download.adapter.MusicListAdapter;
import api.music.download.bean.MDA_MusicBean;
import api.music.download.dialog.MDA_LoadDialog;

public abstract class BaseSearchMusicFragment extends Fragment {
    protected MusicListAdapter musicListAdapter = new MusicListAdapter();
    protected SmartRefreshLayout smartRefreshLayout;
    protected DownloadAndPlayListener mMusicListener;
    protected String query = "";

    public static <T extends BaseSearchMusicFragment> BaseSearchMusicFragment newInstance(Class<T> clz, String query) {

        Bundle args = new Bundle();
        args.putString("query", query);
        BaseSearchMusicFragment fragment = null;
        try {
            fragment = clz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mda_search_music_list, null, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView listView = view.findViewById(R.id.list_view);
        smartRefreshLayout = view.findViewById(R.id.smart_layout);

        smartRefreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });

        musicListAdapter.setOnItemChildClickListener(mItemChildClickListener);
        musicListAdapter.setOnItemClickListener(mItemClickListener);
        musicListAdapter.setItemUIConfig(mItemUIConfig);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listView.setAdapter(musicListAdapter);
        Bundle args = getArguments();
        query = args.getString("query");
        initData();
    }

    protected abstract void initData();

    private BaseQuickAdapter.OnItemChildClickListener mItemChildClickListener = (adapter, view, position) -> {
        MDA_MusicBean musicBean = (MDA_MusicBean) adapter.getItem(position);
        int id = view.getId();
        if (id == R.id.download_iv) {
            downloadMusic(musicBean);
        }
    };

    BaseQuickAdapter.OnItemClickListener mItemClickListener = (adapter, view, position) -> {
        MDA_MusicBean musicBean = (MDA_MusicBean) adapter.getItem(position);
        playMusic(musicBean);
    };

    protected abstract void downloadMusic(MDA_MusicBean musicBean);

    protected abstract void playMusic(MDA_MusicBean musicBean);

    public abstract void search(String query);


    public void setDownloadAndPlayMusicListener(DownloadAndPlayListener listener) {
        mMusicListener = listener;
    }


    public String secToTime(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public abstract void loadMore();

    public abstract void refresh();

    private MDA_LoadDialog mDialog;

    public void showloading() {
        try {
            if (null == mDialog) {
                mDialog = new MDA_LoadDialog(getActivity(), R.style.mdaloadingdialog);
                mDialog.setCancelable(false);
            }
            if (null != mDialog && !mDialog.isShowing())
                mDialog.show();
        } catch (Throwable e) {
        }

    }

    public void dismissloading() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Throwable e) {
        }
    }

    private ItemUIConfig mItemUIConfig = new ItemUIConfig.Builder().build();

    public void setItemUIConfig(ItemUIConfig itemUIConfig) {
        mItemUIConfig = itemUIConfig;
    }

    private String[] filterWord = {};

    public void filterWord(String word) {
        if (TextUtils.isEmpty(word) || !word.contains("-")) {
            return;
        }
        this.filterWord = word.split("-");
    }


    public void dealData(List<MDA_MusicBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (filterWord == null || filterWord.length == 0) {
            return;
        }
        Iterator<MDA_MusicBean> iterator = list.iterator();

        while (iterator.hasNext()) {
            MDA_MusicBean next = iterator.next();
            if (isContain(next.title, filterWord)
                    || isContain(next.artistName, filterWord)
                    || isContain(next.title, filterWord)) {
                iterator.remove();
            }
        }
    }

    private boolean isContain(String content, String[] words) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        content = content.replaceAll("[ -.~!@#$%^&*()_+={}|:<>?/,':;\\]\\[`]", "").toLowerCase();
        for (String word : words) {
            if (content.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public void showShortToast(String text) {
        if (getContext() == null) {
            return;
        }
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


}
