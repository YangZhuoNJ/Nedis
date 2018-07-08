package com.nj.nedis.reply.basic;

import com.nj.nedis.reply.ComplexReply;
import com.nj.nedis.reply.ReplyEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Simple Strings are encoded in the following way:
 * a plus character,
 * followed by a string that cannot contain a CR or LF character (no newlines are allowed),
 * terminated by CRLF (that is "\r\n").
 *
 * Simple Strings are used to transmit non binary safe strings with minimal overhead.
 * For example many Redis commands reply with just "OK" on success, that as a RESP Simple String is encoded with the following 5 bytes:
 *
 * "+OK\r\n"
 *
 * In order to send binary-safe strings, RESP Bulk Strings are used instead.
 * When Redis replies with a Simple String,
 * a client library should return to the caller a string composed of the first character after the '+' up to the end of the string,
 * excluding the final CRLF bytes.
 */

/**
 * 简单字符串回复编码方式如下：
 * 一个加号:"+"
 * 接下来是一个不包含回车符（CR）与换行符（LF）的字符串，(该字符串有且仅有一行)，
 * 最后用回车换行符（CRLF）结束
 *
 * 简单字符串回复使用最小的的传输开销（minimal overhead）来传输非二进制安全的的字符串，
 * 例如，许多Redis命令的仅仅只想回复一个"OK"用来代表操作成功，那么使用简单字符串编码方式回复的话，可用以下五个字节进行回复：
 *
 * "+OK\r\n"
 *
 * 假若想要传输二进制安全的字符串回复，请使用块字符串回复（RESP Bulk String）.
 * 当Redis使用简单字符串回复时，客户端库应该向调用者（译者注:此处调用者是指相对redis客户端库而言的用户程序，例如Dao层的用户代码）返回简单字符串回复中，
 * "+"号之后一直到"CRLF"之前的字符串。
 */
public class SimpleStringReply extends ComplexReply {


    private String body;

    public SimpleStringReply(String body) {
        this.body = body;
    }

    public char getFirstByte() {
        return ReplyEnum.STATUS.getFirstByte();
    }

    public boolean isComplete() {
        return true;
    }

    public String responseString() {
        checkComplete();

        StringBuffer sb = new StringBuffer(body.length() + 3);
        sb.append(getFirstByte());
        sb.append(body);
        sb.append(delimiter());

        System.out.print(sb.toString());
        return sb.toString();
    }

    public ByteBuf response() {
        return Unpooled.copiedBuffer(responseString().getBytes());
    }



}
