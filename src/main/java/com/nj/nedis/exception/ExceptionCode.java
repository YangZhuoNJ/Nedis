package com.nj.nedis.exception;

public class ExceptionCode {

	public static final int START = 1000000000;

	//参数数量命令行长度校验错误
	public static final int ARGS_COUNT_LINE_LEN = START + 1;

	//参数数量行数字转换错误
	public static final int ARGS_COUNT_LINE_NFE = START + 2;

	//参数行数量过小
	public static final int ARGS_COUNT_LINE_SIZE = START + 3;

	//参数数量行起始符号错误  (非 '*' 符号)
	public static final int ARGS_COUNT_LINE_PREFFIX = START + 4;

	//参数行起始符号错误  (非 '$' 符号)
	public static final int ARGS_LINE_PREFFIX = START + 5;

	//参数行顺序错误
	public static final int CMD_ORDER_ERR = START + START + 1;

	//参数行多余错误
	public static final int CMD_ARGS_EXCESS = START + START + 2;
}
