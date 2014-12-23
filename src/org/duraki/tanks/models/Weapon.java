package org.duraki.tanks.models;

/**
 * Created by artemsamsonov on 14.12.14.
 */
public abstract class Weapon extends Sprite{
    private final static Double speed = (double) 50;
    private Integer damagePoints;

    public Integer getDamagePoints() {
        return damagePoints;
    }

    public void setDamagePoints(Integer damagePoints) {
        this.damagePoints = damagePoints;
    }
}
