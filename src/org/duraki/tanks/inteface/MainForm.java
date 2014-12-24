package org.duraki.tanks.inteface;

import org.duraki.tanks.controllers.Controller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import static org.eclipse.swt.widgets.Display.getDefault;

/**
 * Created by artemsamsonov on 14.12.14.
 */
public class MainForm {

    final private Display display = new Display();
    final private Shell shell = new Shell(display);
    Canvas canvas = new Canvas(shell, SWT.NATIVE);

    public MainForm() {
        shell.setSize(800, 600);
        shell.setLayout(new FillLayout());
        shell.open();
    }

    public Shell getShell() {
        return shell;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Display getDisplay() {
        return display;
    }
}
