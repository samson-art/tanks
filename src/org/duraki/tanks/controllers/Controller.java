package org.duraki.tanks.controllers;

import org.duraki.tanks.models.Sprite;
import org.duraki.tanks.models.Tank;
import org.duraki.tanks.models.Weapon;
import org.duraki.tanks.network.ClientTest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by artemsamsonov on 22.12.14.
 */

public class Controller {

    private Display display = new Display();
    private Shell shell = new Shell(display);
    private Canvas canvas = new Canvas(shell, SWT.NATIVE);

    private final static Integer DIMENSION = 45;
    private final Image tankImg = new Image(display, "img/tank.png");
    private final Image weaponImg = new Image(display, "img/weapon.png");
    private final Image backgrondImg = new Image(display, "img/background.png");
    private final Image boomImg = new Image(display, "img/boom.png");

    private ClientTest client;
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private Sprite myTank;
    private BufferedReader responce;
    private PrintWriter request;
    private Double dt;
    private Boolean inGame = false;

    public Controller() throws InterruptedException {
        createClient();
        setWindow();
        setListeners();
        System.out.println("Форма создана");
        System.out.println("Жду сигнала к началу игры...");
        new Thread(new Runnable() {
            @Override
            public void run() {
            while (true) {
                try {
                    String ans = responce.readLine();
                    System.out.println("Server: " + ans);
                    if ("FIRE".equals(ans)) {
                        sprites.add(((Tank) sprites.get(1)).fire(true));
                    }
                    if ("MOVE".equals(ans)) {
                        String[] a = responce.readLine().split("#");
                        sprites.get(1).setX(Double.parseDouble(a[0]));
                        sprites.get(1).setAng(Double.parseDouble(a[1]));
                    }
                    if ("1".equals(ans) || "2".equals(ans)) {
                        setTanks(ans);
                    }
                    if ("LOOSE".equals(ans) || "WIN".equals(ans)) {
                        if ("WIN".equals(ans)) sprites.get(1).setLife(false);
                        JOptionPane.showMessageDialog(null, ans);
                        inGame = false;
                        finish();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            }
        }, "Client thread").start();
        while (!shell.isDisposed()) {
            Double t0 = (double) System.nanoTime();
            //System.out.println("Draw");
            if (inGame) canvas.redraw();
            checkCol();
            if (!display.readAndDispatch()) {
                // if there are currently no other OS event to process
                // sleep until the next OS event is available
                display.sleep();
            }
            Thread.sleep(10);
            dt = (System.nanoTime() - t0) / 1000000000;
        }
    }

    private void setListeners() {
        canvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                paintEvent.gc.drawImage(backgrondImg, 0, 0);
                if (sprites.size() > 0) {
                    paintEvent.gc.setLineWidth(5);
                    if (myTank.getLife()) {
                        paintEvent.gc.drawLine(((Double) (myTank.getX() + Tank.TANK_WIDHT / 2)).intValue(), myTank.getY(),
                                ((Double) (myTank.getX() + Tank.TANK_WIDHT / 2 + Sprite.DULO_LENGHT * Math.cos(myTank.getAng()))).intValue(),
                                ((Double) (myTank.getY() - Sprite.DULO_LENGHT * Math.sin(myTank.getAng()))).intValue());
                        paintEvent.gc.drawImage(tankImg, myTank.getX().intValue(), myTank.getY());
                    } else {
                        paintEvent.gc.drawImage(boomImg, myTank.getX().intValue(), myTank.getY()-DIMENSION);
                    }
                    if (sprites.get(1).getLife()) {
                        paintEvent.gc.drawLine(((Double) (sprites.get(1).getX() + Tank.TANK_WIDHT / 2)).intValue(), sprites.get(1).getY(),
                                ((Double) (sprites.get(1).getX() + Tank.TANK_WIDHT / 2 + Sprite.DULO_LENGHT * Math.cos(sprites.get(1).getAng()))).intValue(),
                                ((Double) (sprites.get(1).getY() - Sprite.DULO_LENGHT * Math.sin(sprites.get(1).getAng()))).intValue());
                        paintEvent.gc.drawImage(tankImg, sprites.get(1).getX().intValue(), sprites.get(1).getY());
                    } else {
                        paintEvent.gc.drawImage(boomImg, sprites.get(1).getX().intValue(), sprites.get(1).getY()-DIMENSION);
                    }
                }
                if (sprites.size() > 2) {
                    for (int i = 2; i < sprites.size(); i++) {
                        paintEvent.gc.drawImage(weaponImg, sprites.get(i).getX().intValue(), sprites.get(i).getY());
                        if (!sprites.get(i).getLife()) sprites.remove(i);
                    }
                }
            }
        });

        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == SWT.ARROW_LEFT) {
                    myTank.moveLeft(dt);
                    sendX();
                }
                if (keyEvent.keyCode == SWT.ARROW_RIGHT) {
                    myTank.moveRight(dt);
                    sendX();
                }
                if (keyEvent.keyCode == SWT.ARROW_UP) {
                    myTank.moveUp(dt);
                    sendX();
                }
                if (keyEvent.keyCode == SWT.ARROW_DOWN) {
                    myTank.moveDown(dt);
                    sendX();
                }
                if (keyEvent.keyCode == SWT.SPACE) {
                    sprites.add(((Tank)myTank).fire(false));
                    request.println("FIRE");
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
    }

