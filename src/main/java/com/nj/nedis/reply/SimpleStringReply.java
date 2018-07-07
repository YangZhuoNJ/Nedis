package com.nj.nedis.reply;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by admin on 2018/7/7.
 */
public class SimpleStringReply extends ComplexReply {


    private String body;

    public SimpleStringReply(String body) {
        this.body = body;
    }

    public char getFirstByte() {
        return ReplyEnum.STATUS.getFirstByte();
    }

    public ByteBuf response() {

        checkComplete();

        StringBuffer sb = new StringBuffer(body.length() + 3);
        sb.append(getFirstByte());
        sb.append(body);
        sb.append(delimiter());

        return Unpooled.copiedBuffer(sb.toString().trim().getBytes());
    }

    public boolean isComplete() {
        return true;
    }



}
