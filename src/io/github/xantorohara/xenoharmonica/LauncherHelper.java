package io.github.xantorohara.xenoharmonica;

import io.github.xantorohara.xenoharmonica.midi.XenoMidiSynthesizer;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class LauncherHelper {
    private XenoMidiSynthesizer synthesizer;
    private InstrumentEmulator emulator;
    private Config config;

    public LauncherHelper(Config config) {
        this.config = config;
    }

    public void init(Container container) {
        try {
            synthesizer = new XenoMidiSynthesizer();

            if (synthesizer.getInstruments().length == 0) {
                InputStream is = this.getClass().getResourceAsStream("resources/soundbank.gm");
                synthesizer.loadSoundbank(is);
            }

            if (synthesizer.getInstruments().length == 0)
                JOptionPane.showMessageDialog(null, "Instruments not found", "XenoHarmonica", JOptionPane.ERROR_MESSAGE);

            synthesizer.setSelecteInstrumentNumber(config.getInstrument());
            synthesizer.setVolume(config.getVolume());

            emulator = new BayanEmulator(synthesizer);
            emulator.setOctave(config.getOctave());
            emulator.setShowLayout(config.isShowLayout());
            emulator.setShowNotes(config.isShowNotes());
        } catch (MidiUnavailableException e) {
            JOptionPane.showMessageDialog(null, "Midi device not found", "XenoHarmonica", JOptionPane.ERROR_MESSAGE);
        }

        KeyboardListener keyboardListener = new KeyboardListener(synthesizer, emulator);
        XenoStatusBar statusBar = new XenoStatusBar(synthesizer, emulator, keyboardListener, config);
        emulator.getUserInterface().addKeyListener(keyboardListener);

        container.setLayout(new BorderLayout());
        container.add(emulator.getUserInterface(), BorderLayout.CENTER);
        container.add(statusBar, BorderLayout.SOUTH);
        statusBar.actionPerformed(null);

        XenoMenu xenoMenu = new XenoMenu(container, emulator, synthesizer, keyboardListener, config);
        xenoMenu.addXenoListener(statusBar);
        if (container instanceof JFrame)
            ((JFrame) container).setJMenuBar(xenoMenu);
        else if (container instanceof JApplet)
            ((JApplet) container).setJMenuBar(xenoMenu);
    }
}
