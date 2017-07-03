package com.youngcapital.tetris.complete.websocket;

public class ScoreMessage extends Message {

	private String name;
	
	public ScoreMessage(){}

	public ScoreMessage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
