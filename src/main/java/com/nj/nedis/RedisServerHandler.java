package com.nj.nedis;

import com.nj.nedis.config.Info;
import com.nj.nedis.protocol.RedisProtocolReq;
import com.nj.nedis.reply.Reply;
import com.nj.nedis.reply.SimpleStringReplyInstance;
import com.nj.nedis.reply.basic.ArrayReply;
import com.nj.nedis.reply.basic.BulkReply;
import com.nj.nedis.reply.basic.IntegersReply;
import com.nj.nedis.store.Store;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Collection;

public class RedisServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RedisProtocolReq req = (RedisProtocolReq) msg;
        String command = req.getCommand();

        if ("PING".equalsIgnoreCase(command)) {
            ctx.writeAndFlush(SimpleStringReplyInstance.getPong().response());

        } else if ("config".equalsIgnoreCase(command)) {
            ctx.writeAndFlush(new IntegersReply(0).response());

        } else if ("select".equalsIgnoreCase(command)) {

            if (Integer.valueOf(req.argIndex(0)) > 2) {
                ctx.writeAndFlush(SimpleStringReplyInstance.getOk().response());
            }

            //// TODO: 2018/7/8
            ctx.writeAndFlush(SimpleStringReplyInstance.getOk().response());

        } else if ("INFO".equalsIgnoreCase(command)) {

            String info = Info.getInfo(new Info());
            System.out.print(info);

//            ctx.writeAndFlush(Unpooled.copiedBuffer((
//                    "*1\r\n" +
//                            "$20\r\n" +
//                            "redis_version:2.9.11\r\n"
//            ).getBytes()));

            String[] arr = new String[]{"redis_version:2.9.11"};
            ctx.writeAndFlush(new ArrayReply(arr).response());

        } else if ("KEYS".equalsIgnoreCase(command)) {

            Collection<String> keys = Store.keys();

//            StringBuffer sb = new StringBuffer(keys.size() * 16);
//            sb.append("*" + keys.size() + "\r\n");
//            for (String key : keys) {
//                sb.append("$" + key.length() + "\r\n" + key + "\r'n");
//            }

            String[] keyArray = new String[keys.size()];
            new ArrayList<String>(keys).toArray(keyArray);
            ctx.writeAndFlush(Unpooled.copiedBuffer(new ArrayReply(keyArray).response()));

        } else if ("SET".equalsIgnoreCase(command)) {
            Store.set(req.argIndex(0), req.argIndex(1));

            ctx.writeAndFlush(SimpleStringReplyInstance.getOk().response());


        } else if ("GET".equalsIgnoreCase(command)) {
            String value = Store.get(req.argIndex(0));
            Reply reply = new BulkReply(value);
            ctx.writeAndFlush(reply.response());
        }


    }
}
