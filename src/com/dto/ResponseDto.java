package com.dto;

import java.io.Serializable;

public class ResponseDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean state;
	private String message;
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
