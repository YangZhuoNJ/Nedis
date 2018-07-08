package com.nj.nedis.reply.basic;

import com.nj.nedis.reply.ComplexReply;
import com.nj.nedis.reply.ReplyEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 * This type is just a CRLF terminated string representing an integer, prefixed by a ":" byte.
 * For example ":0\r\n", or ":1000\r\n" are integer replies.
 *
 * Many Redis commands return RESP Integers, like INCR, LLEN and LASTSAVE.
 * There is no special meaning for the returned integer, it is just an incremental number for INCR, a UNIX time for LASTSAVE and so forth.
 * However, the returned integer is guaranteed to be in the range of a signed 64 bit integer.
 * Integer replies are also extensively used in order to return true or false.
 * For instance commands like EXISTS or SISMEMBER will return 1 for true and 0 for false.
 * Other commands like SADD, SREM and SETNX will return 1 if the operation was actually performed, 0 otherwise.
 * The following commands will reply with an integer reply:
 *
 * SETNX, DEL, EXISTS, INCR, INCRBY, DECR, DECRBY, DBSIZE, LASTSAVE, RENAMENX, MOVE, LLEN, SADD, SREM, SISMEMBER, SCARD.
 *
 */

/**
 * 整数回复的编码方式很简单：以回车换行符结尾，以':'为起始的整数字符串，例如：":0\r\n", 或者 ":1000\r\n" 都是整数回复.
 *
 * 许多redis命令的回复都是整数回复，像 INCR, LLEN, 以及 LASTSAVE.
 * 整数回复的含义也有很多，回复INCR命令，表示增加后的数值，回复LASTSAVE，表示unix时间戳，等等. 但是，整数回复的数值，必须确保不超过64位，
 *
 * 整数回复也被扩展，用作回复真或者假，例如，回复EXISTS 或者SISMEMBER，1表示真，0表示假.
 * 其他许多命令，像SADD, SREM, 以及SETNX ，回复1表示操作有被执行，0表示没有.
 *
 * 以下这些命令都是回复整数回复：
 *
 * SETNX, DEL, EXISTS, INCR, INCRBY, DECR, DECRBY, DBSIZE, LASTSAVE, RENAMENX, MOVE, LLEN, SADD, SREM, SISMEMBER, SCARD.
 *
 */

public class IntegersReply extends ComplexReply {

    private int integer;

    public IntegersReply(int integer) {
        this.integer = integer;
    }

    public char getFirstByte() {
        return ReplyEnum.INTEGER.getFirstByte();
    }

    public boolean isComplete() {
        return true;
    }

    public String responseString() {
        StringBuffer sb = new StringBuffer();

        sb.append(getFirstByte());
        sb.append(integer);
        sb.append(delimiter());

        System.out.print(sb.toString());
        return sb.toString();

    }

    public ByteBuf response() {
        return Unpooled.copiedBuffer(responseString().getBytes());
    }
}
