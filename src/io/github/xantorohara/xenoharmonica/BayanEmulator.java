package io.github.xantorohara.xenoharmonica;

import io.github.xantorohara.xenoharmonica.midi.XenoMidiSynthesizer;
import io.github.xantorohara.xenoharmonica.utils.NoteUtils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Set;

public class BayanEmulator extends JPanel implements InstrumentEmulator {
    private static final int BAYAN_FIRST_NOTE = 46;
    private static final int[] KEYS = new int[]{VK_Q, VK_A, VK_Z, VK_W, VK_S, VK_X, VK_E, VK_D, VK_C, VK_R, VK_F, VK_V, VK_T, VK_G, VK_B, VK_Y, VK_H, VK_N, VK_U, VK_J, VK_M, VK_I, VK_K, VK_COMMA, VK_O, VK_L, VK_PERIOD, VK_P, VK_SEMICOLON, VK_SLASH, VK_OPEN_BRACKET, VK_QUOTE, VK_UNDEFINED, VK_CLOSE_BRACKET};
    private static final char[] CHARS = new char[]{'q', 'a', 'z', 'w', 's', 'x', 'e', 'd', 'c', 'r', 'f', 'v', 't', 'g', 'b', 'y', 'h', 'n', 'u', 'j', 'm', 'i', 'k', ',', 'o', 'l', '.', 'p', ';', '/', '[', '\'', ' ', ']'};

    private static final Color COLOR_LIGHT = new Color(0x99, 0xCC, 0xFF);
    private static final Color COLOR_MIDDLE = new Color(0x33, 0x66, 0x99);
    private static final Color COLOR_DARK = new Color(0x00, 0x33, 0x66);

    private int octave = 5;
    private boolean showNotes = false;
    private boolean showLayout = true;
    private XenoMidiSynthesizer synthesizer;

    private Dimension size;
    private Ellipse2D.Float[] buttons = new Ellipse2D.Float[55];
    private GradientPaint gradientPaint;

    public BayanEmulator(XenoMidiSynthesizer synthesizer) {
        super();
        this.synthesizer = synthesizer;
        for (int i = 0; i < buttons.length; i++)
            buttons[i] = new Ellipse2D.Float();

        addMouseListener(new NoteMouseListener());
        setBorder(new EtchedBorder());
        setFocusable(true);
    }

    public boolean isShowNotes() {
        return showNotes;
    }

    public void setShowNotes(boolean showNotes) {
        this.showNotes = showNotes;
        if (this.isVisible()) repaint();
    }

    public boolean isShowLayout() {
        return showLayout;
    }

    public void setShowLayout(boolean showLayout) {
        this.showLayout = showLayout;
        if (this.isVisible()) repaint();
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave < 0 ? 0 : octave > 10 ? 10 : octave;
        if (this.isVisible()) repaint();
    }

