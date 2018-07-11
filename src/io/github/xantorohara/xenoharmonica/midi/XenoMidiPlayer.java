package io.github.xantorohara.xenoharmonica.midi;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;

public class XenoMidiPlayer {
    private Sequencer sequencer;

    public XenoMidiPlayer() throws MidiUnavailableException {
        sequencer = MidiSystem.getSequencer();
        if (sequencer == null)
            throw new MidiUnavailableException();
        sequencer.open();
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
    }

    public void play(InputStream is) throws IOException, InvalidMidiDataException {
        Sequence mySeq = MidiSystem.getSequence(is);
        sequencer.setSequence(mySeq);
        sequencer.start();
    }

    public void stop() {
        sequencer.stop();
    }
}
