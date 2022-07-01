package api.music.download.fragment;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import api.music.download.R;

public class ItemUIConfig {

    public int titleColor = Color.WHITE;
    public int subTitleColor = Color.WHITE;
    public int downloadResId = R.drawable.mda_icon_download;
    public int playResId = R.drawable.mda_ic_play;

    private ItemUIConfig(Builder builder) {
        titleColor = builder.titleColor;
        subTitleColor = builder.subTitleColor;
        downloadResId = builder.downloadResId;
        playResId = builder.playResId;
    }


    public static class Builder {
        int titleColor = Color.WHITE;
        int subTitleColor = Color.WHITE;
        int downloadResId = R.drawable.mda_icon_download;
        int playResId = R.drawable.mda_ic_play;

        public Builder() {
        }

        public Builder setTitleColor(@ColorInt int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setSubTitleColor(@ColorInt int subTitleColor) {
            this.subTitleColor = subTitleColor;
            return this;
        }

        public Builder setDownloadResId(@DrawableRes int downloadResId) {
            this.downloadResId = downloadResId;
            return this;
        }

        public Builder setPlayResId(@DrawableRes int playResId) {
            this.playResId = playResId;
            return this;
        }

        public ItemUIConfig build() {
            return new ItemUIConfig(this);
        }


    }


}
