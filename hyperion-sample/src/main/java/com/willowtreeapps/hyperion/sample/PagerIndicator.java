package com.willowtreeapps.hyperion.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class PagerIndicator extends View implements ViewPager.OnPageChangeListener {

    private final Paint paint;
    private ViewPager pager;
    private int currentPage;

    public PagerIndicator(@NonNull Context context) {
        this(context, null);
    }

    public PagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context, R.color.dark_blue));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        final ViewParent parent = getParent();
        pager = ((ViewGroup) parent).findViewById(R.id.pager);
        pager.addOnPageChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (pager != null) {
            pager.removeOnPageChangeListener(this);
            pager = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getMeasuredWidth();
        float left, top, right, bottom;

        left = 10f;
        top = 10f;
        right = 20f;
        bottom = 20f;
        drawPosition(0, canvas, left, top, right, bottom);

        left = (width / 2) - 10f;
        right = left + 10f;
        drawPosition(1, canvas, left, top, right, bottom);

        left = width - 20f;
        right = left + 10f;
        drawPosition(2, canvas, left, top, right, bottom);
    }

    private void drawPosition(int position, Canvas canvas, float left, float top, float right, float bottom) {
        if (currentPage == position) {
            final float offset = 4f;
            canvas.drawRect(left - offset, top - offset, right + offset, bottom + offset, paint);
        } else {
            final float cx = (left + right) / 2;
            final float cy = (top + bottom) / 2;
            final float radius = right - left;
            canvas.drawCircle(cx, cy, radius, paint);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}