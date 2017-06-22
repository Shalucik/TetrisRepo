package com.youngcapital.tetris.complete;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youngcapital.tetris.complete.blocks.Block;
import com.youngcapital.tetris.complete.blocks.BlockRepository;

@Controller
public class TetrisController {
	@Autowired
	private BlockRepository bRepo;
	
	@RequestMapping("/tetris")
	public String createPage(Model model){
		model.addAttribute("gridWidth", 10);
		model.addAttribute("gridHeight", 20);
		model.addAttribute("test", bRepo.findOne(1l));
		return "tetris";
	}
	
	@RequestMapping("block")
	public @ResponseBody String storeBlocks() {
		Block line = new Block();
		line.setColor("rgb(100, 149, 237)");
		line.setOrientations("{\"orientations\":[[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":2,\"y\":0},{\"x\":3,\"y\":0}],[{\"x\":1,\"y\":0},{\"x\":1,\"y\":1},{\"x\":1,\"y\":2},{\"x\":1,\"y\":3}]]}");
		line.setCurrentOrientation(0);
		line.setBlockType("line");
		line.setCurrentX(3);
		line.setCurrentY(0);
		
		bRepo.save(line);
		
		Block square = new Block();
		square.setColor("rgb(0, 0, 255)");
		square.setOrientations("{\"orientations\":[[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":0,\"y\":1},{\"x\":1,\"y\":1}]]}");
		square.setCurrentOrientation(0);
		square.setBlockType("square");
		square.setCurrentX(4);
		square.setCurrentY(0);
		
		bRepo.save(square);
		
		Block s = new Block();
		s.setColor("rgb(0, 255, 0)");
		s.setOrientations("{\"orientations\":[[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":0,\"y\":1},{\"x\":-1,\"y\":1}],[{\"x\":-1,\"y\":0},{\"x\":-1,\"y\":-1},{\"x\":0,\"y\":0},{\"x\":0,\"y\":1}]]}");
		s.setCurrentOrientation(0);
		s.setBlockType("S");
		s.setCurrentX(5);
		s.setCurrentY(0);
		
		bRepo.save(s);
		
		Block z = new Block();
		z.setColor("rgb(255, 0, 0)");
		z.setOrientations("{\"orientations\":[[{\"x\":-1,\"y\":0},{\"x\":0,\"y\":0},{\"x\":0,\"y\":1},{\"x\":1,\"y\":1}],[{\"x\":1,\"y\":0},{\"x\":1,\"y\":1},{\"x\":0,\"y\":1},{\"x\":0,\"y\":2}]]}");
		z.setCurrentOrientation(0);
		z.setBlockType("Z");
		z.setCurrentX(4);
		z.setCurrentY(0);
		
		bRepo.save(z);
		
		Block t = new Block();
		t.setColor("rgb(255, 165, 0)");
		t.setOrientations("{\"orientations\":[[{\"x\":0,\"y\":-1},{\"x\":-1,\"y\":0},{\"x\":0,\"y\":0},{\"x\":1,\"y\":0}],[{\"x\":0,\"y\":-1},{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":0,\"y\":1}],[{\"x\":-1,\"y\":-1},{\"x\":0,\"y\":-1},{\"x\":1,\"y\":-1},{\"x\":0,\"y\":0}],[{\"x\":0,\"y\":-1},{\"x\":0,\"y\":0},{\"x\":-1,\"y\":0},{\"x\":0,\"y\":1}]]}");
		t.setCurrentOrientation(0);
		t.setBlockType("T");
		t.setCurrentX(4);
		t.setCurrentY(1);
		
		bRepo.save(t);
		
		Block j = new Block();
		j.setColor("rgb(255, 215, 0)");
		j.setOrientations("{\"orientations\":[[{\"x\":0,\"y\":0},{\"x\":0,\"y\":-1},{\"x\":0,\"y\":1},{\"x\":-1,\"y\":1}],[{\"x\":1,\"y\":0},{\"x\":0,\"y\":0},{\"x\":-1,\"y\":0},{\"x\":-1,\"y\":-1}],[{\"x\":-1,\"y\":0},{\"x\":-1,\"y\":-1},{\"x\":0,\"y\":-1},{\"x\":-1,\"y\":1}],[{\"x\":-1,\"y\":-1},{\"x\":0,\"y\":-1},{\"x\":1,\"y\":-1},{\"x\":1,\"y\":0}]]}");
		j.setCurrentOrientation(0);
		j.setBlockType("J");
		j.setCurrentX(5);
		j.setCurrentY(1);
		
		bRepo.save(j);
		
		Block l = new Block();
		l.setColor("rgb(148, 0, 211)");
		l.setOrientations("{\"orientations\":[[{\"x\":0,\"y\":0},{\"x\":0,\"y\":-1},{\"x\":0,\"y\":1},{\"x\":1,\"y\":1}],[{\"x\":1,\"y\":-1},{\"x\":0,\"y\":-1},{\"x\":-1,\"y\":-1},{\"x\":-1,\"y\":0}],[{\"x\":-1,\"y\":-1},{\"x\":0,\"y\":-1},{\"x\":0,\"y\":0},{\"x\":0,\"y\":1}],[{\"x\":-1,\"y\":0},{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":1,\"y\":-1}]]}");
		l.setCurrentOrientation(0);
		l.setBlockType("L");
		l.setCurrentX(4);
		l.setCurrentY(1);
		
		bRepo.save(l);
		
		return "stored blocks";
	}
	
}
