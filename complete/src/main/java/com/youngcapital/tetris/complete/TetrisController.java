package com.youngcapital.tetris.complete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youngcapital.tetris.complete.block.Block;
import com.youngcapital.tetris.complete.block.BlockRepository;
import com.youngcapital.tetris.complete.block.Orientation;
import com.youngcapital.tetris.complete.block.Pos;
import com.youngcapital.tetris.complete.block.PosRepository;
import com.youngcapital.tetris.complete.websocket.ControlGreeting;
import com.youngcapital.tetris.complete.websocket.ControlMessage;

@Controller
public class TetrisController {
	@Autowired
	private BlockRepository bRepo;
	@Autowired
	private PosRepository posRepo;
	
	@RequestMapping("/tetris")
	public String createPage(Model model){
		model.addAttribute("gridWidth", 10);
		model.addAttribute("gridHeight", 20);
		model.addAttribute("block", bRepo.findOne(1L));
		return "tetris";
	}
	
	@MessageMapping("/controls")
	@SendTo("/tetris/output")
	public ControlGreeting controlgreeting(ControlMessage message){
		switch(message.getKeyboardCode()){
			case 37:
			case 38:
			case 39:
			case 40:
				return new ControlGreeting("Correct Input: " + message.getKeyboardCode());
		}
		return new ControlGreeting("Wrong Input, You Cheater!: " + message.getKeyboardCode());
	}
	
	@RequestMapping("/test")
	public @ResponseBody String makeDB() {
		Block block = new Block();

		block.setOrientation0(new Orientation(
				new Pos(0,0), new Pos(0,1), new Pos(0,2), new Pos(0,3)
				));
		block.setOrientation1(new Orientation(
				new Pos(1,1), new Pos(2,1), new Pos(3,1), new Pos(4,1)
				));
		block.setOrientation2(new Orientation(
				new Pos(0,0), new Pos(0,1), new Pos(0,2), new Pos(0,3)
				));
		block.setOrientation3(new Orientation(
				new Pos(1,1), new Pos(2,1), new Pos(3,1), new Pos(4,1)
				));
		
		block.setColor("rgb(255, 0, 0)");
		block.setCurrentOrientation(0);
		Pos tpos = new Pos(4,2);
		posRepo.save(tpos);
		block.setCurrentPos(tpos);
		block.setBlockType("Line");
		
		bRepo.save(block);
		
		return "Added database entries";
	}
}
