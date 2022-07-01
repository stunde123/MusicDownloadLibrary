package api.music.download.channel.nhac;


public interface CallBack<T>  {
    void onFail();
    void onSuccess(T t);
}

