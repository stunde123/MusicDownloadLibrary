package music.api.net.core;

public
interface HttpCallBack<T> {
    void onSuccess(T musicBeans);

    void onFailure(Exception e);
}
