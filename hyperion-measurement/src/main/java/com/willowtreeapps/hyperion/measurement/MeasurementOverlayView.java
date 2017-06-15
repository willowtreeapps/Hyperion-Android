package com.willowtreeapps.hyperion.measurement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.core.MeasurementHelper;
import com.willowtreeapps.hyperion.core.ViewTarget;
import com.willowtreeapps.hyperion.core.plugins.ExtensionProvider;
import com.willowtreeapps.hyperion.core.plugins.PluginExtension;

class MeasurementOverlayView extends FrameLayout {

    private final ViewGroup contentRoot;
    private final ViewTarget target;
    private final MeasurementHelper measurementHelper;
    private final Path path = new Path();
    private final Rect outRect = new Rect();
    private final Paint paintDashed;
    private final Paint paintPrimary;
    private final Paint paintSecondary;
    private final TextPaint paintText;
    private final @Px int measurementTextOffset;

    private Rect rectPrimary;
    private Rect rectSecondary;
    private StaticLayout measurementWidthText;
    private StaticLayout measurementHeightText;
    /* Used for nested measurementHelper */
    private StaticLayout measurementLeftText;
    private StaticLayout measurementTopText;
    private StaticLayout measurementRightText;
    private StaticLayout measurementBottomText;

    MeasurementOverlayView(Context context) {
        super(context);
        PluginExtension extension = ExtensionProvider.get(context);
        contentRoot = extension.getContentRoot();
        target = extension.getViewTarget();
        measurementHelper = extension.getMeasurementHelper();

        paintDashed = new Paint();
        paintDashed.setColor(ContextCompat.getColor(context, R.color.hm_selection_primary));
        paintDashed.setStyle(Paint.Style.STROKE);
        paintDashed.setStrokeWidth(4f);
        paintDashed.setPathEffect(new DashPathEffect(new float[] {10, 20}, 0));

        paintPrimary = new Paint();
        paintPrimary.setColor(ContextCompat.getColor(context, R.color.hm_selection_primary));
        paintPrimary.setStyle(Paint.Style.STROKE);
        paintPrimary.setStrokeWidth(6f);

        paintSecondary = new Paint();
        paintSecondary.setColor(ContextCompat.getColor(context, R.color.hm_selection_secondary));
        paintSecondary.setStyle(Paint.Style.STROKE);
        paintSecondary.setStrokeWidth(6f);

        paintText = new TextPaint();
        paintText.setColor(ContextCompat.getColor(context, R.color.hm_selection_primary));
        paintText.setTextSize(45);
        paintText.setStyle(Paint.Style.FILL_AND_STROKE);
        paintText.setStrokeWidth(2);

        measurementTextOffset = getResources().getDimensionPixelSize(R.dimen.hm_measurement_text_offset);

        final View current = target.getTarget();
        if (current != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    setPrimaryTarget(current);
                    invalidate();
                }
            });
        }

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rectPrimary == null) {
            return;
        }

        canvas.drawRect(rectPrimary, paintPrimary);

        // top-left to top
        path.reset();
        path.moveTo(rectPrimary.left, 0);
        path.lineTo(rectPrimary.left, rectPrimary.top);
        canvas.drawPath(path, paintDashed);

        // top-right to top
        path.reset();
        path.moveTo(rectPrimary.right, 0);
        path.lineTo(rectPrimary.right, rectPrimary.top);
        canvas.drawPath(path, paintDashed);

        // top-left to left
        path.reset();
        path.moveTo(0, rectPrimary.top);
        path.lineTo(rectPrimary.left, rectPrimary.top);
        canvas.drawPath(path, paintDashed);

        // bottom-left to left
        path.reset();
        path.moveTo(0, rectPrimary.bottom);
        path.lineTo(rectPrimary.left, rectPrimary.bottom);
        canvas.drawPath(path, paintDashed);

        // bottom-left to bottom
        path.reset();
        path.moveTo(rectPrimary.left, rectPrimary.bottom);
        path.lineTo(rectPrimary.left, getBottom());
        canvas.drawPath(path, paintDashed);

        // bottom-right to bottom
        path.reset();
        path.moveTo(rectPrimary.right, rectPrimary.bottom);
        path.lineTo(rectPrimary.right, getBottom());
        canvas.drawPath(path, paintDashed);

        // bottom-right to right
        path.reset();
        path.moveTo(rectPrimary.right, rectPrimary.bottom);
        path.lineTo(getRight(), rectPrimary.bottom);
        canvas.drawPath(path, paintDashed);

        // top-right to right
        path.reset();
        path.moveTo(rectPrimary.right, rectPrimary.top);
        path.lineTo(getRight(), rectPrimary.top);
        canvas.drawPath(path, paintDashed);

        if (rectSecondary == null) {
            // draw width measurement text
            if (measurementWidthText != null) {
                canvas.save();
                canvas.translate(rectPrimary.centerX() - (measurementWidthText.getWidth() / 2),
                        rectPrimary.top - measurementWidthText.getHeight() - measurementTextOffset);
                measurementWidthText.draw(canvas);
                canvas.restore();
            }

            // draw height measurement text
            if (measurementHeightText != null) {
                canvas.save();
                canvas.translate(rectPrimary.right + measurementTextOffset,
                        rectPrimary.bottom - (rectPrimary.height() / 2) - (measurementHeightText.getHeight() / 2));
                measurementHeightText.draw(canvas);
                canvas.restore();
            }
        } else {
            canvas.drawRect(rectSecondary, paintSecondary);

            if (rectPrimary.bottom < rectSecondary.top) {
                // secondary is below. draw vertical line.
                canvas.drawLine(rectSecondary.centerX(), rectPrimary.bottom, rectSecondary.centerX(), rectSecondary.top, paintPrimary);
                if (measurementHeightText != null) {
                    canvas.save();
                    canvas.translate(rectSecondary.centerX() + measurementTextOffset,
                            (rectPrimary.bottom + rectSecondary.top) / 2 - (measurementHeightText.getHeight() / 2));
                    measurementHeightText.draw(canvas);
                    canvas.restore();
                }
            }
            if (rectPrimary.right < rectSecondary.left) {
                // secondary is right. draw horizontal line.
                canvas.drawLine(rectPrimary.right, rectSecondary.centerY(), rectSecondary.left, rectSecondary.centerY(), paintPrimary);
                if (measurementWidthText != null) {
                    canvas.save();
                    canvas.translate((rectPrimary.right + rectSecondary.left) / 2 - (measurementWidthText.getWidth() / 2),
                            rectSecondary.centerY() - measurementTextOffset - measurementWidthText.getHeight());
                    measurementWidthText.draw(canvas);
                    canvas.restore();
                }
            }
            if (rectSecondary.bottom < rectPrimary.top) {
                // secondary is above. draw vertical line.
                canvas.drawLine(rectSecondary.centerX(), rectPrimary.top, rectSecondary.centerX(), rectSecondary.bottom, paintPrimary);
                if (measurementHeightText != null) {
                    canvas.save();
                    canvas.translate(rectSecondary.centerX() + measurementTextOffset,
                            (rectPrimary.top + rectSecondary.bottom) / 2 - (measurementHeightText.getHeight() / 2));
                    measurementHeightText.draw(canvas);
                    canvas.restore();
                }
            }
            if (rectSecondary.right < rectPrimary.left) {
                // secondary is left. draw horizontal line.
                canvas.drawLine(rectPrimary.left, rectSecondary.centerY(), rectSecondary.right, rectSecondary.centerY(), paintPrimary);
                if (measurementWidthText != null) {
                    canvas.save();
                    canvas.translate((rectPrimary.left + rectSecondary.right) / 2 - (measurementWidthText.getWidth() / 2),
                            rectSecondary.centerY() - measurementTextOffset);
                    measurementWidthText.draw(canvas);
                    canvas.restore();
                }
            }

            // check nested
            Rect inside = null;
            Rect outside = null;
            if (rectPrimary.contains(rectSecondary)) {
                outside = rectPrimary;
                inside = rectSecondary;
            } else if (rectSecondary.contains(rectPrimary)) {
                outside = rectSecondary;
                inside = rectPrimary;
            }
            
            if (inside != null && outside != null) {
                // left inside
                canvas.drawLine(outside.left, inside.centerY(), inside.left, inside.centerY(), paintPrimary);
                if (measurementLeftText != null) {
                    canvas.save();
                    canvas.translate((outside.left + inside.left) / 2, inside.centerY() - measurementTextOffset - measurementLeftText.getHeight());
                    measurementLeftText.draw(canvas);
                    canvas.restore();
                }

                // right inside
                canvas.drawLine(outside.right, inside.centerY(), inside.right, inside.centerY(), paintPrimary);
                if (measurementRightText != null) {
                    canvas.save();
                    canvas.translate((outside.right + inside.right) / 2, inside.centerY() - measurementTextOffset - measurementRightText.getHeight());
                    measurementRightText.draw(canvas);
                    canvas.restore();
                }

                // top inside
                canvas.drawLine(inside.centerX(), outside.top, inside.centerX(), inside.top, paintPrimary);
                if (measurementTopText != null) {
                    canvas.save();
                    canvas.translate(inside.centerX() + measurementTextOffset,
                            (outside.top + inside.top) / 2 - (measurementTopText.getHeight() / 2));
                    measurementTopText.draw(canvas);
                    canvas.restore();
                }

                // bottom inside
                canvas.drawLine(inside.centerX(), outside.bottom, inside.centerX(), inside.bottom, paintPrimary);
                if (measurementBottomText != null) {
                    canvas.save();
                    canvas.translate(inside.centerX() + measurementTextOffset,
                            (outside.bottom + inside.bottom) / 2 - (measurementBottomText.getHeight() / 2));
                    measurementBottomText.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            View touchTarget = findTarget(contentRoot, x, y);

            // reset selection if target is root or same target.
            if (contentRoot == touchTarget || target.getTarget() == touchTarget) {
                target.setTarget(null);
                rectPrimary = null;
                rectSecondary = null;
            } else if (rectPrimary == null) {
                setPrimaryTarget(touchTarget);
            } else {
                setSecondaryTarget(touchTarget);
            }

            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    private View findTarget(View root, float x, float y) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                measurementHelper.getScreenLocation(this, child, outRect);
                if (x >= outRect.left && x <= outRect.right && y >= outRect.top && y <= outRect.bottom) {
                    return findTarget(child, x, y);
                }
            }
        }
        return root;
    }

    private void setPrimaryTarget(View view) {
        target.setTarget(view);
        rectPrimary = new Rect();
        measurementHelper.getScreenLocation(this, view, rectPrimary);

        setWidthMeasurementText(rectPrimary.width());
        setHeightMeasurementText(rectPrimary.height());
    }

    private void setSecondaryTarget(View view) {
        rectSecondary = new Rect();
        measurementHelper.getScreenLocation(this, view, rectSecondary);

        if (rectPrimary.bottom < rectSecondary.top) {
            setHeightMeasurementText(rectSecondary.top - rectPrimary.bottom);
        }
        if (rectPrimary.right < rectSecondary.left) {
            setWidthMeasurementText(rectSecondary.left - rectPrimary.right);
        }
        if (rectSecondary.bottom < rectPrimary.top) {
            setHeightMeasurementText(rectPrimary.top - rectSecondary.bottom);
        }
        if (rectSecondary.right < rectPrimary.left) {
            setWidthMeasurementText(rectPrimary.left - rectSecondary.right);
        }

        // check nested
        Rect inside = null;
        Rect outside = null;
        if (rectPrimary.contains(rectSecondary)) {
            outside = rectPrimary;
            inside = rectSecondary;
        } else if (rectSecondary.contains(rectPrimary)) {
            outside = rectSecondary;
            inside = rectPrimary;
        }

        if (inside != null && outside != null) {
            setLeftMeasurementText(inside.left - outside.left);
            setTopMeasurementText(inside.top - outside.top);
            setRightMeasurementText(outside.right - inside.right);
            setBottomMeasurementText(outside.bottom - inside.bottom);
        }
    }

    private void setWidthMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementWidthText = null;
            return;
        }
        Spannable widthText = new SpannableString(measurementHelper.toDp(measurement) + "dp");
        widthText.setSpan(new BackgroundColorSpan(Color.WHITE),
                0, widthText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int width = (int) paintText.measureText(widthText, 0, widthText.length());
        measurementWidthText = new StaticLayout(widthText, paintText, width,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);
    }

    private void setHeightMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementHeightText = null;
            return;
        }
        Spannable heightText = new SpannableString(measurementHelper.toDp(measurement) + "dp");
        heightText.setSpan(new BackgroundColorSpan(Color.WHITE),
                0, heightText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int width = (int) paintText.measureText(heightText, 0, heightText.length());
        measurementHeightText = new StaticLayout(heightText, paintText, width,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);
    }

    private void setLeftMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementLeftText = null;
            return;
        }
        Spannable text = new SpannableString(measurementHelper.toDp(measurement) + "dp");
        text.setSpan(new BackgroundColorSpan(Color.WHITE),
                0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int width = (int) paintText.measureText(text, 0, text.length());
        measurementLeftText = new StaticLayout(text, paintText, width,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);
    }

    private void setTopMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementTopText = null;
            return;
        }
        Spannable text = new SpannableString(measurementHelper.toDp(measurement) + "dp");
        text.setSpan(new BackgroundColorSpan(Color.WHITE),
                0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int width = (int) paintText.measureText(text, 0, text.length());
        measurementTopText = new StaticLayout(text, paintText, width,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);
    }

    private void setRightMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementRightText = null;
            return;
        }
        Spannable text = new SpannableString(measurementHelper.toDp(measurement) + "dp");
        text.setSpan(new BackgroundColorSpan(Color.WHITE),
                0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int width = (int) paintText.measureText(text, 0, text.length());
        measurementRightText = new StaticLayout(text, paintText, width,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);
    }

    private void setBottomMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementBottomText = null;
            return;
        }
        Spannable text = new SpannableString(measurementHelper.toDp(measurement) + "dp");
        text.setSpan(new BackgroundColorSpan(Color.WHITE),
                0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int width = (int) paintText.measureText(text, 0, text.length());
        measurementBottomText = new StaticLayout(text, paintText, width,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);
    }
}