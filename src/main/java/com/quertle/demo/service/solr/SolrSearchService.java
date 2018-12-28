package com.quertle.demo.service.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quertle.demo.dto.SearchDto;
import com.quertle.demo.model.FierceNews;
import com.quertle.demo.repository.FierceNewsRepository;
import com.quertle.demo.utils.SolrUtils;

@Service
public class SolrSearchService {

	private static final Logger LOG = LoggerFactory.getLogger(SolrSearchService.class);
	
	@Autowired
	private SolrUtils solrClient;
	
	@Autowired
	private FierceNewsRepository fierceNewsRepository;
	
	HttpSolrClient solr;
	
	/**
	 * This method starts the Solr Search process
	 * 
	 */
	public void searchFromSolr(String termToSearch) {
		solr = solrClient.initializeSolr();
		serchFromIndex(termToSearch);
	}

	/**
	 * This method searches for documents from Lucene index
	 * 
	 */
	public SearchDto serchFromIndex(String termToSearch) {
		LOG.info("Searching Records");
		SolrQuery query = new SolrQuery();
		try {
			// Search by QUeryResponse
			query.setQuery("first_name:"+termToSearch+ "OR id:"+termToSearch+"");
			QueryResponse response = solr.query(query);
			SolrDocumentList docList = response.getResults();
			//assertEquals(docList.getNumFound(), 1);
			
			List<FierceNews> news = new ArrayList<>();
			for (SolrDocument doc : docList) {
			     LOG.info((String) doc.getFieldValue("title"));
			     //assertEquals((Double) doc.getFieldValue("price"), (Double) 599.99);
			     FierceNews fierceNews = FierceNews.getSolrFierceNews(doc);
			     news.add(fierceNews);
			     LOG.info("Title Results: {}", fierceNews.getTitle());
			}
			return new SearchDto(termToSearch, news);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
