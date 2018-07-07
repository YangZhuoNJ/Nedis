package com.nj.nedis.protocol;

/**
 * Created by admin on 2018/7/7.
 */
public abstract class Protocol {

    public static final String DELIMITER = "\r\n";

    protected String delimiter() {
        return DELIMITER;
    }


}
