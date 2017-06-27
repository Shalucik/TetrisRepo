package com.youngcapital.tetris.complete.websocket;

public abstract class Greeting {
	
	private String content;
	private int status;

	public Greeting(String content, int status) {
		this.content = content;
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public int getStatus() {
		return status;
	}

}
