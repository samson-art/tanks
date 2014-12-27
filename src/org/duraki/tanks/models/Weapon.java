package org.duraki.tanks.models;

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
        while(life) {
            sx = WEAPONSPEED * this.cos * t;
            yx = -WEAPONSPEED * this.sin * t + GRAVITY * t * t / 2;
            Double x = getX()+sx;
            Double y = getY()+yx;
            if (y > DISPLAY_HEIGHT-BACKGROUND_HEIGHT) life = false;
            if (x < 0 || x > DISPLAY_WIDTH) life = false;
            if (y > (DISPLAY_HEIGHT-WALL_HEIGHT-BACKGROUND_HEIGHT-WEAPON_HEIGHT) && (x + WEAPON_WIDTH) > WALL_X && x < (WALL_X+WALL_WIDTH)) life = false;
            if (x < 0 || x > DISPLAY_WIDTH) life = false;
            setX(getX() + sx);
            setY(getY() + yx.intValue());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t += 0.01;
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
