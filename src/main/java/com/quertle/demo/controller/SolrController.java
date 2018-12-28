package com.quertle.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quertle.demo.dto.Address;
import com.quertle.demo.dto.SearchDto;
import com.quertle.demo.model.General;
import com.quertle.demo.service.FetchService;
import com.quertle.demo.service.GeneralService;
import com.quertle.demo.service.lucene.LuceneIndexService;
import com.quertle.demo.service.lucene.LuceneSearchService;
import com.quertle.demo.service.solr.SolrIndexService;
import com.quertle.demo.service.solr.SolrSearchService;

@RestController
public class SolrController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SolrController.class);
	
	//@Autowired
	public GeneralService generalService;
	
	@Autowired
	public SolrIndexService solrIndexService;
	
	@Autowired
	public SolrSearchService solrSearchService;
	
	//using a constructor instead of using @Autowired annotation
	public SolrController(GeneralService generalService) {
		this.generalService = generalService;
	}
	
	@RequestMapping("/solr-index")
	public void indexToSolr() {
		solrIndexService.indexToSolr();
	}
	
	@PostMapping("/solr-search")
	public String searchFromSolr(@ModelAttribute SearchDto sdto, Model model) {
		LOG.info("Form Query posted Requested for search: {}", sdto.getContent());
		model.addAttribute("search", solrSearchService.serchFromIndex(sdto.getContent()));
		return "result";
	}
	
	
}
