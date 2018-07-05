package com.nj.nedis.protocol;

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
}