    private void setWindow() {
        System.out.println("Создание формы");
        shell.setSize(Sprite.DISPLAY_WIDTH, Sprite.DISPLAY_HEIGHT);
        shell.setBackgroundImage(backgrondImg);
        shell.setLayout(new FillLayout());
        shell.open();
    }

    private void createClient() {
        System.out.println("Создание клиента...");
        try {
            client = new ClientTest();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        request = client.getOut();
        responce = client.getIn();
        System.out.println("Клиент создан...");
    }

    private void finish() {
        request.flush();
        request.close();
        try {
            responce.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.getSock().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkCol() {
        if (sprites.size() > 2) {
            Weapon w;
            for (int i = 2; i < sprites.size(); i++) {
                if (sprites.get(i) instanceof Weapon) {
                    w = (Weapon) sprites.get(i);
                    if (w.getEnemy()) {
                        if (w.getX() + Sprite.WEAPON_WIDTH > myTank.getX() && (myTank.getX() + Sprite.TANK_WIDHT) > w.getX()) {
                            if ((w.getY()+Sprite.WEAPON_HEIGHT) > myTank.getY()) {
                                w.setLife(false);
                                myTank.setLife(false);
                                request.println("END");
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendX() {
        request.println("MOVE");
        request.println(myTank.getX()+ "#" + myTank.getAng());
    }

    private void setTanks(String s) {
        if ("1".equals(s)) {
            System.out.println("Your id: 1");
            sprites.add(new Tank((double)100, Sprite.DISPLAY_HEIGHT-Sprite.TANK_HEIGHT-Sprite.BACKGROUND_HEIGHT, Math.toRadians(90)));
            sprites.add(new Tank((double)600, Sprite.DISPLAY_HEIGHT-Sprite.TANK_HEIGHT-Sprite.BACKGROUND_HEIGHT, Math.toRadians(90)));
        } else if ("2".equals(s)) {
            System.out.println("Your id: 2");
            sprites.add(new Tank((double)600, Sprite.DISPLAY_HEIGHT-Sprite.TANK_HEIGHT-Sprite.BACKGROUND_HEIGHT, Math.toRadians(90)));
            sprites.add(new Tank((double)100, Sprite.DISPLAY_HEIGHT-Sprite.TANK_HEIGHT-Sprite.BACKGROUND_HEIGHT, Math.toRadians(90)));
        }
        myTank = sprites.get(0);
        System.out.println("Сигнал получен...\nИгра начинается...");
        inGame = true;
    }

    public static void main(String[] args) throws InterruptedException {
        new Controller();
    }

}
