package com.youngcapital.tetris.complete.block;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Highscore {
	
	private Long id;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
	    return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	private String name;
	private Long score;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getScore() {
		return score;
	}
	
	public void setScore(Long score) {
		this.score = score;
	}
	
}