package org.duraki.tanks.models;

/**
 * Created by artemsamsonov on 22.12.14.
 */
public class Sprite {

    protected final static Double Gravity = (double) 10;
    protected final static Integer TANK_WIDHT = 128;
    protected final static Integer TANK_HEIGHT = 128;
    protected final static Integer DISPLAY_WIDTH = 800;
    protected final static Integer DISPLAY_HEIGHT = 600;

    private Integer x;
    private Integer y;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x+DISPLAY_WIDTH/2;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y-DISPLAY_HEIGHT;
    }
}
