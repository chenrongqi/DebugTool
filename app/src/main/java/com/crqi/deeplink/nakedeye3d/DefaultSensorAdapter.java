package com.crqi.deeplink.nakedeye3d;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.crqi.deeplink.nakedeye3d.croe.NakedEyeAdapter;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchyInflater;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class DefaultSensorAdapter implements NakedEyeAdapter {
    private final List<String> data;

    public DefaultSensorAdapter(@NonNull List<String> data) {
        this.data = data;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public View getView(int index, Context context) {
        GenericDraweeHierarchyBuilder builder =
                GenericDraweeHierarchyInflater.inflateBuilder(context, null);
        if (index == 0) {
            builder.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        } else {
            builder.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        }
        SimpleDraweeView view = new SimpleDraweeView(context, builder.build());
        view.setImageURI(data.get(index));
        return view;

    }

    @Override
    public double getWidth() {
        return -1;
    }

    @Override
    public double getHeight() {
        return -1;
    }
}
