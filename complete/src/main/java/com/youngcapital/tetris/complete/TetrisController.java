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
	
	@MessageMapping("/control")
	@SendTo("/app/output")
	public ControlGreeting controlgreeting(ControlMessage message){
		return new ControlGreeting("" + message.getKeyboardCode());
	}
}
