package api.music.download.channel.archive;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import api.music.download.bean.MDA_MusicBean;

public
class ParseHtml {

    public static List<MDA_MusicBean> parse(Element element) {
        Element contentElement = element.getElementById("container");
        Elements colsetCElements = contentElement.getElementsByClass("colset-c");
        Element colsetCElement = colsetCElements.first();
        Elements listElements = colsetCElement.getElementsByClass("box-stnd-10pad-20marg play-lrg-list");
        Element listElement = listElements.first();
        Element playListElement = element.getElementsByClass("playlist playlist-lrg").first();
        Elements listItemElements = playListElement.getElementsByClass("play-item gcol gid-electronic");
        List<MDA_MusicBean> musicBeans = new ArrayList<>();
        for (Element itemElement : listItemElements) {
            Element artistElement = itemElement.getElementsByClass("ptxt-artist").first();
            String artist = artistElement.text();

            Element trackElement = itemElement.getElementsByClass("ptxt-track").first();
            String track = trackElement.text();

            Element downloadElement = itemElement.getElementsByClass("icn-arrow js-download").first();
            String dataUrl = downloadElement.attr("data-url");
            dataUrl = dataUrl.substring(0, dataUrl.lastIndexOf("Overlay"));

            MDA_MusicBean bean = new MDA_MusicBean();
            bean.channel = MDA_MusicBean.CHANNEL_ARCHIVE_HTML;
            bean.id = dataUrl.hashCode() + "";
            bean.setTitle(artist);
            bean.setArtistName(track);
            bean.setDownloadUrl(dataUrl);
            bean.setListenUrl(dataUrl);
            musicBeans.add(bean);
        }
        return musicBeans;
    }
}
