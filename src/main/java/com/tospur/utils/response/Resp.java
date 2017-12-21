package com.tospur.utils.response;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class Resp {
	private RespStatus status;

	private Object data;
	
	public Resp(){
		
	}
	
	public Resp(RespStatus status,Object data){
		this.status = status;
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public Resp setData(Object data) {
		this.data = data;
		return this;
	}

	public RespStatus getStatus() {
		return status;
	}

	public Resp setStatus(RespStatus status) {
		this.status = status;
		return this;
	}

	public static Resp success() {
		return new Resp().setStatus(RespStatus.SUCCESSFUL);
	}
	
	public static Resp failed() {
		return new Resp().setStatus(RespStatus.FAILED);
	}

	public static Resp failed(String msg) {
		final String c = String.valueOf(RespStatus.CODE_FAILED);
		final RespStatus s = new RespStatus(c, msg);
		return new Resp().setStatus(s);
	}
	
	public static Resp failed(BindingResult bindingResult){
		final FieldError fieldError = bindingResult.getFieldError();
		final String code = RespStatus.FAILED.getCode();
		final String msg = fieldError.getDefaultMessage();
		final RespStatus s = new RespStatus(code, msg);
		return new Resp().setStatus(s);
	}
	
	public static Resp failed(String code,String msg){
		final RespStatus s = new RespStatus(code, msg);
		return new Resp().setStatus(s);
	}
	
}