    private void update() {
        double buttonSize;
        if (getWidth() > getHeight()) {
            buttonSize = getWidth() / 20.0;
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setFrame((i + 1) / 3 * buttonSize + (i + 1) % 3 * buttonSize / 2,
                        (i + 1) % 3 * buttonSize + buttonSize / 2,
                        buttonSize * 0.8, buttonSize * 0.8);
            }
            gradientPaint = new GradientPaint(getWidth() / 2, getHeight(), COLOR_LIGHT, getWidth() / 2, 0, COLOR_MIDDLE, true);
        } else {
            buttonSize = getHeight() / 20.0;
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setFrame(buttonSize * 2 - (i + 1) % 3 * buttonSize + buttonSize / 2,
                        (i + 1) / 3 * buttonSize + (i + 1) % 3 * buttonSize / 2,
                        buttonSize * 0.8, buttonSize * 0.8);
            }
            gradientPaint = new GradientPaint(0, getHeight() / 2, COLOR_LIGHT, getWidth(), getHeight() / 2, COLOR_MIDDLE, true);
        }
        int fontSize = (int) (buttonSize * 0.4);
        if (fontSize != getFont().getSize())
            setFont(new Font("Arial Unicode MS", Font.PLAIN, fontSize));
    }

    private void paintGradient(Graphics2D g2) {
        g2.setPaint(gradientPaint);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void shiftEllipse2D(Ellipse2D ellipse, int shift) {
        ellipse.setFrame(ellipse.getX() + shift, ellipse.getY() + shift, ellipse.getWidth(), ellipse.getHeight());
    }

    private void paintButtons(Graphics2D g2) {
        for (int i = 0; i < buttons.length; i++) {
            g2.setColor(COLOR_DARK);
            if (!synthesizer.isNoteActive(i + BAYAN_FIRST_NOTE)) {
                shiftEllipse2D(buttons[i], 2);
                g2.fill(buttons[i]);
                shiftEllipse2D(buttons[i], -2);
            }
            Color color = getColorForNote(i + 10, false);
            g2.setColor(color);
            g2.fill(buttons[i]);
            color = getColorForNote(i + 10, true);
            if (synthesizer.isNoteActive(i + BAYAN_FIRST_NOTE))
                color = Color.RED;
            g2.setColor(color);
            g2.draw(buttons[i]);
        }
    }

    private void paintNotes(Graphics2D g2) {
        double buttonSize = buttons[0].getWidth();
        int h = g2.getFontMetrics().getHeight();
        for (int i = 0; i < buttons.length; i++) {
            Color color = getColorForNote(i + 10, true);
            g2.setColor(color);
            String note = NoteUtils.getNoteName(i + 10);
            int w = g2.getFontMetrics().stringWidth(note);
            if (showLayout)
                g2.drawString(note,
                        (int) (buttons[i].getX() + buttonSize * 0.333 - w / 2),
                        (int) (buttons[i].getY() + buttonSize * 0.333 + h / 3));
            else
                g2.drawString(note,
                        (int) (buttons[i].getX() + buttonSize / 2 - w / 2),
                        (int) (buttons[i].getY() + buttonSize / 2 + h / 3));
        }
    }

    private void paintLayout(Graphics2D g2) {
        double buttonSize = buttons[0].getWidth();
        int h = g2.getFontMetrics().getHeight();
        for (int i = 0; i < CHARS.length; i++) {
            int shift = i + octave * 12 - BAYAN_FIRST_NOTE;

            if (shift < 0 || shift >= buttons.length) continue;

            Color color = getColorForNote(i + 12, true);
            g2.setColor(color);
            char note = getCharForBaseNote(i);

            int w = g2.getFontMetrics().charWidth(note);
            if (showNotes)
                g2.drawString(Character.toString(note),
                        (int) (buttons[shift].getX() + buttonSize * 0.666 - w / 2),
                        (int) (buttons[shift].getY() + buttonSize * 0.666 + h / 3));
            else
                g2.drawString(Character.toString(note),
                        (int) (buttons[shift].getX() + buttonSize / 2 - w / 2),
                        (int) (buttons[shift].getY() + buttonSize / 2 + h / 3));
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();
        if (!size.equals(this.size)) {
            this.size = size;
            update();
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        paintGradient(g2);
        paintButtons(g2);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (showNotes) paintNotes(g2);
        if (showLayout) paintLayout(g2);
    }

    private static Color getColorForNote(int note, boolean inverse) {
        switch (note % 12) {
            case 1:
            case 3:
            case 6:
            case 8:
            case 10:
                return inverse ? Color.WHITE : Color.BLACK;
            default:
                return inverse ? Color.BLACK : Color.WHITE;
        }
    }

    public static int getKeyForNote(int note) {
        return (note >= 0 && note < KEYS.length) ? KEYS[note] : 0;
    }

    public char getCharForBaseNote(int note) {
        return (note >= 0 && note < CHARS.length) ? CHARS[note] : 0;
    }

    public int getNoteForKey(int key) {
        for (int i = 0; key > 0 && i < KEYS.length; i++) {
            if (key == KEYS[i])
                return octave * 12 + i;
        }
        return -1;
    }

    public Component getUserInterface() {
        return this;
    }

    private class NoteMouseListener extends MouseAdapter {
        private Set<Integer> notes = new HashSet<Integer>();

        public void mousePressed(MouseEvent e) {
            if (!BayanEmulator.this.hasFocus())
                BayanEmulator.this.requestFocus();
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].contains(e.getX(), e.getY())) {
                    int note = i + BAYAN_FIRST_NOTE;
                    if (notes.contains(note)) {
                        notes.remove(note);
                        synthesizer.stopNote(note);
                    } else {
                        notes.add(note);
                        synthesizer.playNote(note);
                    }
                }
            }
            if (!notes.isEmpty())
                repaint();
        }

        public void mouseReleased(MouseEvent e) {
            if (!notes.isEmpty() && !e.isControlDown()) {
                for (Integer note : notes) {
                    synthesizer.stopNote(note);
                }
                notes.clear();
                repaint();
            }
        }
    }
}