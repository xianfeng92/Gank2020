package com.xforg.gank2020.common.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import com.xforg.gank2020.common.recyclerview.base.ItemViewDelegate;
import com.xforg.gank2020.common.recyclerview.base.ViewHolder;
import java.util.List;

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    private static final String TAG = "CommonAdapter";
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        Log.d(TAG, "CommonAdapter: ");
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                Log.d(TAG, "getItemViewLayoutId: "+layoutId);
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                Log.d(TAG, "isForViewType: ");
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                Log.d(TAG, "convert: ");
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

}
