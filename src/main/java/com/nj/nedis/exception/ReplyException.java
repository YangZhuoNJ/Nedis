package com.nj.nedis.exception;

/**
 * Created by admin on 2018/7/7.
 */
public class ReplyException extends RuntimeException {

    private int code;

    public ReplyException(int code) {
        this.code = code;
    }
}
