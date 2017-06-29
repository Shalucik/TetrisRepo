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
		String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
		SessionMaster session = sessionMap.get(sessionId);
		TetrisMaster tetrisMaster = session.getTetrisMaster();
		TetrisBlock currentBlock = session.getCurrentBlock();
		TetrisBlock nextBlock = session.getCurrentBlockQueue().getLast();
		switch(message.getKeyboardCode()){
			case 32:
				
			case 37:				
				return moveGreeting(new MoveMessage(-1,0), headerAccessor);
			case 38:
				return tetrisMaster.rotationGreeting(currentBlock, nextBlock);
			case 39:
				if(currentBlock == null){
					return new MoveGreeting("no change");
				}				
				return moveGreeting(new MoveMessage(1,0), headerAccessor);
			case 40:				
				return moveGreeting(new MoveMessage(0,1), headerAccessor);
			default:
				return new MoveGreeting("no change");
		}
	}
	

	@MessageMapping("/move")

	@SendToUser
	public Greeting moveGreeting(@Payload MoveMessage message, SimpMessageHeaderAccessor headerAccessor) {		
		
		String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
		SessionMaster session = sessionMap.get(sessionId);
		TetrisMaster tetrisMaster = session.getTetrisMaster();
		ModelMaster modelMaster = session.getModelMaster();
		System.out.println("id: "+ sessionId);
		System.out.println("session: "+ session.getSession().getId());
		if (session.getCurrentBlock() == null) {
			session.getCurrentBlockQueue().addLast(session.getModelMaster().createBlock());
			session.setCurrentBlock(session.getCurrentBlockQueue().removeFirst()); 
			for(Point point : session.getCurrentBlock().getCurrentPositions()){
				if(tetrisMaster.checkGrid(point)){
					tetrisMaster.resetGrid();
					modelMaster.addScore(hRepo, "bla", tetrisMaster.getScore());
					tetrisMaster.setScore(0l);
					return new ResetGreeting("reset");
				}
			}
			TetrisBlock nb = session.getCurrentBlockQueue().getLast();
			return new MoveGreeting("there was no block", new Point[]{}, session.getCurrentBlock().getCurrentPositions(), 
					session.getCurrentBlock().getColor(), nb.getOrientations()[nb.getCurrentOrientation()], nb.getColor());
		}
		
		Greeting greeting = tetrisMaster.moveGreeting(message, session.getCurrentBlock(), session.getCurrentBlockQueue().getLast());
		
		if(greeting == null){
			session.setCurrentBlock(null);
			TetrisBlock nb = session.getCurrentBlockQueue().getLast();
			return new MoveGreeting("new block", nb.getOrientations()[nb.getCurrentOrientation()], nb.getColor());
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
