package com.xforg.gank2020.fragment;

import android.support.v4.app.Fragment;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.xforg.gank2020.R;
import com.xforg.gank2020.base.BaseListFragment;
import com.xforg.gank2020.beans.GanHuo;
import com.xforg.gank2020.common.recyclerview.base.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FuLiFragment extends BaseListFragment<GanHuo> {

    @Override
    public int getItemLayout() {
        return R.layout.item_fuli;
    }

    @Override
    public void fillValue(ViewHolder holder, GanHuo ganHuo, int position) {
        ImageView mImage = holder.getView(R.id.image);
        Picasso.with(getContext()).load(ganHuo.getUrl()).placeholder(R.mipmap.avatar).into(mImage);
    }

    @Override
    protected String getUrl() {
        return "https://gank.io/api/data/福利/" + String.valueOf(pageSize) + "/" + String.valueOf(page);
    }
}
