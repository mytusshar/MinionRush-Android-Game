package com.example.mytusshar.minionrush;

/**
 * Created by mytusshar on 9/11/2016.
 */

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.mytusshar.minionrush.Items.AbstractItem;
import com.example.mytusshar.minionrush.Items.ItemBullet;
import com.example.mytusshar.minionrush.Items.ItemFruit;
import com.example.mytusshar.minionrush.Items.ItemUpgradeBullet;

import com.example.mytusshar.minionrush.gamecharacter.AbstractGameCharacter;
import com.example.mytusshar.minionrush.monsters.AbstractMonster;
import com.example.mytusshar.minionrush.monsters.EatingHead;
import com.example.mytusshar.minionrush.monsters.RotateMonster;
import com.example.mytusshar.minionrush.otherviews.OptionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ObjectsManager {

    public static long score;
    public int monster_appear_rate = 80;
    public int item_appear_rate = 75;
    private int monsters_appear = 0;
    public int item_appear = 0;
    public Map<String, AbstractMonster> monsterMap;
    private long monster_identifier = 0;
    private CharacterJumpActivity context;
    private Paint paint;
    SharedPreferences get_Difficulty_Setting;
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public ObjectsManager(CharacterJumpActivity context){
        this.context = context;
        monsterMap = new HashMap<String, AbstractMonster>();
        initPaint();
        score = 0;
        setDifficulty(context);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20* CharacterJumpActivity.scaled_density);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setDifficulty(CharacterJumpActivity context) {
        get_Difficulty_Setting = context.getSharedPreferences(OptionView.PREFS_DIFF, 0);
        String diffString = get_Difficulty_Setting.getString(OptionView.PREFS_DIFF, "Tush");
        if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_TUSH)){

            monster_appear_rate = 50;
            item_appear_rate = 70;
        }
        else if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_NORMAL)){

            monster_appear_rate = 10;
            item_appear_rate = 70;
        }
        else if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_MASTER)){

            monster_appear_rate = 10;
            item_appear_rate = 80;
        }
        else{

            monster_appear_rate = 10;
            item_appear_rate = 95;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public void clear(){
        monsterMap.clear();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean hasMonster() {
        int temp = new Random().nextInt(100);
        if(temp > monster_appear_rate)
            return true;
        return false;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    //draws new bars and monsters on gameView
    // when minion starts jumping
    public void drawBarsAndMonsters(Canvas canvas){

        for(String key : monsterMap.keySet()){
            monsterMap.get(key).drawSelf(canvas);
        }
        drawHeight(canvas);
        removeOuterBarsAndMonsters();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private void drawHeight(Canvas canvas) {
        canvas.drawText(""+score, 5*CharacterJumpActivity.width_factor, 20*CharacterJumpActivity.scaled_density, paint);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public void moveBarsAndMonstersDown(){
        float vertical_speed ;
        float add ;
        //resolving different screen size issue
        if(CharacterJumpActivity.density <=2.3)
        {
            vertical_speed = -6f;
            add = 6f;
        }
        else if(CharacterJumpActivity.density >2.3 && CharacterJumpActivity.density <3)
        {
            vertical_speed = -9f;
            add = 9f;
        }
        else
        {
            vertical_speed = -12f;
            add = 12f;
        }

        //////////////////////////////////////////
        try{

            for(String key : monsterMap.keySet()){
                monsterMap.get(key).CoorY -= vertical_speed;
                monsterMap.get(key).CoorY += add;
            }
        }catch(Exception e){}
        score++;
        monsters_appear = item_appear += add - vertical_speed;
        addNewBarsAndMonsters();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private void removeOuterBarsAndMonsters() {
        List<String> temp = new ArrayList<String>();

        temp.clear();
        for (String key : monsterMap.keySet()) {
            if (monsterMap.get(key).CoorY > CharacterJumpActivity.screen_height + 20) {
                monsterMap.get(key).is_running = false;
                temp.add(key);
            }
        }
        for (String key : temp) {
            monsterMap.remove(key);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    //this is adding new bars at the top of gameView while game running
    public void addNewBarsAndMonsters(){
        float tempY = getTopCoorY();
        if(tempY > (60*CharacterJumpActivity.height_factor)){

            if(hasMonster() && monsters_appear >= 300*CharacterJumpActivity.height_factor){
                monsters_appear = 0;
                int temp2 = new Random().nextInt(100);
                if(temp2 < 40)
                    monsterMap.put(""+monster_identifier, new EatingHead(-10*CharacterJumpActivity.height_factor, context));
                else
                    monsterMap.put(""+monster_identifier, new RotateMonster(-10*CharacterJumpActivity.height_factor, context));

                monster_identifier ++;
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private float getTopCoorY() {
        float tempY = 100;
        try{
            for(String key : monsterMap.keySet()){
                if(monsterMap.get(key).CoorY - 25  * CharacterJumpActivity.height_factor <= tempY) {
                    tempY = -1111;
                }
            }
        }catch(Exception e){}
        return tempY;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private AbstractItem getRandomItem() {
        if(item_appear >= 400 * CharacterJumpActivity.height_factor){
            item_appear = 0;
            int temp = new Random().nextInt(100);
            if(temp > monster_appear_rate){
                temp = new Random().nextInt(100);
                if(temp < 10)
                    return new ItemUpgradeBullet(context);
                else if(temp < 30)
                    return new ItemBullet(context);
                else
                    return new ItemFruit(context);
            }
        }
        return null;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////
    public void checkIsTouchItems(AbstractGameCharacter gameCharacter){
        try{
            float temp_x = gameCharacter.LTCoorX + 22 * CharacterJumpActivity.height_factor;
            float temp_y = gameCharacter.LTCoorY + 22 * CharacterJumpActivity.height_factor;

        }catch (Exception e){}
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private int getLength(float temp_x, float temp_y, int x, float y) {
        double x_length = temp_x - x;
        double y_length = temp_y - y;
        return (int) Math.sqrt(x_length*x_length + y_length*y_length);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    public void checkIsTouchMonsters(AbstractGameCharacter gameCharacter) {
        for (String key : monsterMap.keySet()) {
            monsterMap.get(key).checkDistance(gameCharacter);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
}
