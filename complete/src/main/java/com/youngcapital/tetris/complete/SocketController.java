package com.youngcapital.tetris.complete;

import java.awt.Point;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youngcapital.tetris.complete.block.BlockRepository;
import com.youngcapital.tetris.complete.block.HighscoreRepository;
import com.youngcapital.tetris.complete.block.TetrisBlock;
import com.youngcapital.tetris.complete.websocket.ControlMessage;
import com.youngcapital.tetris.complete.websocket.Greeting;
import com.youngcapital.tetris.complete.websocket.MoveGreeting;
import com.youngcapital.tetris.complete.websocket.MoveMessage;
import com.youngcapital.tetris.complete.websocket.ResetGreeting;
import com.youngcapital.tetris.complete.websocket.ScoreMessage;

@Controller
public class SocketController {
	
	@Autowired
	private BlockRepository bRepo;
	
	@Autowired
	private HighscoreRepository hRepo;
	
	@Autowired
	SimpMessagingTemplate template;
	
	private HashMap<String, SessionMaster> sessionMap = new HashMap<String, SessionMaster>();

	@RequestMapping("/tetris")
	public String createPage(Model model, HttpSession session){
		
		sessionMap.put(session.getId(), new SessionMaster(session, new TetrisMaster(10, 20), new ModelMaster(bRepo), null));
		
		model.addAttribute("gridHeight", 20-1);
		model.addAttribute("gridWidth", 10-1);	
		return "tetris";
	}

	@MessageMapping("/controls")
	@SendToUser
	public synchronized Greeting controlgreeting(ControlMessage message, SimpMessageHeaderAccessor headerAccessor){		
		SessionMaster session = getSessionFromHeader(headerAccessor);
		TetrisMaster tetrisMaster = session.getTetrisMaster();
		TetrisBlock currentBlock = session.getCurrentBlock();
		TetrisBlock nextBlock = session.getCurrentBlockQueue().getLast();
		if(session.getCurrentBlock() == null)
			return new MoveGreeting("no change");
		switch(message.getKeyboardCode()){
			case 32:
				return tetrisMaster.dropBlock(currentBlock, session.getCurrentBlockQueue().getLast());
			case 37:
				return tetrisMaster.moveGreeting(new MoveMessage(-1,0), currentBlock, nextBlock);
			case 38:
				return tetrisMaster.rotationGreeting(currentBlock, nextBlock);
			case 39:				
				return moveBlock(new MoveMessage(1,0), session);
			case 40:				
				return moveBlock(new MoveMessage(0,1), session);
			default:
				return new MoveGreeting("no change");
		}
	}
	

	@MessageMapping("/move")
	@SendToUser
	public synchronized Greeting moveGreeting(@Payload MoveMessage message, SimpMessageHeaderAccessor headerAccessor) {				
		SessionMaster session = getSessionFromHeader(headerAccessor);
		if(session.getCurrentBlock() == null){
			return newBlock(message, session);
		}
		return moveBlock(message, session);
	}
	
	@MessageMapping("/reset")
	@SendToUser
	public Greeting reset(SimpMessageHeaderAccessor headerAccessor){
		SessionMaster session = getSessionFromHeader(headerAccessor);
		session.getTetrisMaster().resetGrid();
		session.setCurrentBlock(null);
		session.getCurrentBlockQueue().removeFirst();
		session.getCurrentBlockQueue().addLast(session.getModelMaster().createBlock());;
		return new ResetGreeting("Reset");
	}
	
	@MessageMapping("/score")
	public void score(@Payload ScoreMessage message, SimpMessageHeaderAccessor headerAccessor){
		SessionMaster session = getSessionFromHeader(headerAccessor);
		session.getModelMaster().addScore(hRepo, message.getName(), session.getTetrisMaster().getScore());
		session.getTetrisMaster().setScore(0l);
		
	}
	
	private SessionMaster getSessionFromHeader(SimpMessageHeaderAccessor headerAccessor){
		return sessionMap.get(headerAccessor.getSessionAttributes().get("sessionId").toString());
	}
	
	public Greeting newBlock(MoveMessage message, SessionMaster session){		
		TetrisMaster tetrisMaster = session.getTetrisMaster();
		ModelMaster modelMaster = session.getModelMaster();
		session.getCurrentBlockQueue().addLast(modelMaster.createBlock());
		session.setCurrentBlock(session.getCurrentBlockQueue().removeFirst()); 
		for(Point point : session.getCurrentBlock().getCurrentPositions()){
			if(tetrisMaster.checkGrid(point)){
				tetrisMaster.resetGrid();								
				return new ResetGreeting("reset");
			}
		}
		TetrisBlock nb = session.getCurrentBlockQueue().getLast();
		return new MoveGreeting("there was no block", new Point[]{}, session.getCurrentBlock().getCurrentPositions(), 
				session.getCurrentBlock().getColor(), nb.getOrientations()[nb.getCurrentOrientation()], nb.getColor());		
	}
	
	public synchronized Greeting moveBlock(MoveMessage message, SessionMaster session){		
		TetrisMaster tetrisMaster = session.getTetrisMaster();		
		Greeting greeting = tetrisMaster.moveGreeting(message, session.getCurrentBlock(), session.getCurrentBlockQueue().getLast());
		
		if(greeting == null){
			session.setCurrentBlock(null);			
			return new MoveGreeting("new block");
		} else if(greeting.getStatus() == 1) {
			session.setCurrentBlock(null);
		}
		return greeting;
	}
	
	
	

	@RequestMapping("/test")
	public @ResponseBody String makeDB() {
		ModelMaster modelMaster = new ModelMaster(bRepo);
		return modelMaster.makeDB();
	}
}
