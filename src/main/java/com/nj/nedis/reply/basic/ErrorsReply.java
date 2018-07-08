package com.nj.nedis.reply.basic;

import com.nj.nedis.reply.ComplexReply;
import com.nj.nedis.reply.ReplyEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 * RESP has a specific data error for errors. Actually errors are exactly like RESP Simple Strings,
 * but the first character is a minus '-' character instead of a plus.
 * The real difference between Simple Strings and Errors in RESP is that errors are treated by clients as exceptions,
 * and the string that composes the Error error is the error message itself. The basic format is:
 * <p>
 * "-Error message\r\n"
 * <p>
 * Error replies are only sent when something wrong happens, for instance if you try to perform an operation against the wrong data error,
 * or if the command does not exist and so forth. An exception should be raised by the library client when an Error Reply is received.
 * The following are examples of error replies:
 * <p>
 * -ERR unknown command 'foobar'
 * -WRONGTYPE Operation against a key holding the wrong kind of value
 * <p>
 * The first word after the "-", up to the first space or newline, represents the kind of error returned.
 * This is just a convention used by Redis and is not part of the RESP Error format.For example,
 * ERR is the generic error,
 * while WRONGTYPE is a more specific error that implies that the client tried to perform an operation against the wrong data error.
 * This is called an Error Prefix and is a way to allow the client to understand the kind of error returned by the server
 * without to rely on the exact message given, that may change over the time.
 * <p>
 * A client implementation may return different kind of exceptions for different errors,
 * or may provide a generic way to trap errors by directly providing the error name to the caller as a string.
 * However, such a feature should not be considered vital as it is rarely useful,
 * and a limited client implementation may simply return a generic error condition, such as false.
 */

/**
 * Redis传输序列化协议有特定的错误类型回复————错误回复，实际上错误回复与Redis传输序列化协议中的简单字符串回复完全相同，
 * 唯一的区别在于：简单字符串回复中的第一个字母:加号'+'，在错误回复中被减号:'-'替代。
 * 错误回复与简单字符串回复之间的真正区别在于客户端（译者注：此处客户端指redis客户端库）将错误回复当成异常对待，
 * 构成错误回复的字符串就是异常的错误信息。 错误回复的基本格式如下：
 * <p>
 * "-Error message\r\n"
 * <p>
 * 错误回复只在发生错误时使用，例如，如果你试图操作错误的数据类型，或者你输入的命令并不存在，等等。
 * 这些时候，客户端库将会受到一个错误回复，并将回复里的错误信息解析出来，以此构造出一个异常，
 * 并将该异常抛出给调用者（译者注:此处调用者是指相对redis客户端库而言的用户程序，例如Dao层的用户代码）
 * 以下是错误回复的例子：
 * <p>
 * -ERR unknown command 'foobar'
 * -WRONGTYPE Operation against a key holding the wrong kind of value
 * <p>
 * 从第一个字符'-',一直到第一个空字符(' '， space char)或者换行符，代表错误回复的类型，这只是redis 使用的一种约定，不是错误格式的一部分。
 * 例如：'ERR' 代表宽泛的错误，而'WRONGTYPE'表示一种更具体的错误类型，具体的指出：客户端试图对错误的数据类型进行操作，这成为错误前缀，
 * 是一种允许客户端理解服务器返回的错误类型的方法，而不依赖给定的确切消息，但是这可能随时间变化。
 * <p>
 * 客户端实现可以针对不同的错误，解析成不同的异常并抛出，也可以直接将错误回复的字符串直接作为异常信息，提供给调用者。以便调用者根据该信息进行捕捉处理。
 * 但是这种功能不是至关重要的，它很少有用，有限的客户端实现，可能只返回一个宽泛的错误，例如只告知调用者，操作失败。
 */
public class ErrorsReply extends ComplexReply {


    private String error;

    private String message;

    public ErrorsReply(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public char getFirstByte() {
        return ReplyEnum.ERROR.getFirstByte();
    }

    public boolean isComplete() {
        return null != error;
    }

    public String responseString() {

        checkComplete();

        StringBuffer sb = new StringBuffer(32);
        sb.append(getFirstByte());
        sb.append(error);
        sb.append(' ');
        sb.append(message);
        sb.append(delimiter());
        System.out.print(sb.toString());
        return sb.toString();
    }

    // "-Error message\r\n"
    public ByteBuf response() {
        return Unpooled.copiedBuffer(responseString().getBytes());

    }
}
