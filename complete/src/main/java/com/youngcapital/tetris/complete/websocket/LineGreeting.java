package com.youngcapital.tetris.complete.websocket;

public class LineGreeting extends Greeting {
	
	private int[] lines;
	private long score;

	public LineGreeting(String content) {
		super(content, 1);
	}

	public LineGreeting(int[] lines, long score) {
		this("no comment");
		this.lines = lines;
		this.score = score;
	}
	
	public LineGreeting(String content, int[] lines, long score) {
		this(content);
		this.lines = lines;
		this.score = score;
	}

	public int[] getLines() {
		return lines;
	}

	public long getScore() {
		return score;
	}
}
