package com.roozen.register.init.resource;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class ResourceFactory {

    public Resource getFileResource(String fileSource) throws FileNotFoundException {
        return new FileResource(fileSource);
    }
}
