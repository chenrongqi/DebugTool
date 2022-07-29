package com.crqi.deeplink.nakedeye3d.croe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.crqi.deeplink.nakedeye3d.croe.NakedEyeAdapter;
import com.crqi.deeplink.nakedeye3d.croe.NakedEyeLayout;
import com.crqi.deeplink.nakedeye3d.croe.NakedEyeView;
import com.crqi.deeplink.nakedeye3d.croe.SensorView;

/**
 * 裸眼3d 的布局类，可以修改参数，通过adatper绑定数据等
 */
public class SensorNakedEyeLayout extends NakedEyeLayout implements LifecycleObserver {

    ///图片大小
    private double width;
    private double height;
    //宽高比。height/width
    private double scale = 0.618;

    //跟随阀值偏移量，距离等于0时静止
    private int moveDistance = 30;
    ///加速度相响应阀值，阀值越小越灵敏
    private int degreeSpacing = 40;
    private Lifecycle lifecycle;

    ///子view层级
    private NakedEyeView[] childViews;

    public SensorNakedEyeLayout(@NonNull Context context) {
        this(context, null);
    }

    public SensorNakedEyeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensorNakedEyeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        width = getResources().getDisplayMetrics().widthPixels;
        height = width * scale;
        setClipChildren(false);
    }

    /**
     * 传感器变化响应阀值，阀值越小越灵敏
     *
     * @param spacing
     */
    @Override
    public void setDegreeSpacing(int spacing) {
        degreeSpacing = spacing;
    }

    /**
     * 跟随传感器的偏移量，数值越小幅度越小，等于0时静止
     *
     * @param spacing
     */
    @Override
    public void setMoveDistance(int spacing) {
        moveDistance = spacing;
    }

    /**
     * 设置数据，必须设置adapter
     *
     * @param lifecycle 外围布局的生命周期,可空，但是建议传
     * @param adapter   图片链接数组
     */
    @Override
    public void setData(@Nullable Lifecycle lifecycle, @NonNull NakedEyeAdapter adapter) {
        this.lifecycle = lifecycle;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }

        this.mAdapter = adapter;
        if (adapter.getWidth() > 0 && adapter.getHeight() > 0) {
            if (adapter.getWidth() > width) {
                //配置的图片比屏幕大，则取其比例适配给布局
                scale = adapter.getHeight() / adapter.getWidth();
                height = width * scale;
            } else {
                //配置的图片比屏幕小，则取其绝对大小
                width = adapter.getWidth();
                height = adapter.getHeight();
                scale = height / width;
            }

        }

        createViews();
    }

    /**
     * 初始化子view
     */
    private void createViews() {
        ///暂停传感器监听
        stop();
        ///清除view
        if (childViews != null) {
            removeAllViews();
        }

        ///创建新view
        childViews = new NakedEyeView[mAdapter.getSize()];
        if (mAdapter.getSize() == 0) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        ///处理层级，相对位置最低和最顶为同一层级配置，相对位置正中间一层为中间静止层
        int depth = mAdapter.getSize() / 2;
        ///跟随传感器阀值偏移量随着层级距离中间静止层越近，偏移量越小
        createView(0, mAdapter.getSize() - 1, moveDistance, moveDistance / depth);

        ///处理完的配置信息view添加进父布局
        for (int i = 0; i < childViews.length; i++) {
            View v = childViews[i];
            FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            addView(v, layoutParams);
        }

        ///重新开启定时器
        start();
    }

    /**
     * 处理添加图层
     *
     * @param start        开始位置
     * @param end          结束位置
     * @param moveDistance 触发阀值后的滚动距离
     * @param descending   各图层间的滚动距离衰减幅度
     */
    private void createView(int start, int end, int moveDistance, int descending) {
        if (start < end) {
            getNakedEyeView(start, moveDistance, NakedEyeView.DIRECTION_LEFT);
            getNakedEyeView(end, moveDistance, NakedEyeView.DIRECTION_RIGHT);

            ///处理下一个层级，滚动幅度衰减
            createView(start + 1, end - 1, moveDistance - descending, descending);
        } else if (start == end) {
            getNakedEyeView(end, moveDistance, NakedEyeView.DIRECTION_LEFT);
        }
    }

    /**
     * 添加view，展示保存，后面同一添加
     *
     * @param index     指定位置
     * @param groupView 子view
     */
    private void addChildVIew(int index, NakedEyeView groupView) {
        childViews[index] = groupView;
    }

    /**
     * 获取传感器view
     *
     * @param index        图片位置
     * @param moveDistance 滚动距离
     */
    private void getNakedEyeView(int index, int moveDistance, int right) {
        SensorView groupView = new SensorView(getContext());
        groupView.setDegreeSpacing(degreeSpacing);
        groupView.setMoveDistance(moveDistance);
        groupView.setDirection(right);
        groupView.setClipChildren(false);

        LayoutParams layoutParams;

        if (index == 0) {
            layoutParams = new LayoutParams((int) (width + 2 * moveDistance), (int) (height + 2 * moveDistance));
            layoutParams.setMarginStart(-moveDistance);
            layoutParams.topMargin = -moveDistance;
            layoutParams.bottomMargin = -moveDistance;
            layoutParams.setMarginEnd(-moveDistance);
        } else {
            layoutParams = new LayoutParams((int) (width), (int) (height));
        }
        View view = mAdapter.getView(index, getContext());
        layoutParams.gravity = Gravity.CENTER;
        groupView.addView(view, layoutParams);
        addChildVIew(index, groupView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void start() {
        if (childViews == null || childViews.length == 0 || (lifecycle != null && !lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED))) {
            return;
        }
        for (NakedEyeView view : childViews) {
            if (view != null) {
                view.register();
            }
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (lifecycle == null) {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (lifecycle == null) {
            stop();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void stop() {
        if (childViews == null || childViews.length == 0) {
            return;
        }
        for (NakedEyeView view : childViews) {
            if (view != null) {
                view.unRegister();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void clearData() {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
        }
    }


}
