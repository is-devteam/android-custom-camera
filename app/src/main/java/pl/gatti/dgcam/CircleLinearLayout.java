package pl.gatti.dgcam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by pivotal on 10/31/14.
 */
public class CircleLinearLayout extends LinearLayout {
    public CircleLinearLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public CircleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public CircleLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cx, cy, radius;
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        radius = getHeight() / 2;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        canvas.drawColor(0xBF666666, PorterDuff.Mode.ADD);
        canvas.drawCircle(cx, cy, radius, paint);
    }
}
