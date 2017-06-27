package com.youngcapital.tetris.complete.websocket;

public class LineGreeting extends Greeting {
	
	private int[] lines;

	public LineGreeting(String content) {
		super(content, 1);
	}

	public LineGreeting(int[] lines) {
		this("no comment");
		this.lines = lines;
	}
	
	public LineGreeting(String content, int[] lines) {
		this(content);
		this.lines = lines;
	}

	public int[] getLines() {
		return lines;
	}
}
