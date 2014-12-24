package org.duraki.tanks.controllers;

import org.duraki.tanks.inteface.MainForm;
import org.duraki.tanks.models.Sprite;
import org.duraki.tanks.models.Tank;
import org.duraki.tanks.network.ClientTest;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by artemsamsonov on 22.12.14.
 */

public class Controller {

    private ClientTest client;
    private MainForm form = new MainForm();
    private Display display = form.getDisplay();
    private Canvas canvas = form.getCanvas();
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private BufferedReader responce;
    private PrintWriter request;
    private static Boolean formCreated = false;
    private static Boolean clientCreated = false;

    private Thread loopTh = new Thread() {
        @Override
        public void run() {
            display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    formCreated = true;
                    System.out.println("draw");
                    while (!display.isDisposed()) {
                        canvas.redraw();
                        if (display.readAndDispatch())
                            display.sleep();
                    }
                    display.dispose();
                }
            });
        }
    };


    public Controller() throws InterruptedException {
        System.out.println("Создание клиента...");
        try {
            client = new ClientTest();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("Клиент создан...");
        request = client.getOut();
        responce = client.getIn();
        System.out.println("Создание формы");
        form.getShell().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        form.getCanvas().addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                //TODO
                Image tankImg = new Image(form.getDisplay(), "img/tank.png");
                for (Sprite i : sprites) {
                    paintEvent.gc.drawImage(tankImg, 0, 0);
                }
            }
        });
        loopTh.start();
        System.out.println("Форма создана");
        System.out.println("Начинаю игру...");
        while (!formCreated) {
            Thread.sleep(100);
        }
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

//    private static void beginGame() throws IOException, InterruptedException {
//
//    }
    private void setTanks(String s) {
        if ("1".equals(s)) {
            System.out.println("Your id: 1");
            sprites.add(new Tank(-100, 0, (double) 0));
            sprites.add(new Tank(100, 0, (double) 0));
        } else if ("2".equals(s)) {
            System.out.println("Your id: 2");
            sprites.add(new Tank(-100, 0, (double) 0));
            sprites.add(new Tank(100, 0, (double) 0));
        }
        System.out.println("Сигнал получен...\nИгра начинается...");
    }

    public static void main(String[] argv) throws InterruptedException {
        new Controller();
    }

    public static void setFormCreated(Boolean formCreated) {
        formCreated = formCreated;
    }
}
