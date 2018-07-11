package io.github.xantorohara.xenoharmonica;

import io.github.xantorohara.xenoharmonica.utils.ConfigUtils;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class Launcher extends JFrame {
    private static final String CONF_FILE = "xenoharmonica.xml";

    private static JWindow splashScreen = null;
    private Config config;

    public Launcher(Config config) {
        super("XenoHarmonica");
        this.config = config;
        setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("resources/icon.gif")));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(16, 16, 800, 240);
    }


    public void dispose() {
        try {
            ConfigUtils.save(config, CONF_FILE);
        } catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(Launcher.this, "Can't save config file", "XenoHarmonica", JOptionPane.WARNING_MESSAGE);
        }
        super.dispose();
        System.exit(0);
    }

    private static void showSplashScreen() {
        splashScreen = new JWindow();
        splashScreen.getContentPane().add(
                new JLabel(new ImageIcon(Launcher.class.getResource(
                        "resources/keyboard.gif"))));
        splashScreen.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Dimension frameSize = splashScreen.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        splashScreen.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        splashScreen.setVisible(true);
    }

    private static void hideSplashScreen() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.print(e.getMessage());
        } finally {

            splashScreen.setVisible(false);
            splashScreen.dispose();
            splashScreen = null;
        }
    }

    public static void main(String[] args) {
        showSplashScreen();
        Config config = null;
        try {
            config = (Config) ConfigUtils.load(CONF_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found.");
        }
        if (config == null)
            config = new Config();

        Launcher launcher = new Launcher(config);
        LauncherHelper launcherHelper = new LauncherHelper(config);
        launcherHelper.init(launcher);
        launcher.setVisible(true);
        hideSplashScreen();
    }
}
