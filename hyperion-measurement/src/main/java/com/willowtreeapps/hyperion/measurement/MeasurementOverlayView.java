package com.willowtreeapps.hyperion.measurement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.willowtreeapps.hyperion.plugin.v1.ExtensionProvider;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;

class MeasurementOverlayView extends FrameLayout {

    private final ViewGroup contentRoot;
    private final MeasurementHelper measurementHelper;
    private final Path path = new Path();
    private final Rect outRect = new Rect();
    private final Paint paintDashed;
    private final Paint paintPrimary;
    private final Paint paintSecondary;
    private final TextPaint paintText;
    private final @Px
    int measurementTextOffset;

    private View currentView;
    private Rect rectPrimary;
    private Rect rectSecondary;
    private TextView measurementWidthText;
    private TextView measurementHeightText;
    /* Used for nested measurementHelper */
    private TextView measurementLeftText;
    private TextView measurementTopText;
    private TextView measurementRightText;
    private TextView measurementBottomText;

    MeasurementOverlayView(Context context) {
        super(context);
        PluginExtension extension = ExtensionProvider.get(context);
        contentRoot = extension.getContentRoot();
        measurementHelper = extension.getMeasurementHelper();

        paintDashed = new Paint();
        paintDashed.setColor(ContextCompat.getColor(context, R.color.hype_blue));
        paintDashed.setStyle(Paint.Style.STROKE);
        paintDashed.setStrokeWidth(4f);
        paintDashed.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));

        paintPrimary = new Paint();
        paintPrimary.setColor(ContextCompat.getColor(context, R.color.hype_blue));
        paintPrimary.setStyle(Paint.Style.STROKE);
        paintPrimary.setStrokeWidth(6f);

        paintSecondary = new Paint();
        paintSecondary.setColor(ContextCompat.getColor(context, R.color.hype_blue));
        paintSecondary.setStyle(Paint.Style.STROKE);
        paintSecondary.setStrokeWidth(6f);

        paintText = new TextPaint();
        paintText.setColor(ContextCompat.getColor(context, R.color.hype_blue));
        paintText.setTextSize(45);
        paintText.setStyle(Paint.Style.FILL_AND_STROKE);
        paintText.setStrokeWidth(2);

        measurementTextOffset = getResources().getDimensionPixelSize(R.dimen.hm_measurement_text_offset);

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rectPrimary == null) {
            return;
        }

        //rectangle around target
        canvas.drawRect(rectPrimary, paintPrimary);

        /*
         * Start guidelines
         */
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
        /*
         * End guidelines
         */

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

            /*
             * Start measurement views
             */
            if (inside != null && outside != null) {
                // left inside
                canvas.drawLine(outside.left, inside.centerY(), inside.left, inside.centerY(), paintPrimary);
                if (measurementLeftText != null) {
                    canvas.save();
                    canvas.translate((outside.left + inside.left) / 2 - measurementLeftText.getWidth() / 2,
                            inside.centerY() - measurementLeftText.getHeight() / 2);
                    measurementLeftText.draw(canvas);
                    canvas.restore();
                }

                // right inside
                canvas.drawLine(outside.right, inside.centerY(), inside.right, inside.centerY(), paintPrimary);
                if (measurementRightText != null) {
                    canvas.save();
                    canvas.translate((outside.right + inside.right) / 2 - measurementRightText.getWidth() / 2,
                            inside.centerY() - measurementRightText.getHeight() / 2);
                    measurementRightText.draw(canvas);
                    canvas.restore();
                }

                // top inside
                canvas.drawLine(inside.centerX(), outside.top, inside.centerX(), inside.top, paintPrimary);
                if (measurementTopText != null) {
                    canvas.save();
                    canvas.translate(inside.centerX() - measurementTopText.getWidth() / 2,
                            (outside.top + inside.top) / 2 - measurementTopText.getHeight() / 2);
                    measurementTopText.draw(canvas);
                    canvas.restore();
                }

                // bottom inside
                canvas.drawLine(inside.centerX(), outside.bottom, inside.centerX(), inside.bottom, paintPrimary);
                if (measurementBottomText != null) {
                    canvas.save();
                    canvas.translate(inside.centerX() - measurementBottomText.getWidth() / 2,
                            (outside.bottom + inside.bottom) / 2 - measurementBottomText.getHeight() / 2);
                    measurementBottomText.draw(canvas);
                    canvas.restore();
                }
            }
            /*
             * End measurement views
             */
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
            if (contentRoot == touchTarget || currentView == touchTarget) {
                currentView = null;
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
        // we consider the "best target" to be the view width the smallest width / height
        // whose location on screen is within the given touch area.
        View bestTarget = root;
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                measurementHelper.getScreenLocation(child, outRect);
                if (child.getVisibility() != VISIBLE) {
                    continue;
                }
                if (x >= outRect.left && x <= outRect.right && y >= outRect.top && y <= outRect.bottom) {
                    final View target = findTarget(child, x, y);
                    if (target.getWidth() <= bestTarget.getWidth() && target.getHeight() <= bestTarget.getHeight()) {
                        bestTarget = target;
                    }
                }
            }
        }
        return bestTarget;
    }

    private void setPrimaryTarget(View view) {
        currentView = view;
        rectPrimary = new Rect();
        measurementHelper.getScreenLocation(view, rectPrimary);

        setWidthMeasurementText(rectPrimary.width());
        setHeightMeasurementText(rectPrimary.height());
    }

    private void setSecondaryTarget(View view) {
        rectSecondary = new Rect();
        measurementHelper.getScreenLocation(view, rectSecondary);

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
        measurementWidthText = makeMeasurementView(measurement);
    }

    private void setHeightMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementHeightText = null;
            return;
        }
        measurementHeightText = makeMeasurementView(measurement);
    }

    private void setLeftMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementLeftText = null;
            return;
        }
        measurementLeftText = makeMeasurementView(measurement);
    }

    private void setTopMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementTopText = null;
            return;
        }
        measurementTopText = makeMeasurementView(measurement);
    }

    private void setRightMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementRightText = null;
            return;
        }
        measurementRightText = makeMeasurementView(measurement);
    }

    private void setBottomMeasurementText(@Px int measurement) {
        if (measurement <= 0) {
            measurementBottomText = null;
            return;
        }
        measurementBottomText = makeMeasurementView(measurement);
    }

    /**
     * Prepares a TextView to display a measurement
     * Centers text
     * Sets text with "dp" suffix
     * Sets selection color as text color
     * Sets TextView background
     * Lays out view
     *
     * @param measurement The measurement to display in the created TextView
     */
    private TextView makeMeasurementView(@Px int measurement) {
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.CENTER);
        String text = measurementHelper.toDp(measurement) + "dp";
        tv.setText(text);
        tv.setTextColor(getResources().getColor(R.color.hype_blue));
        tv.setBackgroundResource(R.drawable.hm_rounded_measurement);
        layoutTextView(tv);
        return tv;
    }

    /**
     * Lays out textview to (text dimensions + padding) * system font scale
     * Padding based on screen density
     *
     * @param tv The TextView to layout
     */
    private void layoutTextView(TextView tv) {
        final float fontScale = getResources().getConfiguration().fontScale;

        /*
         * Multiplier for padding based on phone density
         * Density of 2 does not need padding
         */
        final float densityMultiplier = getContext().getResources().getDisplayMetrics().density - 2;

        Rect bounds = new Rect();
        paintText.getTextBounds(tv.getText().toString().toCharArray(), 0, tv.getText().length(), bounds);
        tv.layout(0, 0, (int) ((bounds.width() + (33 * densityMultiplier)) * fontScale),
                (int) ((bounds.height() + (22 * densityMultiplier)) * fontScale));
    }
}