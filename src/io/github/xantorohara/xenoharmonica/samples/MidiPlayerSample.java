package io.github.xantorohara.xenoharmonica.samples;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.FileInputStream;

public class MidiPlayerSample {

    public static void main(String[] args) {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            if (sequencer == null)
                throw new MidiUnavailableException();
            sequencer.open();
            FileInputStream is = new FileInputStream("sample.mid");
            Sequence mySeq = MidiSystem.getSequence(is);
            sequencer.setSequence(mySeq);
            sequencer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
