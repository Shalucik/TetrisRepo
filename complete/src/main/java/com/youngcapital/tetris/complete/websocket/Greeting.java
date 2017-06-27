package com.youngcapital.tetris.complete.websocket;

public abstract class Greeting {
	
	private String content;
	private boolean line;

	public Greeting(String content, boolean line) {
		this.content = content;
		this.line = line;
	}

	public String getContent() {
		return content;
	}

	public boolean isLine() {
		return line;
	}

}
