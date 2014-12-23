package org.duraki.tanks.controllers;

import org.duraki.tanks.inteface.MainForm;
import org.duraki.tanks.models.Sprite;
import org.duraki.tanks.models.Tank;
import org.duraki.tanks.network.ClientTest;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by artemsamsonov on 22.12.14.
 */
public class Controller
        implements PaintListener, org.eclipse.swt.events.KeyListener {

    private MainForm form;
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private Scanner responce;
    private PrintWriter request;

    public Controller() {
        try {
            ClientTest client = new ClientTest();
            request = client.getOut();
            responce = client.getSc();
            beginGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void beginGame() {
        setTanks();
        form = new MainForm(this);

    }

    private void setTanks() {
        if ("1".equals(responce.nextLine())) {
            System.out.println("Your id: 1");
            sprites.add(new Tank(-100, 0, (double) 0));
            sprites.add(new Tank(100, 0, (double) 0));
        } else if ("2".equals(responce.nextLine())) {
            System.out.println("Your id: 2");
            sprites.add(new Tank(-100, 0, (double) 0));
            sprites.add(new Tank(100, 0, (double) 0));
        } else {
            System.err.println("Неверный ответ сервера");
        }
    }

    public static void main(String[] argv) {

        new Controller();

    }

    @Override
    public void paintControl(PaintEvent paintEvent) {
        for (Sprite i : sprites) {
            Image img = new Image(form.getDisplay(), "img/tank.png");
            paintEvent.gc.drawImage(img, i.getX(), i.getY());
        }
    }

    @Override
    public void keyPressed(org.eclipse.swt.events.KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(org.eclipse.swt.events.KeyEvent keyEvent) {

    }
}
