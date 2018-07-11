package io.github.xantorohara.xenoharmonica;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AboutPanel extends JEditorPane {
    private ImageIcon aboutimage;

    public AboutPanel() {
        setContentType("text/html");
        setEditable(false);
        setBorder(new EtchedBorder());

        aboutimage = new ImageIcon(getClass().getResource("resources/keyboard.gif"));
        setPreferredSize(new Dimension(aboutimage.getIconWidth(), aboutimage.getIconHeight()));
        update();
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    System.runFinalization();
                    System.gc();
                    update();
                }
            }
        });
    }

    private void update() {
        String sb = "<html><body>" +
                "<span style='color: #336699; font-size:large;'>XenoHarmonica</span>" +
                "<br/>" +
                "<a href='xantorohara@gmail.com'>xantorohara@gmail.com</a>" +
                "<br/>" +
                "\u00a9 2007-2014 Xantorohara " +
                "</html></body>";
        setText(sb);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        aboutimage.paintIcon(this, g, 0, 0);
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        paintBorder(g);
    }
}
