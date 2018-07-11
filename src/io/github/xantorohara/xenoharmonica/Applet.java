package io.github.xantorohara.xenoharmonica;

import javax.swing.*;

public class Applet extends JApplet {

    public void init() {
        LauncherHelper launcherHelper = new LauncherHelper(new Config());
        launcherHelper.init(this);
    }
}
