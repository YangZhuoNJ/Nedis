package com.nj.nedis.reply;

import com.nj.nedis.reply.basic.ArrayReply;

/**
 * The concept of Null Array exists as well, and is an alternative way to specify a Null value
 * (usually the Null Bulk String is used, but for historical reasons we have two formats).
 * For instance when the BLPOP command times out, it returns a Null Array that has a count of -1 as in the following example:
 *
 * "*-1\r\n"
 */
public class ArrayReplyInstance extends ArrayReply {

    private static final ArrayReply empty = new ArrayReplyInstance(0);

    private static final ArrayReply nil = new ArrayReplyInstance(-1);


    private ArrayReplyInstance(int length) {
        ArrayReply arrayReply = ArrayReply.empty(length);

    }

    public static ArrayReply emptyInstance() {
        return empty;
    }

    public static ArrayReply nilInstance() {
        return nil;
    }
}
