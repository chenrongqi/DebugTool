package com.crqi.deeplink.nakedeye3d.croe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class NakedEyeView extends FrameLayout {
    public NakedEyeView(@NonNull Context context) {
        super(context);
    }

    public NakedEyeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NakedEyeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void register();
    public abstract void unRegister();
    public abstract void setDegreeSpacing(int spacing);
    public abstract void setMoveDistance(int spacing);
    public abstract void setDirection(@SensorView.ADirection int direction);

    @IntDef({DIRECTION_LEFT, DIRECTION_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface ADirection {

    }

    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = -1;
}
