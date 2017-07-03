package com.youngcapital.tetris.complete.websocket;

public class LineGreeting extends Greeting {
	
	private int[] lines;
	private long score;
	private int level;

	public LineGreeting(String content) {
		super(content, 1);
		this.level = 0;
	}

	public LineGreeting(int[] lines, long score) {
		this("no comment");
		this.lines = lines;
		this.score = score;
	}
	
	public int getLevel() {
		return level;
	}


	public LineGreeting(String content, int[] lines, long score, int level) {
		this(content);
		this.lines = lines;
		this.score = score;
		this.level = level;
	}

	public int[] getLines() {
		return lines;
	}

	public long getScore() {
		return score;
	}
}
