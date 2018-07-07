package com.nj.nedis.reply;

import io.netty.buffer.ByteBuf;

/**
 * Created by admin on 2018/7/7.
 */
public interface Reply {

    char getFirstByte();

    ByteBuf response();

    boolean isComplete();



}
