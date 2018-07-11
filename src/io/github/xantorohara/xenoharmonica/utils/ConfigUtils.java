package io.github.xantorohara.xenoharmonica.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class ConfigUtils {
    public static Object load(String fileName) throws FileNotFoundException {
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(fileName)));
        Object config = decoder.readObject();
        decoder.close();
        return config;
    }

    public static void save(Object config, String fileName) throws FileNotFoundException {
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
        encoder.writeObject(config);
        encoder.close();
    }

}
