package com.nj.nedis.reply;

/**
 * Created by admin on 2018/7/7.
 */
public enum ReplyEnum {

    STATUS('+'),
    ERROR('-'),
    INTEGER(':'),
    BULK('$'),
    ARRAY('*'),

    ;

    ReplyEnum(char firstByte) {
        this.firstByte = firstByte;
    }

    private char firstByte;

    public char getFirstByte() {
        return firstByte;
    }
}
