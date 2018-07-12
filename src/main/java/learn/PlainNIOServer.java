package learn;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlainNIOServer {

	private final int port;

	public static void main( String[] args ) throws Exception {
		new PlainNIOServer( 9090 ).server();

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
						ByteBuffer buffer = ByteBuffer.allocate( 1024 );
						SocketChannel socketChannel = ( SocketChannel )key.channel();
						socketChannel.read( buffer );
						buffer.flip();
						socketChannel.write( buffer );
					}
				} else {
					key.cancel();
				}
			}
		}
	}

	public PlainNIOServer( int port ) {
		this.port = port;
	}
}
