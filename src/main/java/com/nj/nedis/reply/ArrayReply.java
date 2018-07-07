package com.nj.nedis.reply;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by admin on 2018/7/7.
 */
public class ArrayReply extends ComplexReply {

    private String[] arr;

    private int length = 0;


    public char getFirstByte() {
        return ReplyEnum.ARRAY.getFirstByte();
    }

    public ByteBuf response() {

        checkComplete();

        StringBuffer sb = new StringBuffer(length * 16);

        sb.append(getFirstByte());
        sb.append(length);
        sb.append(delimiter());

        for (int i = 0; i < length; i++) {
            String value = arr[i];
            // $length\r\n
            sb.append(ReplyEnum.BULK.getFirstByte());
            sb.append(value.length());
            sb.append(delimiter());

            //body\r\n
            sb.append(value);
            sb.append(delimiter());
        }

        return Unpooled.copiedBuffer(sb.toString().getBytes());
    }

    public boolean isComplete() {
        return true;
    }

}
