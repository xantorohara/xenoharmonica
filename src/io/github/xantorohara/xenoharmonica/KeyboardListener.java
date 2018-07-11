package io.github.xantorohara.xenoharmonica;

import io.github.xantorohara.xenoharmonica.midi.XenoMidiSynthesizer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardListener extends KeyAdapter {
    private XenoMidiSynthesizer synthesizer;
    private InstrumentEmulator emulator;

    public KeyboardListener(XenoMidiSynthesizer synthesizer, InstrumentEmulator emulator) {
        this.synthesizer = synthesizer;
        this.emulator = emulator;
    }

    public void keyPressed(KeyEvent e) {
        int note = emulator.getNoteForKey(e.getKeyCode());
        if (note != -1) {
            synthesizer.playNote(note);
            emulator.getUserInterface().repaint();
        }
    }

    public void keyReleased(KeyEvent e) {
        int note = emulator.getNoteForKey(e.getKeyCode());
        if (note != -1) {
            synthesizer.stopNote(note);
            emulator.getUserInterface().repaint();
        }
    }
}
