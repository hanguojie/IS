package com.tospur.utils.response;

public class RespStatus {

	public static final long CODE_SUCCESSFUL = 0L;

	public static final String MSG_SUCCESSFUL = "ok";

	public static final long CODE_FAILED = 1L;

	public static final String MSG_FAILED = "failed";

	public static final RespStatus SUCCESSFUL = new RespStatus(String.valueOf(CODE_SUCCESSFUL), MSG_SUCCESSFUL);

	public static final RespStatus FAILED = new RespStatus(String.valueOf(CODE_FAILED), MSG_FAILED);

	private String code;

	private String msg;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public RespStatus(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
