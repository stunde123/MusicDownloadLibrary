package api.music.download.bean;

public  class MDA_MusicBean {

    public static final int CHANNEL_JAMENDO = 1;
    public static final int CHANNEL_FREEMP3 = 2;
    public static final int CHANNEL_ARCHIVE_HTML = 3;
    public static final int CHANNEL_YT = 8;
    public static final int CHANNEL_NHAC = 9;
    public static final int CHANNEL_MP3JUICE = 10;

    public int channel;

    public String id;
    public String title;
    public String artistName;
    public String image;
    public String durationstr;
    public String listenUrl;

    public String downloadUrl;
//    public String downloadUrl1;
//    public String downloadUrl2;
//    public String downloadUrl3;

    //temp
//    public List<YTAudioBean> ytlist=new ArrayList<>();

    //add local
//    public String location;//本地存储路径
    public String fileName;
    public int realduration;//真实解析的
    public int downloadStats = 0;//0:未开始，1正在，2完成,3 error
//    public int progress;


    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName == null ? "" : artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDurationstr() {
        return durationstr == null ? "" : durationstr;
    }

    public void setDurationstr(String durationstr) {
        this.durationstr = durationstr;
    }

    public String getListenUrl() {
        return listenUrl == null ? "" : listenUrl;
    }

    public void setListenUrl(String listenUrl) {
        this.listenUrl = listenUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl == null ? "" : downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
