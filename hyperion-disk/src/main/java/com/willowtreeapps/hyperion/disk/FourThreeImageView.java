package com.willowtreeapps.hyperion.disk;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class FourThreeImageView extends AppCompatImageView {

    public FourThreeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int fourThreeHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthSpec) * 3 / 4,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, fourThreeHeight);
    }
}