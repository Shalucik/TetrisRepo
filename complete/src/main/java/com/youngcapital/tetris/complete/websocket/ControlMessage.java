package com.youngcapital.tetris.complete.websocket;

public class ControlMessage {
	
	private int keyboardCode;
	
	public ControlMessage() {
		super();
	}

	public ControlMessage(int keyboardCode) {
		super();
		this.keyboardCode = keyboardCode;
	}

	public int getKeyboardCode() {
		return keyboardCode;
	}

	public void setKeyboardCode(int keyboardCode) {
		this.keyboardCode = keyboardCode;
	}
}
