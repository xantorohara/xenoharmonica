package io.github.xantorohara.xenoharmonica;

import java.awt.*;

public interface InstrumentEmulator {
    int getNoteForKey(int key);

    Component getUserInterface();

    int getOctave();

    void setOctave(int octave);

    void setShowLayout(boolean show);

    void setShowNotes(boolean show);
}
