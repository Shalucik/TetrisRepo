package com.youngcapital.tetris.complete.block;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pos {
	private Long id;
	private int x;
	private int y;
	
	public Pos() {}
	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
	    return id;
	}

	public void setId(Long id){
	    this.id = id;
	}
	
	
}
