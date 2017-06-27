package com.youngcapital.tetris.complete;

import java.awt.Point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youngcapital.tetris.complete.block.BlockRepository;
import com.youngcapital.tetris.complete.block.TetrisBlock;
import com.youngcapital.tetris.complete.websocket.ControlMessage;
import com.youngcapital.tetris.complete.websocket.Greeting;
import com.youngcapital.tetris.complete.websocket.MoveGreeting;
import com.youngcapital.tetris.complete.websocket.MoveMessage;
import com.youngcapital.tetris.complete.websocket.ResetGreeting;

@Controller
public class SocketController {
	
	@Autowired
	private BlockRepository bRepo;

	private TetrisMaster tetrisMaster;
	private ModelMaster modelMaster;
	private TetrisBlock currentBlock;

	@RequestMapping("/tetris")
	public String createPage(Model model){
		tetrisMaster = new TetrisMaster(10, 20);
		modelMaster = new ModelMaster(bRepo);
		
		model.addAttribute("gridHeight", 20-1);
		model.addAttribute("gridWidth", 10-1);	
		currentBlock = null;
		return "tetris";
	}

	@MessageMapping("/controls")
	@SendTo("/tetris/output")
	public synchronized Greeting controlgreeting(ControlMessage message){
		switch(message.getKeyboardCode()){
			case 37:
				return moveGreeting(new MoveMessage(-1,0));
			case 38:
				return tetrisMaster.rotationGreeting(currentBlock);
			case 39:
				if(currentBlock == null){
					return new MoveGreeting("no change");
				}
				return moveGreeting(new MoveMessage(1,0));
			case 40:
				return moveGreeting(new MoveMessage(0,1));
			default:
				return new MoveGreeting("no change");
		}
	}

	@MessageMapping("/move")
	@SendTo("/tetris/move")
	public Greeting moveGreeting(MoveMessage message) {
		if (currentBlock == null) {
			currentBlock = modelMaster.createBlock(currentBlock);
			for(Point point : currentBlock.getCurrentPositions()){
				if(tetrisMaster.checkGrid(point)){
					tetrisMaster.resetGrid();
					return new ResetGreeting("reset");
				}
			}			
			return new MoveGreeting("there was no block", new Point[]{}, currentBlock.getCurrentPositions(), currentBlock.getColor());
		}
		Greeting greeting = tetrisMaster.moveGreeting(message, currentBlock);
		if(greeting == null){
			currentBlock = null;
			return new MoveGreeting("new block");
		} else if(greeting.getStatus() == 1) {
			currentBlock = null;
		}
		return greeting;
	}
	
	
	

	@RequestMapping("/test")
	public @ResponseBody String makeDB() {
		return modelMaster.makeDB();
	}
}
