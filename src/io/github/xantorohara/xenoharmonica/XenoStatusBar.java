package io.github.xantorohara.xenoharmonica;

import io.github.xantorohara.xenoharmonica.midi.XenoMidiSynthesizer;

import javax.sound.midi.Instrument;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XenoStatusBar extends JPanel implements ActionListener {
    private JLabel instrumentLabel = new JLabel("none");
    private JLabel octaveLabel = new JLabel("?");
    private JSlider volumeLabel = new JSlider(0, 100);

    private XenoMidiSynthesizer synthesizer;
    private InstrumentEmulator emulator;
    private Config config;

    public XenoStatusBar(XenoMidiSynthesizer synthesizer, InstrumentEmulator emulator,
                         KeyboardListener keyboardListener, Config config) {

        this.synthesizer = synthesizer;
        this.emulator = emulator;
        this.config = config;

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);
        add(new PanelElement("Instrument:", instrumentLabel, 200));
        add(Box.createRigidArea(new Dimension(4, 0)));
        add(new PanelElement("Octave:", octaveLabel, 25));
        add(Box.createRigidArea(new Dimension(4, 0)));
        add(new PanelElement(null, new JPanel(), 100));
        add(Box.createRigidArea(new Dimension(4, 0)));
        add(new PanelElement("Volume:", volumeLabel, 100));

        volumeLabel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                XenoStatusBar.this.synthesizer.setVolume(volumeLabel.getValue());
                XenoStatusBar.this.config.setVolume(XenoStatusBar.this.synthesizer.getVolume());
            }
        });
        volumeLabel.addKeyListener(keyboardListener);
        volumeLabel.setMinimumSize(new Dimension(100, 0));
    }

    private class PanelElement extends JPanel {
        public PanelElement(String label, JComponent component, int width) {
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            component.setPreferredSize(new Dimension(width, 0));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.LOWERED,
                            getBackground().brighter(),
                            getBackground(),
                            getBackground(),
                            getBackground().darker().darker()),
                    BorderFactory
                            .createEmptyBorder(2, 2, 2, 2)));
            if (label != null) {
                add(new JLabel(label));
                add(Box.createRigidArea(new Dimension(4, 0)));
            }
            add(component);
        }
    }


    public void actionPerformed(ActionEvent e) {
        Instrument instrument = synthesizer.getSelectedInstrument();
        instrumentLabel.setText(instrument == null ? "none" : instrument.getName());
        volumeLabel.setValue(synthesizer.getVolume());
        octaveLabel.setText(String.valueOf(emulator.getOctave()));
    }
}
