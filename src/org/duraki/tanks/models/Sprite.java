package org.duraki.tanks.models;

/**
 * Created by artemsamsonov on 22.12.14.
 */
public class Sprite {

    protected final static Double WEAPONSPEED = (double) 70;


    private final static Double TANK_SPEED = (double) 500;
    protected final static Double GRAVITY = (double) 200;
    public final static Integer BACKGROUND_HEIGHT = 15;
    public final static Integer WEAPON_WIDTH = 15;
    public final static Integer WEAPON_HEIGHT = 15;
    public final static Integer TANK_WIDHT = 120;
    public final static Integer TANK_HEIGHT = 74;
    public final static Integer WALL_WIDTH = 128;
    public final static Integer WALL_HEIGHT = 400;
    public final static Integer WALL_X = 336;
    public final static Integer WALL_Y = 385;
    public final static Integer DISPLAY_WIDTH = 800;
    public final static Integer DISPLAY_HEIGHT = 800;
    public final static Integer DULO_LENGHT = 25;
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
        if ((x-dx)<0) x = (double) 0;
        else if ((x-dx)<(WALL_X+WALL_WIDTH) && (x-dx) > WALL_X) x = (double) WALL_X+WALL_WIDTH;
        else x-=dx;
    }

    public void moveRight(Double dt) {
        Double dx = TANK_SPEED*dt;
        if ((x+dx+TANK_WIDHT)>DISPLAY_WIDTH) x = (double) (DISPLAY_WIDTH - TANK_WIDHT);
        else if ((x+dx+TANK_WIDHT)>WALL_X && (x+dx+TANK_WIDHT) < (WALL_X+WALL_WIDTH)) x = (double) (WALL_X - TANK_WIDHT);
        else x+=dx;
    }

    public Boolean getLife() {
        return life;
    }

    public void setLife(Boolean life) {
        this.life = life;
    }

    public void moveUp(Double dt) {
        Double a = Math.toRadians(dt*DULO_SPEED);
        if ((ang+a)>Math.toRadians(180)) {
            ang = Math.toRadians(180);
        }
        else ang+=a;
    }

    public void moveDown(Double dt) {
        Double a = Math.toRadians(dt*DULO_SPEED);
        if ((ang-a)<Math.toRadians(0)) {
            ang = Math.toRadians(0);
        }
        else ang-=a;
    }

    public void setAng(Double ang) {
        this.ang = ang;
    }

    public Double getAng() {
        return ang;
    }
}
