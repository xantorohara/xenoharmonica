package io.github.xantorohara.xenoharmonica;

public class Config {

    private int[] instruments = new int[]{21, 22, 23, 13, 52, 58, 0,74, 88, 123};
    private boolean showNotes = false;
    private boolean showLayout = true;
    private int octave = 5;
    private int instrument = 21;
    private int volume = 75;

    public int[] getInstruments() {
        return instruments;
    }

    public void setInstruments(int[] instruments) {
        this.instruments = instruments;
    }

    public boolean isShowNotes() {
        return showNotes;
    }

    public void setShowNotes(boolean showNotes) {
        this.showNotes = showNotes;
    }

    public boolean isShowLayout() {
        return showLayout;
    }

    public void setShowLayout(boolean showLayout) {
        this.showLayout = showLayout;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public int getInstrument() {
        return instrument;
    }

    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
