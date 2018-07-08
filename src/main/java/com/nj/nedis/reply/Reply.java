package com.nj.nedis.reply;

import io.netty.buffer.ByteBuf;

/**
 * In RESP, the type of some data depends on the first byte:
 * For Simple Strings the first byte of the reply is "+"
 * For Errors the first byte of the reply is "-"
 * For Integers the first byte of the reply is ":"
 * For Bulk Strings the first byte of the reply is "$"
 * For Arrays the first byte of the reply is "*"
 */
public interface Reply {

    char getFirstByte();

    ByteBuf response();

    boolean isComplete();

    String responseString();

}
