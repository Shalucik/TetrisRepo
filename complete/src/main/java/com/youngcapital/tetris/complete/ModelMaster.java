package com.youngcapital.tetris.complete;

import java.awt.Point;
import java.util.List;

import com.youngcapital.tetris.complete.block.Block;
import com.youngcapital.tetris.complete.block.BlockRepository;
import com.youngcapital.tetris.complete.block.Highscore;
import com.youngcapital.tetris.complete.block.HighscoreRepository;
import com.youngcapital.tetris.complete.block.Orientation;
import com.youngcapital.tetris.complete.block.Pos;
import com.youngcapital.tetris.complete.block.TetrisBlock;

public class ModelMaster {
	
	private BlockRepository bRepo;
	
	public ModelMaster(BlockRepository bRepo){
		this.bRepo = bRepo;
	}

	public TetrisBlock createBlock(){
		Block block = getRandomBlock();
		
		Point curPos = getPoint(block.getCurrentPos());
		
		Point[][] oris = new Point[4][];
		oris[0] = getOrientation(block.getOrientation0());
		oris[1] = getOrientation(block.getOrientation1());
		oris[2] = getOrientation(block.getOrientation2());
		oris[3] = getOrientation(block.getOrientation3());
		
		int randomOrientation = (int)(Math.random() * 4); 
		
		return new TetrisBlock(curPos, oris, randomOrientation, TetrisMaster.addPointToArray(curPos, oris[randomOrientation]), block.getColor());
	}
	
	public Point[] getOrientation(Orientation ori){
		Point[] oris = new Point[4];
		oris[0] = getPoint(ori.getPosition0());
		oris[1] = getPoint(ori.getPosition1());
		oris[2] = getPoint(ori.getPosition2());
		oris[3] = getPoint(ori.getPosition3());
		return oris;
	}
	
	public Point getPoint(Pos position){
		return new Point(position.getX(), position.getY());
	}

	private Block getRandomBlock() {
		List<Block> list = null;
		int blockNum = (int) (Math.random() * 7);
		switch (blockNum) {
		case 6:
			list = bRepo.findByBlockType("Line");
			break;
		case 5:
			list = bRepo.findByBlockType("Square");
			break;
		case 4:
			list = bRepo.findByBlockType("S");
			break;
		case 3:
			list = bRepo.findByBlockType("Z");
			break;
		case 2:
			list = bRepo.findByBlockType("T");
			break;
		case 1:
			list = bRepo.findByBlockType("J");
			break;
		case 0:
			list = bRepo.findByBlockType("L");
			break;
		}
		
		return list == null ? null : list.get(0);
	}
	
	public boolean addScore(HighscoreRepository hRepo, String name, Long score){
		Highscore highScore = new Highscore();
		highScore.setName(name);
		highScore.setScore(score);
		hRepo.save(highScore);
		return true;
	}
	
	public String makeDB() {
		bRepo.deleteAll();

		Block block = new Block();

		// line
		block.setOrientation0(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(2, 0), new Pos(3, 0)));
		block.setOrientation1(new Orientation(new Pos(1, 0), new Pos(1, 1), new Pos(1, 2), new Pos(1, 3)));
		block.setOrientation2(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(2, 0), new Pos(3, 0)));
		block.setOrientation3(new Orientation(new Pos(1, 0), new Pos(1, 1), new Pos(1, 2), new Pos(1, 3)));
		block.setColor("rgb(100, 149, 237)");
		block.setCurrentPos(new Pos(3, 0));
		block.setBlockType("Line");

		bRepo.save(block);

		// Square
		Block square = new Block();
		square.setOrientation0(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)));
		square.setOrientation1(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)));
		square.setOrientation2(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)));
		square.setOrientation3(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)));
		square.setColor("rgb(0, 0, 255)");
		square.setCurrentPos(new Pos(4, 0));
		square.setBlockType("Square");

		bRepo.save(square);

		// S
		Block s = new Block();
		s.setOrientation0(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 1)));
		s.setOrientation1(new Orientation(new Pos(-1, 1), new Pos(-1, 0), new Pos(0, 1), new Pos(0, 2)));
		s.setOrientation2(new Orientation(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 1)));
		s.setOrientation3(new Orientation(new Pos(-1, 1), new Pos(-1, 0), new Pos(0, 1), new Pos(0, 2)));
		s.setColor("rgb(0, 255, 0)");
		s.setCurrentPos(new Pos(5, 0));
		s.setBlockType("S");

		bRepo.save(s);

		// Z
		Block z = new Block();
		z.setOrientation0(new Orientation(new Pos(0, 0), new Pos(-1, 0), new Pos(0, 1), new Pos(1, 1)));
		z.setOrientation1(new Orientation(new Pos(0, 0), new Pos(0, 1), new Pos(-1, 1), new Pos(-1, 2)));
		z.setOrientation2(new Orientation(new Pos(0, 0), new Pos(-1, 0), new Pos(0, 1), new Pos(1, 1)));
		z.setOrientation3(new Orientation(new Pos(0, 0), new Pos(0, 1), new Pos(-1, 1), new Pos(-1, 2)));
		z.setColor("rgb(255, 0, 0)");
		z.setCurrentPos(new Pos(4, 0));
		z.setBlockType("Z");

		bRepo.save(z);

		// T
		Block t = new Block();
		t.setOrientation0(new Orientation(new Pos(0, -1), new Pos(-1, 0), new Pos(0, 0), new Pos(1, 0)));
		t.setOrientation1(new Orientation(new Pos(0, -1), new Pos(0, 0), new Pos(1, 0), new Pos(0, 1)));
		t.setOrientation2(new Orientation(new Pos(-1, -1), new Pos(0, -1), new Pos(1, -1), new Pos(0, 0)));
		t.setOrientation3(new Orientation(new Pos(0, -1), new Pos(0, 0), new Pos(-1, 0), new Pos(0, 1)));
		t.setColor("rgb(255, 165, 0)");
		t.setCurrentPos(new Pos(4, 1));
		t.setBlockType("T");

		bRepo.save(t);

		// J
		Block j = new Block();
		j.setOrientation0(new Orientation(new Pos(0, 0), new Pos(0, -1), new Pos(0, 1), new Pos(-1, 1)));
		j.setOrientation1(new Orientation(new Pos(1, 0), new Pos(0, 0), new Pos(-1, 0), new Pos(-1, -1)));
		j.setOrientation2(new Orientation(new Pos(-1, 0), new Pos(-1, -1), new Pos(0, -1), new Pos(-1, 1)));
		j.setOrientation3(new Orientation(new Pos(-1, -1), new Pos(0, -1), new Pos(1, -1), new Pos(1, 0)));
		j.setColor("rgb(255, 215, 0)");
		j.setCurrentPos(new Pos(5, 1));
		j.setBlockType("J");

		bRepo.save(j);

		// L
		Block l = new Block();
		l.setOrientation0(new Orientation(new Pos(0, 0), new Pos(0, -1), new Pos(0, 1), new Pos(1, 1)));
		l.setOrientation1(new Orientation(new Pos(1, -1), new Pos(0, -1), new Pos(-1, -1), new Pos(-1, 0)));
		l.setOrientation2(new Orientation(new Pos(-1, -1), new Pos(0, -1), new Pos(0, 0), new Pos(0, 1)));
		l.setOrientation3(new Orientation(new Pos(-1, 0), new Pos(0, 0), new Pos(1, 0), new Pos(1, -1)));
		l.setColor("rgb(148, 0, 211)");
		l.setCurrentPos(new Pos(4, 1));
		l.setBlockType("L");

		bRepo.save(l);

		return "Added database entries";
	}
}
