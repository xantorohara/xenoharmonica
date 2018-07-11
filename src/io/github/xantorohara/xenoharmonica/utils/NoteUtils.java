package io.github.xantorohara.xenoharmonica.utils;


public class NoteUtils {
    public static final String SHARP = "\u266f";
    public static final String FLAT = "\u266d";
    public static final String[] noteNames = new String[]{
            "Do", SHARP + FLAT, "Re", SHARP + FLAT, "Mi", "Fa", SHARP + FLAT, "So", SHARP + FLAT, "La", SHARP + FLAT, "Si"};

    /**
     * @param noteNumber [0..11]
     * @return [Do..Si]
     */
    public static String getNoteName(int noteNumber) {
        return noteNames[noteNumber % 12];
    }
}
