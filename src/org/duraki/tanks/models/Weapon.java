package org.duraki.tanks.models;
import org.duraki.tanks.controllers.Controller;

import java.math.*;
/**
 * Created by artemsamsonov on 14.12.14.
 */
public class Weapon extends Sprite implements Runnable {
    private Integer damagePoints;
    public double ang;
    private Double cos;
    private Double sin;
    private boolean enemy;


    public Weapon(Double x, Integer y, double ang, Boolean enemy){
        life = true;
        this.x = x;
        this.y = y;
        this.enemy  = enemy;
        sin = Math.sin(ang);
        cos = Math.cos(ang);
    }
    @Override
    public void run() {
        float t = 0;
        Double sx, yx;
        x = (getX() + 70 + 58 * (this.cos).intValue()/10);
        y = (getY() + 42 - 63 * (this.sin).intValue());
        while(life) {
            sx = WEAPONSPEED * this.cos * t;
            yx = -WEAPONSPEED * this.sin * t + GRAVITY * t * t / 2;
            setX(getX() + sx);
            setY(getY() + yx.intValue());
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (y > DISPLAY_HEIGHT-BACKGROUND_HEIGHT) life = false;
            if (x < 0 || x > DISPLAY_WIDTH) life = false;
            if (y < WALL_HEIGHT && x > WALL_X && x < (WALL_X+WALL_WIDTH)) life = false;
            if (x < 0 || x > DISPLAY_WIDTH) life = false;
            t += 0.01;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public Integer getDamagePoints() {
        return damagePoints;
    }

    public void setDamagePoints(Integer damagePoints) {
        this.damagePoints = damagePoints;
    }


    public boolean getEnemy() {
        return enemy;
    }
}
