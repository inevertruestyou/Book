package activitytest.example.com.book;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by haha on 2017-08-27.
 */

public class HButton {
    Bitmap bmp;
    int x, y, w, h;

    public HButton(Bitmap bmp, int x, int y) {
        this.bmp = bmp;
        this.x = x;
        this.y = y;
        this.w = bmp.getWidth();
        this.h = bmp.getHeight();
    }

    public boolean touch(MotionEvent event){
        int pointx = (int) event.getX();
        int pointy = (int) event.getY();
        if (pointx > x && pointx < x + w){
            if (pointy > y && pointy < y + h){
                return true;
            }
        }
        return false;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bmp, x, y, paint);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
