package activitytest.example.com.book;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by haha on 2017-08-26.
 */

public class MainMenu extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    int identi;     //用户类型
    MainActivity mainActivity;
    Paint paint;
    SurfaceHolder sfh;
    Thread th;
    boolean flag;
    Canvas canvas;
    //按钮图标
    Bitmap bmpselect;   //查询
    Bitmap bmpyuyue;    //预约
    Bitmap bmploss;    //挂失
    Bitmap bmpback;     //推出
    Bitmap bmplogout;   //推出
    Bitmap bmpabout;    //古语按钮
    Bitmap bmphelp;     //帮助
    Bitmap bmpbackground;   //背景
    Bitmap bmpuserset;      //用户管理
    Bitmap bmpbookset;      //书籍管理
    Bitmap bmpadminset;
    //按钮类
    HButton btn_select, btn_yuyue, btn_loss, btn_back, btn_logout, btn_about, btn_help ,btn_userset,
    btn_bookset, btn_adminset;

    public MainMenu(MainActivity activity) {
        super(activity);
        this.mainActivity = activity;
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        initbmp();
    }

    private void initbmp() {
        identi = mainActivity.identity;
        //图片资源
        bmpselect = BitmapFactory.decodeResource(getResources(), R.mipmap.select);
        bmpyuyue = BitmapFactory.decodeResource(getResources(), R.mipmap.order);
        bmploss = BitmapFactory.decodeResource(getResources(), R.mipmap.lose);
        bmpback = BitmapFactory.decodeResource(getResources(), R.mipmap.exit);
        bmplogout = BitmapFactory.decodeResource(getResources(), R.mipmap.logout);
        bmpabout = BitmapFactory.decodeResource(getResources(), R.mipmap.about);
        bmphelp = BitmapFactory.decodeResource(getResources(), R.mipmap.help);
        bmpbackground = BitmapFactory.decodeResource(getResources(), R.mipmap.backg1);
        bmpbookset = BitmapFactory.decodeResource(getResources(), R.mipmap.tushuguanli);
        bmpuserset = BitmapFactory.decodeResource(getResources(), R.mipmap.xueshengguanli);
        bmpadminset = BitmapFactory.decodeResource(getResources(), R.mipmap.admin);

        if (identi == 0) {
            btn_select = new HButton(bmpselect, Constant.screenW / 3 - bmpselect.getWidth(), Constant.screenH / 5);
            btn_loss = new HButton(bmploss, Constant.screenW / 3 + 100, Constant.screenH / 5);
            btn_yuyue = new HButton(bmpyuyue, Constant.screenW / 3 * 2, Constant.screenH / 5);

            btn_logout = new HButton(bmplogout, Constant.screenW / 3 - bmpselect.getWidth(), Constant.screenH / 5 * 2);
            btn_about = new HButton(bmpabout, Constant.screenW / 3 + 100, Constant.screenH / 5 * 2);
            btn_help = new HButton(bmphelp, Constant.screenW / 3 * 2, Constant.screenH / 5 * 2);

            btn_back = new HButton(bmpback, Constant.screenW / 3 - bmpselect.getWidth(), Constant.screenH / 5 * 3);
        }else if (identi == 1){

            btn_select = new HButton(bmpselect, Constant.screenW/3 - bmpselect.getWidth() , Constant.screenH/5);
            btn_loss = new HButton(bmploss, Constant.screenW/3 + 100, Constant.screenH/5);
            btn_yuyue = new HButton(bmpyuyue, Constant.screenW/3 * 2, Constant.screenH/5);

            btn_logout = new HButton(bmplogout, Constant.screenW/3 - bmpselect.getWidth(), Constant.screenH/5 * 2);
            btn_about = new HButton(bmpabout, Constant.screenW/3 + 100, Constant.screenH/5 * 2);
            btn_help = new HButton(bmphelp, Constant.screenW/3 * 2 , Constant.screenH/5 * 2);


            btn_userset = new HButton(bmpuserset,Constant.screenW/3 - bmpselect.getWidth(), Constant.screenH/5 * 3);
            btn_bookset = new HButton(bmpbookset, Constant.screenW/3 + 100, Constant.screenH/5 * 3);
            btn_back = new HButton(bmpback, Constant.screenW/3, Constant.screenH/5 * 3);
        } else {
            btn_select = new HButton(bmpselect, Constant.screenW/3 - bmpselect.getWidth() , Constant.screenH/5);
            btn_loss = new HButton(bmploss, Constant.screenW/3 + 100, Constant.screenH/5);
            btn_yuyue = new HButton(bmpyuyue, Constant.screenW/3 * 2, Constant.screenH/5);

            btn_logout = new HButton(bmplogout, Constant.screenW/3 - bmpselect.getWidth(), Constant.screenH/5 * 2);
            btn_about = new HButton(bmpabout, Constant.screenW/3 + 100, Constant.screenH/5 * 2);
            btn_help = new HButton(bmphelp, Constant.screenW/3 * 2 , Constant.screenH/5 * 2);


            btn_userset = new HButton(bmpuserset,Constant.screenW/3 - bmpselect.getWidth(), Constant.screenH/5 * 3);
            btn_bookset = new HButton(bmpbookset, Constant.screenW/3 + 100, Constant.screenH/5 * 3);
            btn_adminset = new HButton(bmpadminset, Constant.screenW/3 * 2, Constant.screenH/5 * 3);

            btn_back = new HButton(bmpback, Constant.screenW/3 - bmpadminset.getWidth(), Constant.screenH/5 * 4);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        flag = true;
        th = new Thread(this);
        th.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        synchronized (this) {
            flag = false;
        }
    }

    public void myDraw() {
        synchronized (this) {
            try {
                canvas = sfh.lockCanvas();
                if (canvas != null) {
                    canvas.save();
                    float scalex = (float) Constant.screenW / bmpbackground.getWidth();
                    float scaley = (float) Constant.screenH / bmpbackground.getHeight();
                    canvas.scale(scalex, scaley, 0, 0);
                    canvas.drawBitmap(bmpbackground, 0, 0, paint);
                    canvas.restore();
                    btn_select.draw(canvas, paint);
                    btn_loss.draw(canvas, paint);
                    btn_yuyue.draw(canvas, paint);
                    btn_logout.draw(canvas, paint);
                    btn_about.draw(canvas, paint);
                    btn_help.draw(canvas, paint);
                    btn_back.draw(canvas, paint);
                    if (identi == 1) {
                        btn_userset.draw(canvas, paint);
                        btn_bookset.draw(canvas, paint);
                    }
                    if (identi == 2) {
                        btn_userset.draw(canvas, paint);
                        btn_bookset.draw(canvas, paint);
                        btn_adminset.draw(canvas, paint);
                    }
                }
            } catch (Exception e) {

            } finally {
                if (canvas != null) {
                    sfh.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        if (identi == 0){
            if (btn_select.touch(event)){
                mainActivity.handler.sendEmptyMessage(1);   //查询
            }
            if (btn_loss.touch(event)){
                mainActivity.handler.sendEmptyMessage(2);
            }
            if (btn_yuyue.touch(event)){
                mainActivity.handler.sendEmptyMessage(3);
            }
            if (btn_logout.touch(event)){
                mainActivity.handler.sendEmptyMessage(0);
            }
            if (btn_back.touch(event)){
                mainActivity.exit();
            }
            if (btn_help.touch(event)){
                mainActivity.handler.sendEmptyMessage(4);
            }
            if (btn_about.touch(event)){
                mainActivity.handler.sendEmptyMessage(5);
            }
        }else if (identi == 1){
            if (btn_select.touch(event)){
                mainActivity.handler.sendEmptyMessage(1);   //查询
            }
            if (btn_loss.touch(event)){
                mainActivity.handler.sendEmptyMessage(2);
            }
            if (btn_yuyue.touch(event)){
                mainActivity.handler.sendEmptyMessage(3);
            }
            if (btn_logout.touch(event)){
                mainActivity.handler.sendEmptyMessage(0);
            }
            if (btn_back.touch(event)){
                mainActivity.exit();
            }
            if (btn_help.touch(event)){
                mainActivity.handler.sendEmptyMessage(4);
            }
            if (btn_about.touch(event)){
                mainActivity.handler.sendEmptyMessage(5);
            }
            if (btn_bookset.touch(event)){
                mainActivity.handler.sendEmptyMessage(6);
            }
            if (btn_userset.touch(event)){
                mainActivity.handler.sendEmptyMessage(7);
            }
        }else if (identi == 2){
            if (btn_select.touch(event)){
                mainActivity.handler.sendEmptyMessage(1);   //查询
            }
            if (btn_loss.touch(event)){
                mainActivity.handler.sendEmptyMessage(2);
            }
            if (btn_yuyue.touch(event)){
                mainActivity.handler.sendEmptyMessage(3);
            }
            if (btn_logout.touch(event)){
                mainActivity.handler.sendEmptyMessage(0);
            }
            if (btn_back.touch(event)){
                mainActivity.exit();
            }
            if (btn_help.touch(event)){
                mainActivity.handler.sendEmptyMessage(4);
            }
            if (btn_about.touch(event)){
                mainActivity.handler.sendEmptyMessage(5);
            }
            if (btn_bookset.touch(event)){
                mainActivity.handler.sendEmptyMessage(6);
            }
            if (btn_userset.touch(event)){
                mainActivity.handler.sendEmptyMessage(7);
            }
            if (btn_adminset.touch(event)){
                mainActivity.handler.sendEmptyMessage(8);
            }
        }
        return true;
    }

    public void logic(){

    }

    @Override
    public void run() {
        while (flag){
            long start = System.currentTimeMillis();
            myDraw();
            logic();
            long end = System.currentTimeMillis();
            if (end - start < 50){
                try {
                    Thread.sleep(50- (end - start));
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
