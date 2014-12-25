package org.duraki.tanks.controllers;

import org.duraki.tanks.models.Sprite;
import org.duraki.tanks.models.Tank;
import org.duraki.tanks.network.ClientTest;
import org.eclipse.swt.SWT;
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

    final static private Display display = new Display();
    final private Shell shell = new Shell(display);
    Canvas canvas = new Canvas(shell, SWT.NATIVE);

    private static Image tankImg = new Image(display, "img/tank.png");

    private ClientTest client;
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private Sprite myTank;
    private BufferedReader responce;
    private PrintWriter request;

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
        shell.setSize(1000, 1000);
        shell.setLayout(new FillLayout());
        shell.open();
        canvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                for (Sprite i : sprites) {
                    paintEvent.gc.drawImage(tankImg, i.getX(), i.getY());
                }
            }
        });
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == SWT.ARROW_LEFT) {
                    myTank.setX((int) (myTank.getX()-Tank.getSpeed()));
                } else if (keyEvent.keyCode == SWT.ARROW_RIGHT) {
                    myTank.setX((int) (myTank.getX()+Tank.getSpeed()));
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
                        if ("END".equals(responce)) {
                            break;
                        }
                        if ("1".equals(ans) || "2".equals(ans)) {
                            setTanks(ans);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Client thread").start();
        while (!shell.isDisposed()) {
            System.out.println("Draw");
            canvas.redraw();
            // read the next OS event queue and transfer it to a SWT event
            if (!display.readAndDispatch()) {
                // if there are currently no other OS event to process
                // sleep until the next OS event is available
                display.sleep();
            }
        }
    }

    private void setTanks(String s) {
        if ("1".equals(s)) {
            System.out.println("Your id: 1");
            sprites.add(new Tank(-100, 100, (double) 0));
            sprites.add(new Tank(100, 100, (double) 0));
        } else if ("2".equals(s)) {
            System.out.println("Your id: 2");
            sprites.add(new Tank(100, 100, (double) 0));
            sprites.add(new Tank(-100, 100, (double) 0));
        }
        myTank = sprites.get(0);
        System.out.println("Сигнал получен...\nИгра начинается...");
    }

    public static void main(String[] argv) throws InterruptedException {
        new Controller();
    }

    public static void setFormCreated(Boolean formCreated) {
        formCreated = formCreated;
    }
}
