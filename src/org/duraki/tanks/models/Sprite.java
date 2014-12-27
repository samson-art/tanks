package org.duraki.tanks.models;

import org.duraki.tanks.controllers.Controller;

/**
 * Created by artemsamsonov on 22.12.14.
 */
public class Sprite {

    protected final static Double WEAPONSPEED = (double) 50;

    private final static Double TANK_SPEED = (double) 500;
    protected final static Double GRAVITY = (double) 20;
    public final static Integer TANK_WIDHT = 128;
    public final static Integer TANK_HEIGHT = 128;
    public final static Integer DISPLAY_WIDTH = 800;
    public final static Integer DISPLAY_HEIGHT = 600;

    protected Boolean life = true;
    protected Double x;
    protected Integer y;

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
}
