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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by artemsamsonov on 22.12.14.
 */

public class Controller {

    final static public Display display = new Display();
    final static private Shell shell = new Shell(display);
    final static public Canvas canvas = new Canvas(shell, SWT.NATIVE);

    private final Image tankImg = new Image(display, "img/tank.png");
    private final Image weaponImg = new Image(display, "img/weapon.png");
    private final Image backgrondImg = new Image(display, "img/background.png");
    private ClientTest client;
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private Sprite myTank;
    private BufferedReader responce;
    private PrintWriter request;
    private Double dt;
    private Boolean inGame = false;

    public Controller() throws InterruptedException {
        System.out.println("Создание клиента...");
        try {
            client = new ClientTest();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        request = client.getOut();
        responce = client.getIn();
        System.out.println("Клиент создан...");
        System.out.println("Создание формы");
        shell.setSize(Sprite.DISPLAY_WIDTH, Sprite.DISPLAY_HEIGHT);
        shell.setBackgroundImage(backgrondImg);
        shell.setLayout(new FillLayout());
        shell.open();
        canvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                paintEvent.gc.drawImage(backgrondImg, 0, 0);
                if (sprites.size() > 0) {
                    paintEvent.gc.setLineWidth(5);
                    paintEvent.gc.drawLine(((Double)(myTank.getX()+Tank.TANK_WIDHT/2)).intValue(), myTank.getY(),
                            ((Double)(myTank.getX()+Tank.TANK_WIDHT/2+Sprite.DULO_LENGHT*Math.cos(myTank.getAng()))).intValue(), 
                            ((Double)(myTank.getY()-Sprite.DULO_LENGHT*Math.sin(myTank.getAng()))).intValue());
                    paintEvent.gc.drawImage(tankImg, myTank.getX().intValue(), myTank.getY());
                    paintEvent.gc.drawLine(((Double) (sprites.get(1).getX()+Tank.TANK_WIDHT/2)).intValue(), sprites.get(1).getY(),
                            ((Double)(sprites.get(1).getX()+Tank.TANK_WIDHT/2+Sprite.DULO_LENGHT*Math.cos(sprites.get(1).getAng()))).intValue(),
                            ((Double)(sprites.get(1).getY()-Sprite.DULO_LENGHT*Math.sin(sprites.get(1).getAng()))).intValue());
                    paintEvent.gc.drawImage(tankImg, sprites.get(1).getX().intValue(), sprites.get(1).getY());
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


        System.out.println("Форма создана");
        System.out.println("Начинаю игру...");

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
                        System.out.println(ans);
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
            checkCol();
            if (inGame) canvas.redraw();
            if (!display.readAndDispatch()) {
                // if there are currently no other OS event to process
                // sleep until the next OS event is available
                display.sleep();
            }
            Thread.sleep(10);
            dt = (System.nanoTime() - t0) / 1000000000;
        }
    }

    private void finish() {
        try {
            client.getSock().close();
            request.flush();
            request.close();
            request.flush();
            responce.close();
            display.syncExec(new Runnable() {
                @Override
                public void run() {
                    shell.dispose();
                }
            });
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
                        if (myTank.getX() < w.getX() && (myTank.getX() + Sprite.TANK_WIDHT) > w.getX()) {
                            if (myTank.getY() < (w.getY()+Sprite.WALL_HEIGHT)) {
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
