package com.nj.nedis.codec;

import com.nj.nedis.exception.CmdArgCountLineException;
import com.nj.nedis.exception.CmdException;
import com.nj.nedis.exception.CmdLineException;
import com.nj.nedis.exception.ExceptionCode;
import com.nj.nedis.protocol.RedisProtocolReq;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class CommandLineCodecHandler extends ChannelHandlerAdapter {

	public static final int MIN_ARGS = 1;

	public static final int MIN_ARG_LINE_LEN = 2;

	/**
	 * 参数个数
	 */
	private int argsCount = 0;

	/**
	 * 参数个数已被解析
	 */
	private boolean argsCountParsed = false;

	/**
	 * 参数数组
	 */
	private String[] args;

	private boolean initialized = false;

	/**
	 * 参数一倍解析
	 */
	private boolean argsParsed = false;

	/**
	 * 当前参数参数字符串长度
	 */
	private int tmpArgLen = -1;

	/**
	 * 当前参数长度已被解析
	 */
	private boolean tmpArgLengthParsed = false;

	private int argIndex = 0;

	@Override

	public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
		String in = ( String )msg;
		System.out.println( in );

		if( !argsCountParsed ) {
			parseArgCount( in );
		} else {
			parseArgs( in );
		}

		if( initialized && argIndex == args.length ) {
			printCommand();
			RedisProtocolReq req = new RedisProtocolReq( args );
			reset();

			ctx.fireChannelRead( req );
		}

	}

	private void printCommand() {
		System.out.print( "Command :  " );
		for( int i = 0; i < args.length; i++ ) {
			System.out.print( args[ i ] + " ");
		}
		System.out.println( "  " );
	}

	private void parseArgs( String in ) {
		initialize();

		if( !tmpArgLengthParsed ) {
			parseArgLengthLine( in );
			tmpArgLengthParsed = true;
		} else {
			storeArg( in );
			tmpArgLengthParsed = false;
		}

	}

	private void storeArg( String in ) {
		if( !tmpArgLengthParsed ) {
			throw new CmdLineException( ExceptionCode.CMD_ORDER_ERR );
		}

		if( in == null || in.length() != tmpArgLen ) {
			throw new CmdLineException( ExceptionCode.ARGS_COUNT_LINE_LEN );
		}

		args[ argIndex++ ] = in;
	}

	private void parseArgLengthLine( String in ) {
		if( in == null || in.length() < MIN_ARG_LINE_LEN ) {
			throw new CmdLineException( ExceptionCode.ARGS_COUNT_LINE_SIZE );
		}

		if( '$' != in.charAt( 0 ) ) {
			throw new CmdLineException( ExceptionCode.ARGS_LINE_PREFFIX );
		}
		int tmpArgLen = 0;
		try {
			tmpArgLen = Integer.valueOf( in.substring( 1, in.length() ) );
		} catch( NumberFormatException nfe ) {
			throw new CmdLineException( ExceptionCode.ARGS_COUNT_LINE_NFE );
		}

		this.tmpArgLen = tmpArgLen;

	}

	private void initialize() {

		//确保参数数量已经被解析
		if( !argsCountParsed ) {
			throw new CmdException( ExceptionCode.CMD_ORDER_ERR );
		}

		if( !initialized ) {
			this.args = new String[ argsCount ];
			initialized = true;
		}

	}

	private void parseArgCount( String in ) {
		checkArgCountLine( in );

		int argsCount = 0;

		try {
			argsCount = Integer.parseInt( in.substring( 1, in.length() ) );
		} catch( NumberFormatException nfe ) {
			throw new CmdLineException( ExceptionCode.ARGS_COUNT_LINE_NFE );
		}

		if( argsCount < MIN_ARGS ) {
			throw new CmdLineException( ExceptionCode.ARGS_COUNT_LINE_SIZE );
		}

		this.argsCount = argsCount;
		this.argsCountParsed = true;

	}

	private void checkArgCountLine( String in ) {
		if( in == null && in.length() < 2 ) {
			throw new CmdLineException( ExceptionCode.ARGS_COUNT_LINE_LEN );
		}

		//TODO magic char
		if( '*' != in.charAt( 0 ) ) {
			throw new CmdArgCountLineException( ExceptionCode.ARGS_COUNT_LINE_PREFFIX );
		}

	}

	@Override
	public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * 一条完整的命令请求已经解析完成，重置所有
	 */
	private void reset() {
		this.argsCount = 0;
		this.argsCountParsed = false;
		this.args = null;
		this.argsParsed = false;
		this.initialized = false;
		this.tmpArgLengthParsed = false;
		this.tmpArgLen = -1;
		this.argIndex = 0;
	}
}
