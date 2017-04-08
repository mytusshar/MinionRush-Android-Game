package com.example.mytusshar.minionrush.Items;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import com.example.mytusshar.minionrush.CharacterJumpActivity;
import com.example.mytusshar.minionrush.R;
import com.example.mytusshar.minionrush.gamecharacter.AbstractGameCharacter;

/**
 * Created by mytusshar on 8/17/2016.
 */
public class ItemUpgradeBullet extends AbstractItem {

    Bitmap Upgrade;

    public ItemUpgradeBullet(CharacterJumpActivity context){
        Upgrade = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.upgrade)).getBitmap();
    }

    @Override
    public void drawSelf(Canvas canvas, float CoorX, float CoorY) {
        canvas.drawBitmap(Upgrade, CoorX, CoorY, null);
    }

    @Override
    public void modifyGameCharacter(AbstractGameCharacter gameCharacter) {
        gameCharacter.bullet_times = 100;
    }

}
