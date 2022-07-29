package com.crqi.deeplink.nakedeye3d.croe;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crqi.deeplink.nakedeye3d.croe.NakedEyeView;

/**
 * 3d 动态图 子view是图片
 */
public class SensorView extends NakedEyeView implements SensorEventListener {
    private final SensorManager mSensorManager;
    private float[] mAccelerateValues;
    private float[] mMagneticValues;
    private final Scroller mScroller;
    private double mDegreeYMin = -40;
    private double mDegreeYMax = 40;
    private double mDegreeXMin = -40;
    private double mDegreeXMax = 40;
    private boolean hasChangeX;
    private int scrollX;
    private boolean hasChangeY;
    private int scrollY;
    private double mXMoveDistance = 30;
    private double mYMoveDistance = 20;
    private int mDirection = 1;

    private boolean hasRegister = false;

    public SensorView(@NonNull Context context) {
        this(context, null);
    }

    public SensorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerateValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagneticValues = event.values;
        }
        float[] values = new float[3];
        float[] R = new float[9];
        if (mMagneticValues != null && mAccelerateValues != null)
            SensorManager.getRotationMatrix(R, null, mAccelerateValues, mMagneticValues);
        SensorManager.getOrientation(R, values);
        // x轴的偏转角度
        values[1] = (float) Math.toDegrees(values[1]);
        // y轴的偏转角度
        values[2] = (float) Math.toDegrees(values[2]);
        double degreeX = values[1];
        double degreeY = values[2];
        if (degreeY <= 0 && degreeY > mDegreeYMin) {
            hasChangeX = true;
            scrollX = (int) (degreeY / Math.abs(mDegreeYMin) * mXMoveDistance * mDirection);
        } else if (degreeY > 0 && degreeY < mDegreeYMax) {
            hasChangeX = true;
            scrollX = (int) (degreeY / Math.abs(mDegreeYMax) * mXMoveDistance * mDirection);
        }
        if (degreeX <= 0 && degreeX > mDegreeXMin) {
            hasChangeY = true;
            scrollY = (int) (degreeX / Math.abs(mDegreeXMin) * mYMoveDistance * mDirection);
        } else if (degreeX > 0 && degreeX < mDegreeXMax) {
            hasChangeY = true;
            scrollY = (int) (degreeX / Math.abs(mDegreeXMax) * mYMoveDistance * mDirection);
        }
        smoothScroll(hasChangeX ? scrollX : mScroller.getFinalX(), hasChangeY ? scrollY : mScroller.getFinalY());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //加速度传感器，暂时不需要用
    }

    /**
     * 跟随传感器的变化
     * @param destX
     * @param destY
     */
    public void smoothScroll(int destX, int destY) {
        int scrollY = getScrollY();
        int delta = destY - scrollY;
        mScroller.startScroll(destX, scrollY, 0, delta, 200);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public void register() {
        if (mSensorManager != null && !hasRegister) {
            hasRegister = true;
            Sensor accelerateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // 地磁场传感器
            Sensor magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void unRegister() {
        mSensorManager.unregisterListener(this);
        hasRegister = false;
    }

    @Override
    public void setDegreeSpacing(int spacing) {
        mDegreeYMin = -spacing;
        mDegreeYMax = spacing;
        mDegreeXMin = -spacing;
        mDegreeXMax = spacing;
    }

    @Override
    public void setMoveDistance(int spacing) {
        mYMoveDistance = spacing;
        mXMoveDistance = (int) (spacing * 0.618);
    }

    @Override
    public void setDirection(@ADirection int direction) {
        mDirection = direction;
    }



}
