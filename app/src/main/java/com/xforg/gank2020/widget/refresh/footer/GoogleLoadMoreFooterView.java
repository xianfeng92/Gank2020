package com.xforg.gank2020.widget.refresh.footer;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.xforg.gank2020.R;
import com.xforg.gank2020.theme.ColorUiInterface;
import com.xforg.gank2020.utils.ThemeUtils;
import com.xforg.gank2020.widget.refresh.drawable.google.RingProgressDrawable;


public class GoogleLoadMoreFooterView extends FrameLayout implements SwipeTrigger, SwipeLoadMoreTrigger , ColorUiInterface {

    private ImageView ivLoadMore;

    private int mTriggerOffset;

    private RingProgressDrawable ringProgressDrawable;

    public GoogleLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public GoogleLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ringProgressDrawable = new RingProgressDrawable(context);
        Resources res = getResources();
        ringProgressDrawable.setColors(ThemeUtils.getThemeColor(getContext(), R.attr.colorPrimary));
        mTriggerOffset = context.getResources().getDimensionPixelOffset(R.dimen.load_more_trigger_offset_google);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivLoadMore = (ImageView) findViewById(R.id.ivLoadMore);
        ivLoadMore.setBackgroundDrawable(ringProgressDrawable);
    }

    @Override
    public void onLoadMore() {
        ringProgressDrawable.start();
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onSwipe(int y, boolean isComplete) {
        if (!isComplete) {
            ringProgressDrawable.setPercent(-y / (float) mTriggerOffset);
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void complete() {
        ringProgressDrawable.stop();
    }

    @Override
    public void onReset() {

    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setTheme(Resources.Theme themeId) {
        ringProgressDrawable.setColors(ThemeUtils.getThemeColor(getContext(), R.attr.colorPrimary));
    }
}
