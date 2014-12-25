package org.duraki.tanks.models;

import org.eclipse.swt.graphics.Image;

/**
 * Created by artemsamsonov on 14.12.14.
 */
public class Tank  extends Sprite{

    private Double  ang;

    private final static Double speed = (double) 5;

    private Integer lifePoints = 100;
    private Boolean alive = true;

    public Tank(Integer x, Integer y, Double ang) {
        setX(x);
        setY(y);
        this.ang = ang;
    }

    public void damage(Weapon weapon) {
        this.lifePoints-=weapon.getDamagePoints();
        if (!this.checkAlive()) {
            this.deth();
        }
    }

    private void deth() {

    }

    private Boolean checkAlive() {
        return this.lifePoints > 0;
    }

    public void setLifePoints(Integer lifePoints) {
        this.lifePoints = lifePoints;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public Integer getLifePoints() {
        return lifePoints;
    }

    public Boolean getAlive() {
        return alive;
    }

    public Double getAng() {
        return ang;
    }

    public void setAng(Double ang) {
        this.ang = ang;
    }

    public static Double getSpeed() {
        return speed;
    }
}
