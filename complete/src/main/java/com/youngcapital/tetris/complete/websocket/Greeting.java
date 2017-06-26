package com.youngcapital.tetris.complete.websocket;

import java.awt.Point;

import com.youngcapital.tetris.complete.block.Pos;

public class Greeting {
	private String content;
	private Pos[] grayPositions;
	private Pos[] colorPositions;
	private String color;
	
	public Greeting() {}

	public Greeting(String content) {
		this.content = content;
	}

	public Greeting(Pos[] grayPositions, Pos[] colorPositions, String color) {
		this("no comment");
		this.grayPositions = grayPositions;
		this.colorPositions = colorPositions;
		this.color = color;
	}

	public Greeting(String content, Pos[] grayPositions, Pos[] colorPositions, String color) {
		this.content = content;
		this.grayPositions = grayPositions;
		this.colorPositions = colorPositions;
		this.color = color;
	}

	public String getContent() {
		return content;
	}

	public Pos[] getGrayPositions() {
		return grayPositions;
	}

	public Pos[] getColorPositions() {
		return colorPositions;
	}

	public String getColor() {
		return color;
	}

	
}
