package com.example.mytusshar.minionrush.gamecharacter;

/**
 * Created by mytusshar on 9/11/2016.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.example.mytusshar.minionrush.CharacterJumpActivity;
import com.example.mytusshar.minionrush.ObjectsManager;
import com.example.mytusshar.minionrush.R;

public class GameCharacter extends AbstractGameCharacter {

    private boolean is_hurt = false;
    private int  invisible_time = 0;
    public Bitmap normal_gameCharacter;
    public Bitmap hurt_gameCharacter;
    public Bitmap bullet;
    public Bitmap life_item;
    ObjectsManager objectsManager;
////////////////////////////////////////////////////////////////////////////////////////////////////
    public GameCharacter(CharacterJumpActivity context){
        //resolving different screen size issue
        if(CharacterJumpActivity.density <=2.3)
        {
            INITIAL_VERTICAL_SPEED = -10;
            MAX_VERTICAL_SPEED = 10;
        }
        else if(CharacterJumpActivity.density >2.3 && CharacterJumpActivity.density <3)
        {
            INITIAL_VERTICAL_SPEED = -12;
            MAX_VERTICAL_SPEED =12;
        }
        else
        {
            INITIAL_VERTICAL_SPEED            = -14;
            MAX_VERTICAL_SPEED                = 14;
        }
        //////////////////////////////////////////
        objectsManager = new ObjectsManager(context);
        this.LTCoorX = INITIAL_COORX;
        this.LTCoorY = INITIAL_COORY;
        this.horizontal_speed = 0;
        this.vertical_speed = INITIAL_VERTICAL_SPEED;
        accelerameter = DEFAULT_VERTICAL_ACCELERATE;
        this.life_num = INITIAL_LIFE_NUM;
        this.bullet_times = INITIAL_LUANCHER_BULLET_TIMES;

        initBitMap(context);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initBitMap(CharacterJumpActivity context){
        //normal_gameCharacter = new Bitmap[2];
        normal_gameCharacter = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.minion)).getBitmap();
        //normal_gameCharacter[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.minion_launch)).getBitmap();

        //hurt_gameCharacter = new Bitmap[2];
        hurt_gameCharacter = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.minion_hurt)).getBitmap();

        life_item = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.heart)).getBitmap();
        bullet = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bullet_item)).getBitmap();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void drawSelf(Canvas canvas) {
        if(!is_hurt)
            canvas.drawBitmap(normal_gameCharacter, LTCoorX, LTCoorY, null);
        else{
            if(invisible_time %2 == 0)
                canvas.drawBitmap(normal_gameCharacter, LTCoorX, LTCoorY, null);
            else
                canvas.drawBitmap(hurt_gameCharacter, LTCoorX, LTCoorY, null);
            invisible_time ++;
            if(invisible_time > 100){
                invisible_time = 0;
                is_hurt = false;
            }
        }
        drawLifeNumAndBulletTimes(canvas);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private void drawLifeNumAndBulletTimes(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20*CharacterJumpActivity.scaled_density);

        canvas.drawBitmap(bullet, CharacterJumpActivity.screen_width/3+120* CharacterJumpActivity.scaled_density, 5*CharacterJumpActivity.scaled_density, null);
        canvas.drawText("X "+bullet_times, CharacterJumpActivity.screen_width/3+140* CharacterJumpActivity.scaled_density, 20* CharacterJumpActivity.scaled_density, paint);

        canvas.drawBitmap(life_item,70*CharacterJumpActivity.scaled_density, 2, null);
        paint.setColor(Color.RED);
        canvas.drawRect(100*CharacterJumpActivity.scaled_density, 5*CharacterJumpActivity.scaled_density,
                200*CharacterJumpActivity.scaled_density ,
                23*CharacterJumpActivity.scaled_density, paint);
        if(is_hurt)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.GREEN);
        canvas.drawRect(100*CharacterJumpActivity.scaled_density, 5*CharacterJumpActivity.scaled_density,
                100*CharacterJumpActivity.scaled_density + (life_num+1)*25*CharacterJumpActivity.scaled_density,
                23*CharacterJumpActivity.scaled_density, paint);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void minusLife() {
        if(!is_hurt){
            is_hurt = true;
            this.life_num -= 1;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public static AbstractGameCharacter GameCharacterFactory(CharacterJumpActivity context){
        return new GameCharacter(context);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void move() {
        vertical_speed += accelerameter;
        LTCoorX += horizontal_speed;
        horizontal_speed = 0;

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkGameCharacterCoor(ObjectsManager oBjectsManager){
        // limits horizontal movement
        if(LTCoorX < 15)
            LTCoorX = 15;
        if(LTCoorX > CharacterJumpActivity.screen_width- 40*CharacterJumpActivity.width_factor)
            LTCoorX = CharacterJumpActivity.screen_width - 40*CharacterJumpActivity.width_factor;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
}
