package com.xforg.gank2020.base;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.internal.$Gson$Types;
import com.xforg.g2020.base.BaseFragment;
import com.xforg.gank2020.R;
import com.xforg.gank2020.common.recyclerview.CommonAdapter;
import com.xforg.gank2020.common.recyclerview.base.ViewHolder;
import com.xforg.gank2020.common.recyclerview.decoration.OffsetDecoration;
import com.xforg.gank2020.common.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.xforg.gank2020.widget.MultipleStatusView;
import com.xforg.http.HttpListener;
import com.xforg.http.RequestManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.swipe_target)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.content_view)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView mMultipleStatusView;
    protected CommonAdapter<T> commonAdapter;
    protected HeaderAndFooterWrapper headerAndFooterWrapper;
    protected List<T> list = new ArrayList<>();
    protected HashMap<String, String> map = new HashMap<>();
    protected int page = 1;
    protected int pageSize = 30;
    private Type type;
    private View view;
    protected String tag = UUID.randomUUID().toString();


    protected OffsetDecoration decoration = new OffsetDecoration();

    public BaseListFragment() {
        type = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    protected void setLoadMoreEnabled(boolean enable) {
        mSwipeToLoadLayout.setLoadMoreEnabled(enable);
    }

    public abstract int getItemLayout();

    public abstract void fillValue(ViewHolder holder, T t, int position);

    protected abstract String getUrl();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base_list,container,false);
        ButterKnife.bind(this,view);
        initViews();
        return view;
    }


    private void initViews() {
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mMultipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        commonAdapter = new CommonAdapter<T>(getActivity(), getItemLayout(), list) {
            @Override
            public void convert(ViewHolder holder, T t, int position) {
                fillValue(holder, t, position);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        headerAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
        SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(headerAndFooterWrapper);
        slideInBottomAnimationAdapter.setFirstOnly(true);
        mRecyclerView.setAdapter(slideInBottomAnimationAdapter);

        mRecyclerView.removeItemDecoration(decoration);
        mRecyclerView.addItemDecoration(decoration);
        onRefresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    private void getData(final boolean isRefresh) {
        RequestManager.getList(tag,  getUrl(), type,false, new HttpListener() {
            @Override
            public void onSuccess(Object o) {
                ArrayList<T> result = (ArrayList<T>) o;
                if (isRefresh) {
                    page = 2;
                    list.clear();
                } else {
                    page++;
                }
                list.addAll(result);
                if (list.size() > 0) {
                    mMultipleStatusView.showContent();
                } else {
                    mMultipleStatusView.showEmpty();
                }
                commonAdapter.notifyDataSetChanged();
                if (mSwipeToLoadLayout.isRefreshing()) {
                    mSwipeToLoadLayout.setRefreshing(false);
                }
                if (mSwipeToLoadLayout.isLoadingMore()) {
                    mSwipeToLoadLayout.setLoadingMore(false);
                }

            }

            @Override
            public void onFailure(int errorType, String message) {
                if (mSwipeToLoadLayout != null && mMultipleStatusView != null) {
                    if (list.size() < 1) {
                        if (errorType == 5) {
                            mMultipleStatusView.showNoNetwork();
                        } else {
                            mMultipleStatusView.showError();
                        }
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                    if (mSwipeToLoadLayout.isRefreshing()) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                    if (mSwipeToLoadLayout.isLoadingMore()) {
                        mSwipeToLoadLayout.setLoadingMore(false);
                    }
                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RequestManager.cancelRequest(tag);
        view = null;
    }

    @Override
    public void onRefresh() {
        if (list.size() < 1)
            mMultipleStatusView.showLoading();
        page = 1;
        getData(true);
    }

    @Override
    public void onLoadMore() {
        getData(false);
    }
}
