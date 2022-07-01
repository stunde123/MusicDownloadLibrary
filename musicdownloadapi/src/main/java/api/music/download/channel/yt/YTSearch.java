package api.music.download.channel.yt;

import androidx.annotation.NonNull;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.search.SearchInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class YTSearch {
    private Disposable searchDisposable;
    private final CompositeDisposable searchdisposables = new CompositeDisposable();

    private onSearchListener mListener;
    public AtomicBoolean isLoading = new AtomicBoolean();
    private Page nextPage;

    public void search(int serviceId, String searchString, onSearchListener listener) {
        mListener = listener;
        List<String> contentFilter = new ArrayList<>(1);
        contentFilter.add("videos");
        if (searchdisposables != null) searchdisposables.clear();
        nextPage = null;
        searchDisposable = ExtractorHelper.searchFor(serviceId,
                searchString,
                contentFilter,
                "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent((searchResult, throwable) -> isLoading.set(false))
                .subscribe(this::handleResult, this::onError);
        searchdisposables.add(searchDisposable);
    }


    public void searchMore(int serviceId, String searchString, onSearchListener listener) {
        mListener = listener;
        if (searchdisposables != null) searchdisposables.clear();
        if (nextPage == null) {
            return;
        }

        List<String> contentFilter = new ArrayList<>(1);
        contentFilter.add("videos");
        searchDisposable = ExtractorHelper.getMoreSearchItems(
                serviceId,
                searchString,
                contentFilter,
                "",
                nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent((nextItemsResult, throwable) -> isLoading.set(false))
                .subscribe(this::handleNextItems, this::onError);
        searchdisposables.add(searchDisposable);
    }

    public void handleNextItems(final ListExtractor.InfoItemsPage<InfoItem> result) {
        List<InfoItem> list = result.getItems();
        if (result.hasNextPage()) {
            nextPage = result.getNextPage();
        } else {
            nextPage = null;
        }
        handlerResultData(list);
    }


    private void handleResult(@NonNull SearchInfo result) {
        final List<Throwable> exceptions = result.getErrors();
        if (!exceptions.isEmpty() && !(exceptions.size() == 1 && exceptions.get(0) instanceof SearchExtractor.NothingFoundException)) {
            if (null != mListener) {
                mListener.onError();
                return;
            }
        }
        if (result.hasNextPage()) {
            nextPage = result.getNextPage();
        } else {
            nextPage = null;
        }

        List<InfoItem> datas = result.getRelatedItems();
        handlerResultData(datas);
    }

    private void handlerResultData(List<InfoItem> datas) {
        if (null == datas || datas.size() == 0) {
            if (null != mListener) {
                mListener.onEmpty();
            }
            return;
        }
        if (null != mListener) {
            List<StreamInfoItem> res = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i) instanceof StreamInfoItem) {
                    res.add((StreamInfoItem) datas.get(i));
                }
            }
            mListener.onSuccess(res);
        }
    }


    private void onError(Throwable exception) {
        if (null != mListener) {
            mListener.onError();
        }
    }

    public interface onSearchListener {
        void onSuccess(List<StreamInfoItem> list);

        void onError();

        void onEmpty();
    }
}
