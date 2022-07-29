package com.crqi.deeplink.nakedeye3d.croe;

import android.content.Context;
import android.view.View;

/**
 * 裸眼3d数据适配器
 */
public interface NakedEyeAdapter {
    /**
     * 获取图层总数
     *
     * @return
     */
    public int getSize();

    /**
     * 获取每层对应的view，获取view和bindingview，不存在回收
     *
     * @param index
     * @param context
     * @return
     */
    public View getView(int index, Context context);

    /**
     * 动态设置裸眼3ds的宽
     *
     * @return
     */
    public double getWidth();

    /**
     * 动态设置裸眼3ds的高
     *
     * @return
     */
    public double getHeight();
}
