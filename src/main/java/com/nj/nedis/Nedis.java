package com.nj.nedis;

import com.nj.nedis.codec.CommandLineCodecHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

public class Nedis {

	public static void main( String[] args ) {
		int port = 6378;
		new Nedis().serve(port);
	}

	private void serve( int port ) {

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();

			bootstrap.group( boss, worker )
					.channel( NioServerSocketChannel.class )
					.option( ChannelOption.TCP_NODELAY, true )
					.childHandler( new ChannelInitializer<SocketChannel>() {

						protected void initChannel( SocketChannel ch ) throws Exception {

							ByteBuf in = Unpooled.copiedBuffer( "\r\n".getBytes() );
							ch.pipeline().addLast( new DelimiterBasedFrameDecoder( 1024, in ) );
							ch.pipeline().addLast( new StringDecoder( CharsetUtil.UTF_8 ) );
							ch.pipeline().addLast( new CommandLineCodecHandler() );
							ch.pipeline().addLast( new RedisServerHandler() );

						}
					} );

			ChannelFuture future = bootstrap.bind( port ).sync();
			future.channel().closeFuture().sync();
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

}
