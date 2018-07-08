package com.nj.nedis.reply.basic;

import com.nj.nedis.reply.ComplexReply;
import com.nj.nedis.reply.Reply;
import com.nj.nedis.reply.ReplyEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Clients send commands to the Redis server using RESP Arrays.
 * Similarly certain Redis commands returning collections of elements to the client use RESP Arrays are reply type.
 * An example is the LRANGE command that returns elements of a list.
 *
 * RESP Arrays are sent using the following format:
 *
 * A * character as the first byte, followed by the number of elements in the array as a decimal number, followed by CRLF.
 * An additional RESP type for every element of the Array.
 *
 * So an empty Array is just the following:
 *
 * "*0\r\n"
 *
 * While an array of two RESP Bulk Strings "foo" and "bar" is encoded as:
 *
 * "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n"
 *
 * As you can see after the *<count>CRLF part prefixing the array,
 * the other data types composing the array are just concatenated one after the other.
 * For example an Array of three integers is encoded as follows:
 *
 * "*3\r\n:1\r\n:2\r\n:3\r\n"
 *
 * Arrays can contain mixed types, it's not necessary for the elements to be of the same type. For instance,
 * a list of four integers and a bulk string can be encoded as the follows:
 *
 * *5\r\n
 * :1\r\n
 * :2\r\n
 * :3\r\n
 * :4\r\n
 * $6\r\n
 * foobar\r\n
 *
 * (The reply was split into multiple lines for clarity).
 * The first line the server sent is *5\r\n in order to specify that five replies will follow.
 * Then every reply constituting the items of the Multi Bulk reply are transmitted.
 * The concept of Null Array exists as well, and is an alternative way to specify a Null value
 * (usually the Null Bulk String is used, but for historical reasons we have two formats).
 * For instance when the BLPOP command times out, it returns a Null Array that has a count of -1 as in the following example:
 *
 * "*-1\r\n"
 *
 * A client library API should return a null object and not an empty Array when Redis replies with a Null Array.
 * This is necessary to distinguish between an empty list and a different condition (for instance the timeout condition of the BLPOP command).
 * Arrays of arrays are possible in RESP. For example an array of two arrays is encoded as follows:
 *
 * *2\r\n
 * *3\r\n
 * :1\r\n
 * :2\r\n
 * :3\r\n
 * *2\r\n
 * +Foo\r\n
 * -Bar\r\n
 *
 * (The format was split into multiple lines to make it easier to read).
 * The above RESP data type encodes a two elements Array consisting of an Array that contains three Integers 1, 2, 3 and
 * an array of a Simple String and an Error.
 * Null elements in Arrays
 * Single elements of an Array may be Null. This is used in Redis replies in order to signal that this elements are missing and not empty strings.
 * This can happen with the SORT command when used with the GET pattern option when the specified key is missing.
 * Example of an Array reply containing a Null element:
 *
 * *3\r\n
 * $3\r\n
 * foo\r\n
 * $-1\r\n
 * $3\r\n
 * bar\r\n
 *
 * The second element is a Null. The client library should return something like this:
 *
 * ["foo",nil,"bar"]
 *
 * Note that this is not an exception to what said in the previous sections, but just an example to further specify the protocol.
 *
 */

/**
 *
 * 客户端发送命令给redis服务器的编码方式是数组回复，类似的，某些命令，服务器的回复也是数组回复，例如LRANGE命令就是回复一个列表.
 *
 * 数组回复的编码格式如下:
 * 一个'*'号作为第一个字节，接下来是数组元素的个数（十进制）,然后是回车换行符.
 * 然后每个元素的按照元素自己的编码格式进行编码（译者注，可以将数组模式看成一种包装的方式.只在外层套一个包装）
 *
 * 空数组的编码格式如下:
 * "*0\r\n"
 *
 * 那么，带有两个块字符串 "foo" 和 "bar" 编码格式如下:
 *
 * "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n"
 *
 * 就如你所见到的，在数组回复前缀 *<count>CRLF 之后，按照各个元素自己的编码方式，一个接一个的追加。例如一个有三个整数回复的数组回复编码如下:
 * "*3\r\n:1\r\n:2\r\n:3\r\n"
 *
 * 数组回复可以包含多种回复类型，并不要求数组元素都是同一个类型，
 * 例如：一个包含四个整数回复，和一个块字符串回复的数组回复能够以以下方式编码:
 *
 * *5\r\n
 * :1\r\n
 * :2\r\n
 * :3\r\n
 * :4\r\n
 * $6\r\n
 * foobar\r\n
 *
 * （为了清楚的展示，我们将回复展开成多行）
 *
 * 服务器发送的第一行是 *5\r\n ,这是为了表明接下来有五个回复.然后构成数组回复的每一个元素会被传输
 * 空数组回复也被很好的支持。由于历史的原因，有两种方式来代表NUll，
 * 例如：当BLPOP命令超时时，返回一个元素个数为-1的空数组，编码方式如下：
 *
 * "*-1\r\n"
 *
 * 当客户端库的接口收到一个空回复(译者注：注意与"*0\r\n" 区分： -1 代表null， 0 代表 empty array )，应该返回一个NUll对象，而不是一个空数组，（译者注：返回给调用者）
 * 客户端必须如此解析，不然调用者无法感知是空数组还是还是NULL对象。
 *
 * 数组回复中的元素可以是数组回复。例如，包含两个数组回复的数组回复编码如下：
 *
 * *2\r\n
 * *3\r\n
 * :1\r\n
 * :2\r\n
 * :3\r\n
 * *2\r\n
 * +Foo\r\n
 * -Bar\r\n
 *
 */
public class ArrayReply extends ComplexReply {

    private Reply[] arr;

    protected int length = 0;

    public ArrayReply() {

    }

    public ArrayReply(Reply[] arr, int length) {
        this.arr = arr;
        this.length = length;
    }

    public ArrayReply(String[] bulkReplyStrArr) {
        length = bulkReplyStrArr.length;
        arr = new Reply[length];

        for (int i = 0; i < length; i++) {
            arr[i] = new BulkReply(bulkReplyStrArr[i]);
        }
    }

    protected static ArrayReply empty(int length) {
        ArrayReply empty = new ArrayReply();
        empty.length = length;
        return empty;
    }

    public char getFirstByte() {
        return ReplyEnum.ARRAY.getFirstByte();
    }

    public ByteBuf response() {
        return Unpooled.copiedBuffer(responseString().getBytes());
    }

    public boolean isComplete() {
        return true;
    }

    public String responseString() {
        checkComplete();

        StringBuffer sb = new StringBuffer(length * 16);

        sb.append(getFirstByte());
        sb.append(length);
        sb.append(delimiter());

        for (int i = 0; i < length; i++) {
            String value = arr[i].responseString();
            // $length\r\n
            sb.append(ReplyEnum.BULK.getFirstByte());
            sb.append(value.length());
            sb.append(delimiter());

            //body\r\n
            sb.append(value);
            sb.append(delimiter());
        }

        System.out.print(sb.toString());

        return sb.toString();
    }

}
