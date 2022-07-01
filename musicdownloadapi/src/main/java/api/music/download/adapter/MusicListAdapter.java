package api.music.download.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import api.music.download.R;
import api.music.download.bean.MDA_MusicBean;
import api.music.download.fragment.ItemUIConfig;

public class MusicListAdapter extends BaseQuickAdapter<MDA_MusicBean, BaseViewHolder> {

    private ItemUIConfig mItemUIConfig;

    public MusicListAdapter() {
        super(R.layout.mda_item_music);
    }

    @Override
    protected void convert(BaseViewHolder helper, MDA_MusicBean item) {
        helper.setText(R.id.title_tv, item.title);
        helper.setText(R.id.artist_tv, item.artistName);
        helper.setText(R.id.duration_tv, item.durationstr);

        helper.setTextColor(R.id.title_tv, mItemUIConfig.titleColor);
        helper.setTextColor(R.id.artist_tv, mItemUIConfig.subTitleColor);
        helper.setTextColor(R.id.duration_tv, mItemUIConfig.subTitleColor);

        helper.setImageResource(R.id.download_iv, mItemUIConfig.downloadResId);
        helper.setImageResource(R.id.play_iv, mItemUIConfig.playResId);

        ImageView imageView = helper.getView(R.id.image_iv);
        String image = item.getImage();
        Glide.with(imageView).load(image).error(R.drawable.default_image).into(imageView);
        helper.addOnClickListener(R.id.download_iv);


    }


    public void setItemUIConfig(ItemUIConfig itemUIConfig) {
        mItemUIConfig = itemUIConfig;
    }
}
