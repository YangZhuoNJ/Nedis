package com.nj.nedis.protocol;

import com.nj.nedis.exception.CmdException;
import com.nj.nedis.exception.ExceptionCode;

public class RedisProtocolReq {

	private String command;

	private String args[];

	public RedisProtocolReq( String[] req ) {

		this.command = req[ 0 ];

		if( req.length > 1 ) {
			args = new String[ req.length - 1 ];
			System.arraycopy( req, 1, args, 0, args.length );

		}
	}

	public String getCommand() {
		return command;
	}

	public String argIndex(int index) {

		if (index < 0 || index > args.length) {
			//// TODO: 2018/7/7
			throw new CmdException(ExceptionCode.START);
		}

		return this.args[index];
	}
}
