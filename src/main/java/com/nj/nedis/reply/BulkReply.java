package com.nj.nedis.reply;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by admin on 2018/7/7.
 */
public class BulkReply extends ComplexReply {

    private int length = -1;

    private String body;

    public BulkReply(String body) {
        if (null != body) {
            this.body = body;
            this.length = body.length();
        }
    }

    public ByteBuf response() {

        checkComplete();

        StringBuffer sb = new StringBuffer();

        // $length\r\n
        sb.append(getFirstByte());
        sb.append(length);
        sb.append(delimiter());

        if (notNil()) {
            // body\r\n
            sb.append(body);
            sb.append(delimiter());
        }

        return Unpooled.copiedBuffer(sb.toString().trim().getBytes());

    }

    public char getFirstByte() {
        return ReplyEnum.BULK.getFirstByte();
    }

    public boolean isComplete() {
        return false;
    }

    private boolean notNil() {
        return -1 != length;
    }

}
