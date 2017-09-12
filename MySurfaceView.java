package activitytest.example.com.book;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by haha on 2017-08-25.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    MainActivity mainActivity;
    private SurfaceHolder sfh;
    private Thread th;
    private boolean flag;
    private Paint paint;
    private Canvas canvas;
    int screenH, screenW;
    private Bitmap bmp1, bmp2, bmp3, bmp4, bmp5, bmp6, bmp7;
    double k = 0;


    public MySurfaceView(MainActivity activity) {
        super(activity);
        this.mainActivity = activity;
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);


        bmp1 = BitmapFactory.decodeResource(getResources(), R.mipmap.bg1);
        bmp2 = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);
        bmp3 = BitmapFactory.decodeResource(getResources(), R.mipmap.bg3);
        bmp4 = BitmapFactory.decodeResource(getResources(), R.mipmap.bg4);
        bmp5 = BitmapFactory.decodeResource(getResources(), R.mipmap.bg5);
        bmp6 = BitmapFactory.decodeResource(getResources(), R.mipmap.bg6);
        bmp7 = BitmapFactory.decodeResource(getResources(), R.mipmap.bg7);


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        screenH = this.getHeight();
        screenW = this.getWidth();
        flag = true;
        th = new Thread(this);
        th.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        flag = false;
    }


    public void myDraw(){
        try {
            canvas = sfh.lockCanvas();
            canvas.save();
            canvas.scale((float)Constant.screenW/bmp1.getWidth(), (float)Constant.screenH/bmp1.getHeight(), 1, 1);
            if (canvas != null){
                if (k >=0 && k < 1){
                    canvas.drawBitmap(bmp1, 0, 0, paint);
                }
            }
            if (canvas != null){
                if (k >=1 && k < 2){
                    canvas.drawBitmap(bmp2, 0, 0, paint);
                }
            }
            if (canvas != null){
                if (k >=2 && k < 3){
                    canvas.drawBitmap(bmp3, 0, 0, paint);
                }
            }
            if (canvas != null){
                if (k >=3 && k < 4){
                    canvas.drawBitmap(bmp4, 0, 0, paint);
                }
            }
            if (canvas != null){
                if (k >=4 && k < 5){
                    canvas.drawBitmap(bmp5, 0, 0, paint);
                }
            }
        }catch (Exception e){

        }finally {
            if (canvas != null){
                sfh.unlockCanvasAndPost(canvas);
            }
        }
    }


    public void logic(){

    }

    @Override
    public void run() {
        while (flag){
            long start = System.currentTimeMillis();
            k = k + 0.07;
            if (k > 5){
                mainActivity.handler.sendEmptyMessage(0);
            }
            myDraw();
            logic();
            long end = System.currentTimeMillis();
            if (end - start < 50){
                try {
                    Thread.sleep(50 - ( end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return false;
    }
}
