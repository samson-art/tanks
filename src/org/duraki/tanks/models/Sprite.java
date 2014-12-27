package org.duraki.tanks.models;

import org.duraki.tanks.controllers.Controller;

/**
 * Created by artemsamsonov on 22.12.14.
 */
public class Sprite {

    protected final static Double WEAPONSPEED = (double) 100;

    private final static Double TANK_SPEED = (double) 500;
    protected final static Double GRAVITY = (double) 200;
    public final static Integer TANK_WIDHT = 128;
    public final static Integer TANK_HEIGHT = 128;
    public final static Integer DISPLAY_WIDTH = 800;
    public final static Integer DISPLAY_HEIGHT = 600;
    public final static Integer DULO_SPEED = 100;

    protected Boolean life = true;
    protected Double x;
    protected Integer y;
    protected Double  ang;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void moveLeft(Double dt) {
        Double dx = TANK_SPEED*dt;
        setX(x-dx);
    }

    public void moveRight(Double dt) {
        Double dx = TANK_SPEED*dt;
        setX(x+dx);
    }

    public Boolean getLife() {
        return life;
    }

    public void setLife(Boolean life) {
        this.life = life;
    }

    public void moveUp(Double dt) {
        Double a = ang < Math.toRadians(90) ? Math.toRadians(dt*DULO_SPEED) : -Math.toRadians(dt*DULO_SPEED);
        ang+=a;
    }

    public void moveDown(Double dt) {
        Double a = ang < Math.toRadians(90) ? -Math.toRadians(dt*DULO_SPEED) : Math.toRadians(dt*DULO_SPEED);
        ang+=a;
    }

    public void setAng(Double ang) {
        this.ang = ang;
    }

    public Double getAng() {
        return ang;
    }
}
