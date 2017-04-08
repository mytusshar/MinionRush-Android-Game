package com.example.mytusshar.minionrush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.mytusshar.minionrush.sqlite.DataBaseOperation;

import java.util.Calendar;
import java.util.Random;

public class GameView extends View {
    
    private static final int SLEEP_TIME = 50;
    public LogicManager logicManager;
    public ObjectsManager objectsManager;
    private Paint paint;
    public static boolean isGameOver;
    CharacterJumpActivity characterJumpActivity;
    private DataBaseOperation dataBaseOperation;
    public static boolean ispause = false;
    int save_time = 0;
    private boolean isgamerunning = true;
    private Animation alphAnimation;

////////////////////////////////////////////////////////////////////////////////////////////////////
    public void startEntryAnim(){
        alphAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphAnimation.setDuration(Constants.ANIMATION_TIME);
        startAnimation(alphAnimation);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public GameView(CharacterJumpActivity context) {
        super(context);
        logicManager = new LogicManager(context);
        objectsManager = new ObjectsManager(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        isGameOver = false;
        this.characterJumpActivity = context;
        dataBaseOperation = new DataBaseOperation(context);
        ispause = false;
        new Thread(new GameThread()).start();
        startEntryAnim();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#FF979899"));
        try{
            logicManager.drawAndroidAndBars(canvas);
        }catch (Exception e) { }
        drawHeader(canvas);

        if(ispause){
            drawPause(canvas);
        }
        super.onDraw(canvas);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private void drawPause(Canvas canvas) {
        // drawing pause box
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(Color.parseColor("#FF000000"));//FF406E12
        paint.setAlpha(150);
        // on pause background
        canvas.drawRect(0, 0, CharacterJumpActivity.screen_width, CharacterJumpActivity.screen_height, paint);
        paint.setColor(Color.parseColor("#FFFDD835"));//FFFDF256
        paint.setAlpha(255);
        float Y = CharacterJumpActivity.screen_height/2;
        float X = CharacterJumpActivity.screen_width/2;
        // drawing options box
        canvas.drawRect(X-100*CharacterJumpActivity.scaled_density, Y-100*CharacterJumpActivity.scaled_density, X+100*CharacterJumpActivity.scaled_density, Y+100*CharacterJumpActivity.scaled_density, paint);

        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setTextSize(20 * CharacterJumpActivity.scaled_density);
        paint2.setColor(Color.parseColor("#000000"));
        canvas.drawText("RESUME", X - 35* CharacterJumpActivity.scaled_density, Y - 35*CharacterJumpActivity.scaled_density, paint2);
        canvas.drawText("EXIT", X - 18*CharacterJumpActivity.scaled_density, Y + 35*CharacterJumpActivity.scaled_density, paint2);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    // in game view Header the Semi-green rectangle
    private void drawHeader(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(Color.parseColor("#FF406E12"));
        paint.setAlpha(50);
        canvas.drawRect(0, 0, CharacterJumpActivity.screen_width, CharacterJumpActivity.screen_height/16, paint);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!ispause){
                //this fires BULLETS
                logicManager.addNewBulletSet();
            }
            else{
                Paint paint = new Paint();
                paint.setAntiAlias(false);
                paint.setColor(Color.parseColor("#000000"));
                paint.setAlpha(50);
                // receives action from the button when game is pause
                float x = event.getX();
                float y = event.getY();
                float X = CharacterJumpActivity.screen_width/2;
                float Y = CharacterJumpActivity.screen_height/2;
                if(x>X-45*CharacterJumpActivity.scaled_density && x<X+50*CharacterJumpActivity.scaled_density
                        && y>Y-65*CharacterJumpActivity.scaled_density && y<Y-20*CharacterJumpActivity.scaled_density)
                {
                    ispause = false;
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {}
                }
                if(x>X-45*CharacterJumpActivity.scaled_density && x<X+50* CharacterJumpActivity.scaled_density
                        && y>Y+10*CharacterJumpActivity.scaled_density && y<Y+45*CharacterJumpActivity.scaled_density)
                {
                    LogicManager.is_running = false;
                    isgamerunning = false;
                    characterJumpActivity.handler.sendEmptyMessage(CharacterJumpActivity.WELCOME);
                    GameView.ispause = false;
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {}
                }
            }
        }
        return super.onTouchEvent(event);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
//    private String GetDate() {
//        Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        int mHour = c.get(Calendar.HOUR_OF_DAY);
//        int mMinute = c.get(Calendar.MINUTE);
//        return ""+mYear+"-"+mMonth+"-"+mDay+" "+mHour+":"+mMinute;
//    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private class GameThread implements Runnable{
        @Override
        public void run() {
            while(isgamerunning){
                try {
                    Thread.sleep(GameView.SLEEP_TIME);
                } catch (Exception e) {}
                postInvalidate();

                if(isGameOver == true){
                    if(save_time == 0)
                        //dataBaseOperation.SaveHeight(ObjectsManager.score, GetDate());
                        dataBaseOperation.SaveHeight(ObjectsManager.score);


                    save_time ++;
                    isgamerunning = false;
                    logicManager.clear();
                    characterJumpActivity.handler.sendEmptyMessage(CharacterJumpActivity.GAME_OVER);
                }
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
}

