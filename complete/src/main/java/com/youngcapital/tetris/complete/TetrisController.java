package com.youngcapital.tetris.complete;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.youngcapital.tetris.complete.websocket.*;

@Controller
public class TetrisController {
	
	@RequestMapping("/tetris")
	public String createPage(Model model){
		model.addAttribute("gridWidth", 10);
		model.addAttribute("gridHeight", 20);
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
}
