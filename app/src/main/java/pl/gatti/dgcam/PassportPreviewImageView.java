package pl.gatti.dgcam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PassportPreviewImageView extends ImageView {
    Bitmap bitmap;

    public PassportPreviewImageView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public PassportPreviewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

    }

    public PassportPreviewImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.bitmap = bitmap;
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
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        canvas.drawColor(0xBF666666, PorterDuff.Mode.ADD);
        canvas.drawCircle(cx, cy, radius, paint);
//
//        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//        //Destination RectF sized to the camera picture
//        RectF dst = new RectF(0, 0, myImage.getWidth(), myImage.getHeight());
//
//        canvas.drawBitmap(bitmap, cx, cy, radius, null);
    }
}