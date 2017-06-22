package com.youngcapital.tetris.complete;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TetrisController {
	
	@RequestMapping("/tetris")
	public String createPage(Model model){
		model.addAttribute("gridWidth", 10);
		model.addAttribute("gridHeight", 20);
		return "tetris";
	}
}
