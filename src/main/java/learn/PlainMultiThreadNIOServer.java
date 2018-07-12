package learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * one io thread + multi thread worker module
 */
public class PlainMultiThreadNIOServer {

	private final int port;

	private static final ExecutorService ex =
			new ThreadPoolExecutor(
					Runtime.getRuntime().availableProcessors() / 2 + 1,
					Runtime.getRuntime().availableProcessors(),
					5, TimeUnit.MINUTES,
					new LinkedBlockingDeque<Runnable>() //	todo OOM
			);

	public static void main( String[] args ) throws Exception {
		new PlainMultiThreadNIOServer( 9090 ).server();

	}

	private void server() throws Exception {
		// open a server socket channel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		// bind server socket channel to port
		serverSocketChannel.bind( new InetSocketAddress( port ) );

		// config channel is un-blocking
		serverSocketChannel.configureBlocking( false );

		// open a new multiplexing selector
		Selector selector = Selector.open();

		// regist the server socket channel to the selector
		serverSocketChannel.register( selector, SelectionKey.OP_ACCEPT );

		while( true ) {
			int readChannelCount = selector.select();
			if( readChannelCount == 0 ) {

				//jdk epoll bug select() should be blocking util there is one or more than one channel is ready
				//this bug can cause cpu 100% overload ,best way is to rebuild a new select,
				System.err.println( "JDK epoll bug" );
			}

			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> i = selectedKeys.iterator();

			while( i.hasNext() ) {
				SelectionKey key = i.next();
				i.remove();

				if( key.isValid() ) {

					// a new socket connected to server socket channel
					if( key.isAcceptable() ) {
						SocketChannel socketChannel = serverSocketChannel.accept();
						socketChannel.configureBlocking( false );
						socketChannel.register( selector, SelectionKey.OP_READ );

						// socket channel ready to read
					} else if( key.isReadable() ) {
						SocketChannel socketChannel = ( SocketChannel )key.channel();
						ex.submit( new ChannelHandler( socketChannel ) );
					}
				} else {
					key.cancel();
				}
			}
		}
	}



	public PlainMultiThreadNIOServer( int port ) {
		this.port = port;
	}

	static class ChannelHandler implements Runnable {

		@Override
		public void run() {
			ByteBuffer buffer = ByteBuffer.allocate( 1024 );
			try {
				socketChannel.read( buffer );
				buffer.flip();
				socketChannel.write( buffer );
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}

		private final SocketChannel socketChannel;

		public ChannelHandler( SocketChannel socketChannel ) {
			this.socketChannel = socketChannel;
		}
	}
}
