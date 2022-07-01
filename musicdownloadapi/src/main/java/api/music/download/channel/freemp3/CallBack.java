package api.music.download.channel.freemp3;


public interface CallBack<T>  {
    void onFail();
    void onSuccess(T t);
}

