package com.hikcreate.library.app.ui.view.pointradio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * 带红点提醒的RadioButton
 *
 * @author gongwei 2018/12/24.
 */
public class PointRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    private float textSize = 10f;
    private float textPadding = 4f;
    private float textMargin = 10f;

    private Paint paint, paintText;
    private float radius;
    private boolean enabledPoint;
    private String text;

    public PointRadioButton(Context context) {
        super(context);
        init();
    }

    public PointRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.WHITE);
    }

    public void showPoint(boolean enabled) {
        enabledPoint = enabled;
        invalidate();
    }

    public boolean isEnabledPoint() {
        return enabledPoint;
    }

    public void showPoint(long num) {
        if (num > 0) {
            showPoint(true, num > 99 ? "99+" : String.valueOf(num));
        } else {
            showPoint(false);
        }
    }

    public void showPoint(boolean enabled, String text) {
        enabledPoint = enabled;
        this.text = text;
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        textPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        textMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        paintText.setTextSize(textSize);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (enabledPoint) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            float padding = radius;
            if (text == null) {
                canvas.drawCircle(getWidth() / 2 + radius * 3, padding, radius, paint);
            } else {
                float textWidth = paintText.measureText(text);
                float redStart = getWidth() / 2 + textMargin;
                float redWidth = textWidth + textPadding * 2;
                float textLeft = redStart + textPadding;
                Paint.FontMetricsInt fontMetrics = paintText.getFontMetricsInt();
                float baseline = (radius * 2 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top - 2;
                if (textWidth < radius * 2 - textPadding * 2) {
                    textLeft = redStart + (2 * radius - textWidth) / 2;
                    canvas.drawCircle(redStart + radius, padding, radius, paint);
                    canvas.drawText(text, textLeft, baseline, paintText);
                } else {
                    canvas.drawCircle(redStart + radius, padding, radius, paint);
                    canvas.drawCircle(redStart + redWidth - 2 * textPadding, padding, radius, paint);
                    canvas.drawRect(redStart + radius, 0, redStart + redWidth - 2 * textPadding, radius * 2, paint);
                    canvas.drawText(text, textLeft, baseline, paintText);
                }
            }
        }
    }
}
