package com.roozen.register.init.resource;

public interface Resource {

    // TODO: Custom Exceptions if we need to differentiate in the future.
    public String readBlock() throws Exception;

    public void close() throws Exception;

}
