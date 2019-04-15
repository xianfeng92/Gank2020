package com.xforg.gank2020.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
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
import com.xforg.gank2020.utils.DimenUtils;
import com.xforg.gank2020.utils.ThemeUtils;
import com.xforg.gank2020.widget.MultipleStatusView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created By zhongxianfeng on 19-4-15
 * github: https://github.com/xianfeng92
 */
public class GAllFrament extends BaseFragment<CommonPresenter> implements OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = "GAllFrament";
    private String type = "all";

    @BindView(R.id.swipe_target)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.content_view)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.multipleStatusView)
    MultipleStatusView mMultipleStatusView;
    protected CommonAdapter<GanHuoList.ResultsBean> commonAdapter;
    protected HeaderAndFooterWrapper headerAndFooterWrapper;
    protected List<GanHuoList.ResultsBean> list = new ArrayList<>();
    protected HashMap<String, String> map = new HashMap<>();
    protected int page = 1;
    protected int pageSize = 30;
    protected String tag = UUID.randomUUID().toString();
    private View view;
    protected OffsetDecoration decoration = new OffsetDecoration();


    @Inject
    IRepositoryManager repositoryManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base_list,container,false);
        ButterKnife.bind(this,view);
        initViews();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onLoadMore() {
        getData(false);
    }

    @Override
    public void onRefresh() {
        if (list.size() < 1)
            mMultipleStatusView.showLoading();
        page = 1;
        getData(true);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        commonAdapter = new CommonAdapter<GanHuoList.ResultsBean>(getActivity(), R.layout.item_common, list) {
            @Override
            public void convert(ViewHolder holder, GanHuoList.ResultsBean ganHuo, int position) {
                    ImageView mImage = holder.getView(R.id.image);
                    TextView mText = holder.getView(R.id.text);
                    if (ganHuo.getType().equals("福利")) {
                        mImage.setVisibility(View.VISIBLE);
                        mText.setVisibility(View.GONE);
                        Picasso.with(getContext()).load(ganHuo.getUrl()).placeholder(R.mipmap.avatar).into(mImage);
                    } else {
                        mImage.setVisibility(View.GONE);
                        mText.setVisibility(View.VISIBLE);
                        mText.setLinkTextColor(ThemeUtils.getThemeColor(getActivity(),R.attr.colorPrimary));
                        mText.setText(Html.fromHtml("<a href=\""
                                + ganHuo.getUrl() + "\">"
                                + ganHuo.getDesc() + "</a>"
                                + "[" + ganHuo.getWho() + "]"));
                        mText.setMovementMethod(LinkMovementMethod.getInstance());
                        switch (ganHuo.getType()) {
                            case "Android":
                                setIconDrawable(mText, MaterialDesignIconic.Icon.gmi_android);
                                break;
                            case "iOS":
                                setIconDrawable(mText, MaterialDesignIconic.Icon.gmi_apple);
                                break;
                            case "休息视频":
                                setIconDrawable(mText, MaterialDesignIconic.Icon.gmi_collection_video);
                                break;
                            case "前端":
                                setIconDrawable(mText, MaterialDesignIconic.Icon.gmi_language_javascript);
                                break;
                            case "拓展资源":
                                setIconDrawable(mText, FontAwesome.Icon.faw_location_arrow);
                                break;
                            case "App":
                                setIconDrawable(mText, MaterialDesignIconic.Icon.gmi_apps);
                                break;
                            case "瞎推荐":
                                setIconDrawable(mText, MaterialDesignIconic.Icon.gmi_more);
                                break;

                        }
                    }
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
                        list.addAll(resultsBeans.results);
                        commonAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void setIconDrawable(TextView view, IIcon icon) {
        view.setCompoundDrawablesWithIntrinsicBounds(new IconicsDrawable(getActivity())
                        .icon(icon)
                        .color(ThemeUtils.getThemeColor(getActivity(), R.attr.colorPrimary))
                        .sizeDp(14),
                null, null, null);
        view.setCompoundDrawablePadding(DimenUtils.dp2px(getActivity(), 5));
    }
}
