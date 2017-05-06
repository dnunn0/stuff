package com.whatgameapps.firefly.com.whatgameapps.firefly.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    public static BufferedReader readerForFileNamed(String fileName) {
        InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(stream));
    }
}
