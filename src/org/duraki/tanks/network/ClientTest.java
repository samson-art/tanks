package org.duraki.tanks.network;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author aleksejtitorenko
 */
public class ClientTest extends Thread{
    private Socket sock;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner sc;

    public ClientTest() throws IOException {

        System.out.println("Making client");
        try {
            sock = new Socket("localhost", 4444);
        }
        catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
            sc = new Scanner(in);
            start();
        }
        catch (IOException e) {
            try {
                sock.close();
            }
            catch (IOException e2) {
                System.err.println("Socket not closed");
            }
        }
      
    }
    @Override
    public void run() {
        while (true) {
            continue;
        }
    }

    public Scanner getSc() {
        return sc;
    }

    public PrintWriter getOut() {
        return out;
    }
}
