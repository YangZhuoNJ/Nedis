package com.nj.nedis;

import com.nj.nedis.protocol.RedisProtocolReq;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class RedisServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {

		RedisProtocolReq req = ( RedisProtocolReq )msg;
		String command = req.getCommand();

		if( "PING".equalsIgnoreCase( command ) ) {
			ctx.writeAndFlush( Unpooled.copiedBuffer( "+PONG\r\n".getBytes() ) );

		} else if( "config".equalsIgnoreCase( command ) ) {
			ctx.writeAndFlush( Unpooled.copiedBuffer( ":0\r\n".getBytes() ) );
		} else if( "select".equalsIgnoreCase( command ) ) {
			ctx.writeAndFlush( Unpooled.copiedBuffer( "+OK\r\n".getBytes() ) );
		} else if ("INFO".equalsIgnoreCase(command)) {
			ctx.writeAndFlush(Unpooled.copiedBuffer((
							"*1\r\n" +
							"$20\r\n" +
							"redis_version:2.9.11\r\n"
							).getBytes()));

		}


	}
}
