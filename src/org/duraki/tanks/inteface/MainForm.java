package org.duraki.tanks.inteface;

import org.duraki.tanks.controllers.Controller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Created by artemsamsonov on 14.12.14.
 */
public class MainForm {

    final private Display display = new Display();
    final private Shell shell = new Shell(display);
    private Canvas canvas;
    private Controller controller;


    public MainForm(Controller controller) {
        this.controller = controller;
        shell.setSize(800, 600);
        shell.setLayout(new FillLayout());
        canvas = new Canvas(shell, SWT.NO_BACKGROUND);
        shell.addKeyListener(this.controller);
        canvas.addPaintListener(this.controller);
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                shell.open();
                while (!shell.isDisposed()) {
                    canvas.redraw();
                    if (!display.readAndDispatch()) display.sleep();
                }
            }
        });

        display.dispose();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Display getDisplay() {
        return display;
    }
}
