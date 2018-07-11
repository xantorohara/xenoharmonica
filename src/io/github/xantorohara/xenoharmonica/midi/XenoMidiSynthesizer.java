package io.github.xantorohara.xenoharmonica.midi;

import javax.sound.midi.*;
import java.io.InputStream;

public class XenoMidiSynthesizer {
    private Synthesizer synthesizer;
    private MidiChannel channel;
    private int volume = 50;
    private boolean activeNotes[] = new boolean[128];

    public XenoMidiSynthesizer() throws MidiUnavailableException {
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        channel = synthesizer.getChannels()[0];
        channel.programChange(21);
    }

    public void playNote(int note) {
        if (note >= 0 && note < 127 && !activeNotes[note]) {
            activeNotes[note] = true;
            channel.noteOn(note, volume);
        }
    }

    public void stopNote(int note) {
        if (note >= 0 && note < 127) {
            activeNotes[note] = false;
            channel.noteOff(note, 0);
        }
    }

    public void stopAllNotes() {
        channel.allNotesOff();
        for (int i = 0; i < activeNotes.length; i++)
            activeNotes[i] = false;
    }

    public boolean isNoteActive(int note) {
        return note < activeNotes.length && activeNotes[note];
    }

    public Instrument[] getInstruments() {
        if (synthesizer.getLoadedInstruments().length > 0) {
            return synthesizer.getLoadedInstruments();
        } else {
            return synthesizer.getAvailableInstruments();
        }
    }

    public void setSelecteInstrumentNumber(int instrument) {
        channel.programChange(instrument);
    }

    public int getSelecteInstrumentNumber() {
        return channel.getProgram();
    }

    public Instrument getSelectedInstrument() {
        Instrument[] instruments = getInstruments();
        for (int i = 0; i < instruments.length; i++)
            if (channel.getProgram() == i)
                return instruments[i];
        return null;
    }

    public void setSelecteInstrument(Instrument instrument) {
        Instrument[] instruments = getInstruments();
        for (int i = 0; i < instruments.length; i++)
            if (instruments[i].equals(instrument))
                channel.programChange(i);
    }

    public boolean loadSoundbank(InputStream soundBankStream) {
        try {
            Soundbank soundbank = MidiSystem.getSoundbank(soundBankStream);
            if (synthesizer.isSoundbankSupported(soundbank)) {
                System.out.println("Soundbank loaded");
                synthesizer.loadAllInstruments(soundbank);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume < 0 ? 0 : volume > 100 ? 100 : volume;
    }
}
