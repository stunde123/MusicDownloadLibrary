package api.music.download.fragment;

public interface DownloadAndPlayListener {

    /**
     *
     * @param channel 渠道
     * @param title   标题
     * @param artistName 作者
     * @param url 地址
     * @param image 缩略图
     * @param durationStr 时间
     */
    void download(String channel, String title, String artistName, String url, String image, String durationStr);

    /**
     *
     * @param channel 渠道
     * @param title   标题
     * @param artistName 作者
     * @param url 地址
     * @param image 缩略图
     */
    void play(String channel, String title, String artistName, String url, String image);


    /**
     *
     * @param channel 渠道
     * @param word	  事件
     * @param result  值
     */

    void eventResult(String channel, String word, String result);

}
