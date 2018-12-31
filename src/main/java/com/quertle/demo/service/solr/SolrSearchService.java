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
	public SearchDto searchFromSolr(String termToSearch) {
		solr = solrClient.initializeSolr();
		//serchFromIndex(termToSearch);
		return serch(termToSearch);
	}

	/**
	 * This method searches for documents from Solr index
	 * 
	 */
	public SearchDto serchFromIndex(String termToSearch) {
		LOG.info("Searching Records");
		SolrQuery query = new SolrQuery();
		try {
			// Search by QueryResponse
			query.setQuery("first_name:"+termToSearch+ " OR id:"+termToSearch+"");
			QueryResponse response = solr.query(query);
			SolrDocumentList docList = response.getResults();
			
			List<FierceNews> news = new ArrayList<>();
			for (SolrDocument doc : docList) {
			     LOG.info((String) doc.getFieldValue("title"));
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
	
	/**
	 * This is alternative method which searches for documents from Solr index
	 * 
	 */
	public SearchDto serch(String termToSearch) {
		LOG.info("Searching Records From Solr Index");
		List<FierceNews> news = new ArrayList<>();
		LOG.info("Search Term :{}", termToSearch);
		SolrQuery query = new SolrQuery();
		query.set("q", getQuery(termToSearch));
		LOG.info("Solr Query :{}", query);
		QueryResponse response;
		try {
			response = solr.query(query);
			SolrDocumentList docList = response.getResults();
			for (SolrDocument doc : docList) {
			     LOG.info("Title: {}",(String) doc.getFieldValue("title"));
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

	private String getQuery(String termToSearch) {
		StringBuffer fullQuery = new StringBuffer();
		String[] fields = FierceNews.getFields();
		for (String f : fields) {
			if(!f.equalsIgnoreCase("datePublished") )
			fullQuery.append(f).append(":").append(termToSearch).append(" or ");
		}
		String tempQuery = fullQuery.toString();
		tempQuery = tempQuery.substring(0, tempQuery.lastIndexOf("or"));
		return tempQuery;
	}

}
