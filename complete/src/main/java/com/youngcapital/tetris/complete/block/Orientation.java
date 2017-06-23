package com.youngcapital.tetris.complete.block;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Orientation {
	private Long id;
	private Pos position0;
	private Pos position1;
	private Pos position2;
	private Pos position3;

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
	    return this.id;
	}

	public void setId(Long id){
	    this.id = id;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Pos getPosition0() {
		return position0;
	}

	public void setPosition0(Pos position0) {
		this.position0 = position0;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Pos getPosition1() {
		return position1;
	}

	public void setPosition1(Pos position1) {
		this.position1 = position1;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Pos getPosition2() {
		return position2;
	}

	public void setPosition2(Pos position2) {
		this.position2 = position2;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Pos getPosition3() {
		return position3;
	}

	public void setPosition3(Pos position3) {
		this.position3 = position3;
	}

	public Orientation(){}
	public Orientation(Pos one, Pos two, Pos three, Pos four) {
		position0 = one;
		position1 = two;
		position2 = three;
		position3= four;
	}
}
