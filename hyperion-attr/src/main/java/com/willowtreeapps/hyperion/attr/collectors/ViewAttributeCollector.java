package com.willowtreeapps.hyperion.attr.collectors;

import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.MutableBooleanViewAttribute;
import com.willowtreeapps.hyperion.attr.MutableStringViewAttribute;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class ViewAttributeCollector extends TypedAttributeCollector<View> {

    public ViewAttributeCollector() {
        super(View.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(final View view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        Rect rect = new Rect();
        String value;

        attributes.add(new ViewAttribute<>("Id", new ResourceValue(view.getResources(), view.getId())));

        value = attributeTranslator.translatePx(view.getHeight());
        attributes.add(new ViewAttribute<>("Height", value));

        value = attributeTranslator.translatePx(view.getWidth());
        attributes.add(new ViewAttribute<>("Width", value));

        view.getGlobalVisibleRect(rect);

        attributes.add(new ViewAttribute<>("GlobalLeft", String.valueOf(rect.left)));
        attributes.add(new ViewAttribute<>("GlobalTop", String.valueOf(rect.top)));
        attributes.add(new ViewAttribute<>("GlobalRight", String.valueOf(rect.right)));
        attributes.add(new ViewAttribute<>("GlobalBottom", String.valueOf(rect.bottom)));

        view.getLocalVisibleRect(rect);

        attributes.add(new ViewAttribute<>("LocalLeft", String.valueOf(rect.left)));
        attributes.add(new ViewAttribute<>("LocalTop", String.valueOf(rect.top)));
        attributes.add(new ViewAttribute<>("LocalRight", String.valueOf(rect.right)));
        attributes.add(new ViewAttribute<>("LocalBottom", String.valueOf(rect.bottom)));

        attributes.add(new MutableBooleanViewAttribute("Clickable", view.isClickable()) {
            @Override
            protected void mutate(Boolean value) {
                view.setClickable(value);
            }
        });
        attributes.add(new MutableBooleanViewAttribute("LongClickable", view.isLongClickable()) {
            @Override
            protected void mutate(Boolean value) {
                view.setLongClickable(value);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            attributes.add(new ViewAttribute<>("ContextClickable", view.isContextClickable()));
        }
        attributes.add(new MutableBooleanViewAttribute("Enabled", view.isEnabled()) {
            @Override
            protected void mutate(Boolean value) {
                view.setEnabled(value);
            }
        });
        attributes.add(new MutableBooleanViewAttribute("Activated", view.isActivated()) {
            @Override
            protected void mutate(Boolean value) {
                view.setActivated(value);
            }
        });
        attributes.add(new MutableBooleanViewAttribute("Selected", view.isSelected()) {
            @Override
            protected void mutate(Boolean value) {
                view.setSelected(value);
            }
        });
        attributes.add(new ViewAttribute<>("AttachedToWindow", ViewCompat.isAttachedToWindow(view)));
        attributes.add(new ViewAttribute<>("Dirty", view.isDirty()));

        attributes.add(new MutableBooleanViewAttribute("IsFocusable", view.isFocusable()) {
            @Override
            protected void mutate(Boolean value) {
                view.setFocusable(value);
            }
        });
        attributes.add(new MutableBooleanViewAttribute("IsFocusableInTouchMode", view.isFocusableInTouchMode()) {
            @Override
            protected void mutate(Boolean value) {
                view.setFocusableInTouchMode(value);
            }
        });
        attributes.add(new ViewAttribute<>("HasFocus", view.hasFocus()));
        attributes.add(new ViewAttribute<>("HasFocusable", view.hasFocusable()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            attributes.add(new ViewAttribute<>("HasTransientState", view.hasTransientState()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            attributes.add(new ViewAttribute<>("ForegroundGravity",
                    new GravityValue(view.getForegroundGravity())));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attributes.add(new MutableStringViewAttribute("TransitionName", view.getTransitionName()) {
                @Override
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                protected void mutate(CharSequence value) {
                    view.setTransitionName(value.toString());
                }
            });
            attributes.add(new ViewAttribute<>("Elevation", view.getElevation()));
        }

        attributes.add(new ViewAttribute<>("BackgroundTintMode",
                new PorterDuffModeValue(ViewCompat.getBackgroundTintMode(view))));

        attributes.add(Collectors.createColorAttribute(
                view, "BackgroundTint", ViewCompat.getBackgroundTintList(view)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            attributes.add(new ViewAttribute<>("AccessibilityClassName", view.getAccessibilityClassName()));
        }

        return attributes;
    }

}