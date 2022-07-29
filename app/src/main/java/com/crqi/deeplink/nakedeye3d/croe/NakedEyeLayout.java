package com.crqi.deeplink.nakedeye3d.croe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.crqi.deeplink.nakedeye3d.croe.NakedEyeAdapter;

import java.util.List;

/**
 * 裸眼3ds的布局
 */
public abstract class NakedEyeLayout extends FrameLayout {

    protected NakedEyeAdapter mAdapter;

    public NakedEyeLayout(@NonNull Context context) {
        super(context);
    }

    public NakedEyeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NakedEyeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setData(Lifecycle lifecycle,NakedEyeAdapter mAdapter);
    public abstract void setDegreeSpacing(int spacing);
    public abstract void setMoveDistance(int spacing);

}
