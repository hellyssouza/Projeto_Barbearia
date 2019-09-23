package com.projetofinal.Barbearia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.projetofinal.Barbearia.servico.ServicoDeAtendimento;

@Controller
public class InicioController {
	@Autowired
	private ServicoDeAtendimento servico;
	
	@RequestMapping(method = RequestMethod.GET, value="/login")
	public ModelAndView login() {
		ModelAndView view = new ModelAndView("login"); 
		
		return view;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/index")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
	
	@RequestMapping(value="/atendimento", method = RequestMethod.GET)
	public ModelAndView atendimento(Model model) {
		ModelAndView view = new ModelAndView("atendimento");
		
		model.addAttribute("atendimentos", servico.obtenhaTodos());
		
		return view;
	}	
}
