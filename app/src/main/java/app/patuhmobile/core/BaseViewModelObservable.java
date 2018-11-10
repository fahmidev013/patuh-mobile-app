package app.patuhmobile.core;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;

import java.math.BigDecimal;

import app.patuhmobile.utils.StringUtils;

public abstract class BaseViewModelObservable extends BaseObservable {

    @BindingAdapter("android:text")
    public static void setText_Int(TextView view, int value) {
        view.setText(Integer.toString(value));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getText_Int(TextView view) {
        String numText = view.getText().toString();
        return !StringUtils.isStringNullOrEmpty(numText) ? Integer.parseInt(view.getText().toString()) : 0;
    }

    @BindingAdapter("android:text")
    public static void setText_Double(TextView view, Double value) {
        if (value != null) {
            view.setText(Double.toString(value));
        } else {
            view.setText(null);
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Double getText_Double(TextView view) {
        String numText = view.getText().toString();
        return !StringUtils.isStringNullOrEmpty(numText) ? Double.parseDouble(view.getText().toString()) : 0.0;
    }

    @BindingAdapter("android:text")
    public static void setText_BigDecimal(TextView view, BigDecimal value) {
        if (value != null) {
            view.setText(value.toString());
        } else {
            view.setText(null);
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static BigDecimal getText_BigDecimal(TextView view) {
        String numText = view.getText().toString();
        return !StringUtils.isStringNullOrEmpty(numText) ? new BigDecimal(numText) : new BigDecimal(0.0);
    }
}
