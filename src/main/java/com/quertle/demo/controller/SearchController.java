package com.quertle.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quertle.demo.dto.SearchDto;
import com.quertle.demo.service.lucene.LuceneSearchService;

@Controller
public class SearchController {

	private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	public LuceneSearchService searchService;

	@RequestMapping("/search")
	public Model search(Model model) {
		LOG.info("Requested for search");
		model.addAttribute("search", new SearchDto());
		return model;
	}

	@PostMapping("/search")
	public String search(@ModelAttribute SearchDto sdto, Model model) {
		LOG.info("Form Query posted Requested for search: {}", sdto.getContent());
		model.addAttribute("search", searchService.serchFromIndex(sdto.getContent()));
		return "result";
	}

	/*
	 * @RequestMapping(method = RequestMethod.GET) public ModelAndView search(Model
	 * model) { ModelAndView mv = new ModelAndView("search"); mv.addObject("search",
	 * new SearchDto()); LOG.info("Requested for search"); return mv; }
	 */

}
