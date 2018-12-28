package com.quertle.demo.service.solr;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quertle.demo.model.Author;
import com.quertle.demo.model.FierceNews;
import com.quertle.demo.repository.FierceNewsRepository;
import com.quertle.demo.utils.SolrUtils;

@Service
public class SolrIndexService {

	private static final Logger LOG = LoggerFactory.getLogger(SolrIndexService.class);

	@Autowired
	private SolrUtils solrClient;

	@Autowired
	private FierceNewsRepository fierceNewsRepository;

	HttpSolrClient solr;

	/**
	 * This method starts the Solr indexing process
	 * 
	 */
	public void indexToSolr() {
		solr = solrClient.initializeSolr();
		indexFromBean(solr);
	}

	/**
	 * This method gets FierceNews object from database and indexes to Solr
	 * 
	 * @param solr2
	 */
	private void indexFromBean(HttpSolrClient solr) {
		List<FierceNews> fierceList = fierceNewsRepository.findAll();
		fierceList.stream().forEach(f -> {
			LOG.info("FierceNews: {}", f.toString());
			try {
				SolrInputDocument document = createSolrDocument(f.getId(), f.getTitle(), f.getDatePublished(),
						f.getAuthors(), f.getAbstractContent(), f.getFullText(), f.getUrlLink());

				solr.add(document);
				solr.commit();
			} catch (IOException | SolrServerException e) {
				e.printStackTrace();
			}
		});
		LOG.info("Records Indexed into Solr Core from Database");
	}

	/**
	 * This method creates Solr Document using received parementers from the
	 * database results
	 * 
	 * @param id
	 * @param title
	 * @param datePublished
	 * @param author
	 * @param abstractContent
	 * @param fullText
	 * @param urlLink
	 * @return
	 */
	private SolrInputDocument createSolrDocument(Integer id, String title, Date datePublished, List<Author> authors,
			String abstractContent, String fullText, String urlLink) {
		SolrInputDocument document = new SolrInputDocument();
		LOG.info("Id : {} ", id.toString());
		// document.addField("id", id.toString());
		LOG.info("Title : {} ", title);
		document.addField("title", title);
		if (authors != null && authors.size() > 0)
			for (Author author : authors) {
				document.addField("author", author);
				LOG.info("Author : {} ", author);
			}
		// document.addField("authors", f.getAuthors());
		LOG.info("Date Published : {} ", datePublished.toString());
		document.addField("date_published", datePublished.toString());
		if (abstractContent != null)
			document.addField("abstract_content", abstractContent);
		if (fullText != null)
			document.addField("full_text", fullText);
		if (urlLink != null)
			document.addField("url_link", urlLink);
		//LOG.info("Doc: {}", document);
		return document;
	}

}
