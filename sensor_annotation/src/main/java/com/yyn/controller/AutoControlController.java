package com.yyn.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yyn.service.AutoControlService;
import com.yyn.util.RDFReasoning;

@Controller
@RequestMapping("/auto*.do")
public class AutoControlController {
	@Autowired
	private AutoControlService acs;
	
	@RequestMapping("/autoGenerator.do")
	public String generateAction(HttpServletRequest request) {
		Dataset ds = (Dataset)request.getSession().getServletContext().getAttribute("dataset");
		acs.generateControlModel(ds);
		RDFReasoning.output(ds);
		return "redirect:/index.jsp";
	}
}
