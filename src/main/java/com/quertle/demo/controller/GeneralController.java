package com.quertle.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quertle.demo.dto.Address;
import com.quertle.demo.model.General;
import com.quertle.demo.service.FetchService;
import com.quertle.demo.service.GeneralService;
import com.quertle.demo.service.lucene.LuceneIndexService;
import com.quertle.demo.service.lucene.LuceneSearchService;
import com.quertle.demo.service.solr.SolrIndexService;

@RestController
public class GeneralController {
	
	private static final Logger LOG = LoggerFactory.getLogger(GeneralController.class);
	
	//@Autowired
	public GeneralService generalService;
	
	@Autowired
	public FetchService fetchService;
	
	@Autowired
	public LuceneIndexService indexService;
	
	@Autowired
	public SolrIndexService solrIndexService;
	
	
	
	//using a constructor instead of using @Autowired annotation
	public GeneralController(GeneralService generalService) {
		this.generalService = generalService;
	}
	
	@RequestMapping("/")
	public General getGeneral() {
		LOG.info("General Service: "+ generalService.getGeneral());
		LOG.info("hello {} (false)", generalService == null);
		return generalService.getGeneral();
	}
	
	@PostMapping("/location")
	public Address receivedAddress(@RequestBody Address address) {
		System.out.println("Address: "+ address.getLocation());
		LOG.info("Address: "+ address.getLocation());
		address.setLocation(address.getLocation() + " Location12345");
		return address;
	}
	
	@RequestMapping("/load-xml")
	public void loadXMLIntoDatabase() {
		fetchService.saveFromXML();
	}
	
	@RequestMapping("/index")
	public void indexFromDatabase() {
		indexService.indexFromDatabase();
	}
	
	
}
