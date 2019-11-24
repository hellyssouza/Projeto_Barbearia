package com.projetofinal.Barbearia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InicioController {
	@RequestMapping(method = RequestMethod.GET, value = "/index")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@RequestMapping(value = "/atendimento", method = RequestMethod.GET)
	public ModelAndView atendimento() {
		return new ModelAndView("atendimento");
	}

	@RequestMapping(value = "/funcionario", method = RequestMethod.GET)
	public ModelAndView funcionario() {
		return new ModelAndView("funcionario");
	}
	
	@RequestMapping(value = "/agendamento", method = RequestMethod.GET)
	public ModelAndView agendamento() {
		return new ModelAndView("agendamento");
	}
	
	@RequestMapping(value = "/servico", method = RequestMethod.GET)
	public ModelAndView servico() {
		return new ModelAndView("servico");
	}
	
	@RequestMapping(value = "/relatorioatendimento", method = RequestMethod.GET)
	public ModelAndView relatorioatendimento() {
		return new ModelAndView("relatorioatendimento");
	}
}
