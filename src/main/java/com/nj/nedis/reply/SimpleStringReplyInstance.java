package com.nj.nedis.reply;

import com.nj.nedis.reply.basic.SimpleStringReply;

public class SimpleStringReplyInstance extends SimpleStringReply {

    private static final Reply ok = new SimpleStringReplyInstance("OK");

    private static final Reply pong = new SimpleStringReplyInstance("PONG");



    private SimpleStringReplyInstance(String string) {
        super(string);
    }

    public static Reply getOk() {
        return ok;
    }

    public static Reply getPong() {
        return pong;
    }
}