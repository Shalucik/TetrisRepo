package com.youngcapital.tetris.complete.block;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Block {
	private Long id;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
	    return id;
	}

	
	private String blockType;
	private String color;
	private Orientation orientation0;
	private Orientation orientation1;
	private Orientation orientation2;
	private Orientation orientation3;
	private int currentOrientation;
	private Pos currentPos;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	public Orientation getOrientation0() {
		return orientation0;
	}

	public void setOrientation0(Orientation orientation0) {
		this.orientation0 = orientation0;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Orientation getOrientation1() {
		return orientation1;
	}

	public void setOrientation1(Orientation orientation1) {
		this.orientation1 = orientation1;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Orientation getOrientation2() {
		return orientation2;
	}

	public void setOrientation2(Orientation orientation2) {
		this.orientation2 = orientation2;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Orientation getOrientation3() {
		return orientation3;
	}

	public void setOrientation3(Orientation orientation3) {
		this.orientation3 = orientation3;
	}

	public void setId(Long id){
	    this.id = id;
	}


	@OneToOne(cascade = CascadeType.ALL)
	//@JoinColumn(name="currentpos_id")
	public Pos getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(Pos currentPos) {
		this.currentPos = currentPos;
	}

	public String getBlockType() {
		return blockType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	public int getCurrentOrientation() {
		return currentOrientation;
	}

	public void setCurrentOrientation(int currentOrientation) {
		this.currentOrientation = currentOrientation;
	}
}