package com.youngcapital.tetris.complete.websocket;

import com.youngcapital.tetris.complete.block.Highscore;

public class HighscoreGreeting extends Greeting {
	
	Highscore[] scores;

	public HighscoreGreeting(String content) {
		super(content, 3);
	}

	public HighscoreGreeting(String content, Highscore[] scores) {
		this(content);
		this.scores = scores;
	}

	public Highscore[] getScores() {
		return scores;
	}
	
}
