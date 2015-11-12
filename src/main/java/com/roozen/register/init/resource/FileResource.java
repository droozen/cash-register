package com.roozen.register.init.resource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileResource implements Resource {

    private FileReader reader;
    private BufferedReader bufferedReader;

    public FileResource(String fileSource) throws FileNotFoundException {
        reader = new FileReader(fileSource);
        bufferedReader = new BufferedReader(reader);
    }


    @Override
    public String readBlock() throws Exception {
        if (bufferedReader == null) return null;
        return bufferedReader.readLine();
    }

    @Override
    public void close() throws Exception {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }
}
