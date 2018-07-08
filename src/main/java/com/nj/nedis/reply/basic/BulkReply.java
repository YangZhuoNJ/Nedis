package com.nj.nedis.reply.basic;

import com.nj.nedis.reply.ComplexReply;
import com.nj.nedis.reply.ReplyEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Bulk Strings are used in order to represent a single binary safe string up to 512 MB in length.
 * Bulk Strings are encoded in the following way:
 *
 * A "$" byte followed by the number of bytes composing the string (a prefixed length), terminated by CRLF.
 * The actual string data.
 * A final CRLF.
 *
 * So the string "foobar" is encoded as follows:
 *
 * "$6\r\nfoobar\r\n"
 *
 * When an empty string is just:
 *
 * "$0\r\n\r\n"
 *
 * RESP Bulk Strings can also be used in order to signal non-existence of a value using a special format that is used to represent a Null value.
 * In this special format the length is -1, and there is no data, so a Null is represented as:
 *
 * "$-1\r\n"
 *
 * This is called a Null Bulk String.
 *
 * The client library API should not return an empty string, but a nil object, when the server replies with a Null Bulk String.
 * For example a Ruby library should return 'nil' while a C library should return NULL (or set a special flag in the reply object), and so forth.
 *
 *
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

    public char getFirstByte() {
        return ReplyEnum.BULK.getFirstByte();
    }

    public boolean isComplete() {
        return -1 != length;
    }

    private boolean notNil() {
        return -1 != length;
    }

    public String responseString() {
        checkComplete();

        StringBuffer sb = new StringBuffer();

        // $length\r\n
        sb.append(getFirstByte());
        sb.append(length);
        sb.append(delimiter());

        if (isComplete()) {
            // body\r\n
            sb.append(body);
            sb.append(delimiter());
        }

        System.out.print(sb.toString());
        return sb.toString();
    }

    public ByteBuf response() {
        return Unpooled.copiedBuffer(responseString().getBytes());

    }



}
