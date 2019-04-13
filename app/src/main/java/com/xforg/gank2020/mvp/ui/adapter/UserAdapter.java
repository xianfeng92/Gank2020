package com.xforg.gank2020.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xforg.g2020.di.component.AppComponent;
import com.xforg.g2020.http.imageloader.ImageLoader;
import com.xforg.g2020.http.imageloader.glide.GlideImageLoaderStrategy;
import com.xforg.g2020.http.imageloader.glide.ImageConfigImpl;
import com.xforg.g2020.utils.ArmsUtils;
import com.xforg.gank2020.R;
import com.xforg.gank2020.mvp.model.entity.User;
import java.util.List;

public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    private AppComponent mAppComponent;

    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public UserAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        mImageLoader = mAppComponent.imageLoader();
        if (mImageLoader.getLoadImgStrategy() == null){
            Log.d(TAG, "convert: mImageLoader.getLoadImgStrategy() == null");
            mImageLoader.setLoadImgStrategy(new GlideImageLoaderStrategy());
        }
        mImageLoader.loadImage(mContext,
                ImageConfigImpl
                        .builder()
                        .url(item.getAvatarUrl())
                        .imageView(helper.getView(R.id.iv_avatar))
                        .build());
    }
}
