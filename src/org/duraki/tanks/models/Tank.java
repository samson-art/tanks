package org.duraki.tanks.models;

/**
 * Created by artemsamsonov on 14.12.14.
 */
public class Tank  extends Sprite{

    private Integer lifePoints = 100;

    public Tank(Double x, Integer y, Double ang) {
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

    public Integer getLifePoints() {
        return lifePoints;
    }


    public Double getAng() {
        return ang;
    }

    public void setAng(Double ang) {
        this.ang = ang;
    }


    public Weapon fire(Boolean e) {
        Weapon weapon = new Weapon(this.x+TANK_WIDHT/2+DULO_LENGHT*Math.cos(ang), ((Double)(this.y-Math.sin(ang)*DULO_LENGHT)).intValue(), this.ang, e);
        new Thread(weapon).start();
        return weapon;
    }
}
