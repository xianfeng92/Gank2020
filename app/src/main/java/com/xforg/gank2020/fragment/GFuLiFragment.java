package com.xforg.gank2020.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.squareup.picasso.Picasso;
import com.xforg.g2020.base.BaseFragment;
import com.xforg.g2020.di.component.AppComponent;
import com.xforg.g2020.integration.IRepositoryManager;
import com.xforg.gank2020.R;
import com.xforg.gank2020.beans.GanHuoList;
import com.xforg.gank2020.common.recyclerview.CommonAdapter;
import com.xforg.gank2020.common.recyclerview.base.ViewHolder;
import com.xforg.gank2020.common.recyclerview.decoration.OffsetDecoration;
import com.xforg.gank2020.common.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.xforg.gank2020.di.component.DaggerAllFragmentComponent;
import com.xforg.gank2020.mvp.model.api.service.UserService;
import com.xforg.gank2020.mvp.presenter.CommonPresenter;
import com.xforg.gank2020.widget.MultipleStatusView;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created By zhongxianfeng on 19-4-15
 * github: https://github.com/xianfeng92
 */
public class GFuLiFragment extends BaseFragment<CommonPresenter> implements OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = "GFuLiFragment";

    String type = "福利";

    protected RecyclerView mRecyclerView;
    SwipeToLoadLayout mSwipeToLoadLayout;
    MultipleStatusView mMultipleStatusView;
    protected CommonAdapter<GanHuoList.ResultsBean> commonAdapter;
    protected HeaderAndFooterWrapper headerAndFooterWrapper;
    protected List<GanHuoList.ResultsBean> list = new ArrayList<>();
    protected int page = 1;
    protected int pageSize = 10;
    private View view;
    protected String tag = UUID.randomUUID().toString();

    @Inject
    IRepositoryManager repositoryManager;

    protected OffsetDecoration decoration = new OffsetDecoration();

    protected void setLoadMoreEnabled(boolean enable) {
        mSwipeToLoadLayout.setLoadMoreEnabled(enable);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base_list,container,false);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = view.findViewById(R.id.content_view);
        mMultipleStatusView = view.findViewById(R.id.multipleStatusView);
        initViews();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getData(true);
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
        commonAdapter = new CommonAdapter<GanHuoList.ResultsBean>(mContext, R.layout.item_fuli, list) {
            @Override
            public void convert(ViewHolder holder,GanHuoList.ResultsBean ganHuo, int position) {
                Log.d(TAG, "convert: ");
                ImageView mImage = holder.getView(R.id.image);
                Picasso.with(getContext()).load(ganHuo.getUrl()).placeholder(R.mipmap.avatar).into(mImage);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
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
        repositoryManager.obtainRetrofitService(UserService.class)
                .getGanHuoList(type,pageSize,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GanHuoList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GanHuoList resultsBeans) {
                        Log.d(TAG, "onNext: ");
                        list.addAll(resultsBeans.results);
                        commonAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerAllFragmentComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
