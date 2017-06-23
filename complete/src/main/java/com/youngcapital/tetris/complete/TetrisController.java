package com.youngcapital.tetris.complete;

import java.awt.Point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.youngcapital.tetris.complete.websocket.ControlMessage;
import com.youngcapital.tetris.complete.websocket.Greeting;

@Controller
public class TetrisController {
	
	@Autowired
	protected SimpMessagingTemplate template;
	
	@RequestMapping("/tetris")
	public String createPage(Model model){
		model.addAttribute("gridHeight", 19);
		model.addAttribute("gridWidth", 9);
		return "tetris";
	}
	
	@MessageMapping("/controls")
	@SendTo("/tetris/output")
	public Greeting controlgreeting(ControlMessage message){
		switch(message.getKeyboardCode()){
			case 37:
			case 38:
			case 39:
			case 40:
				return new Greeting("Correct Input: " + message.getKeyboardCode());
		}
		return new Greeting("Wrong Input, You Cheater!: " + message.getKeyboardCode());
	}
	
	@MessageMapping("/init")	
	public void initGreeting() {
		test();
		//return new Greeting(new Point[]{}, new Point[]{new Point(4,0)}, "red");
	}
	
	public void test(){
		template.convertAndSend("/tetris/init", new Greeting(new Point[]{}, new Point[]{new Point(4,0)}, "red"));
	}
}
