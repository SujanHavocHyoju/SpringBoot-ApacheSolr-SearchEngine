package com.quertle.demo.utils;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SolrUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(SolrUtils.class);
	
	@Value("${solr.url}")
	private String solrUrl;
	
	/**
	 * This method initializes Apache Solr
	 * 
	 */
	@Bean
	public HttpSolrClient initializeSolr() {
		String urlString = solrUrl;
		LOG.info("Solr URL : {} ", urlString);
		HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
		solr.setParser(new XMLResponseParser());
		return solr;
	}

}
