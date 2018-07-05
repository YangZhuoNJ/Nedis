package com.nj.nedis.exception;

public class CmdException extends RuntimeException {

	protected int code;

	public CmdException( int code ) {
		this.code = code;
	}
}
