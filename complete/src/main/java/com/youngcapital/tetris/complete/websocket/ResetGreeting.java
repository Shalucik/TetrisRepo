package com.youngcapital.tetris.complete.websocket;

public class ResetGreeting extends Greeting {

	public ResetGreeting(){
		this("no comment");
	}
	
	public ResetGreeting(String content){
		super(content, 2);
	}
}
