package com.quertle.demo.service.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quertle.demo.model.Author;
import com.quertle.demo.model.FierceNews;
import com.quertle.demo.repository.AuthorRepository;
import com.quertle.demo.repository.FierceNewsRepository;

@Service
public class LuceneIndexService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LuceneIndexService.class);
	
	@Autowired
	private FierceNewsRepository fierceNewsRepository;
	
	@Autowired
	private AuthorRepository authorRepository;
	
	private static final String INDEX_DIR = "D:/lucene/index";

	/**
	 * This method creates and indexes lucene documents from database
	 */
	public void indexFromDatabase() {
		IndexWriter writer = createWriter();
		List<Document> documents = 	getConvertedDocuments();
		try {
			writer.deleteAll();
	        writer.addDocuments(documents);
	        writer.commit();
	        writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function gets all the data from database and makes lucene Documents
	 * @return
	 */
	private List<Document> getConvertedDocuments() {
		List<FierceNews> fierceNewsList = fierceNewsRepository.findAll();
		LOG.info("Records retreived from Database");
		List<Document> documentList = new ArrayList<>();
		fierceNewsList.stream().forEach(f -> {
			LOG.info("FierceNews: {}", f.toString());
			Document document = createDocument(
						f.getId(), 
						f.getTitle(), 
						f.getDatePublished(),
						f.getAuthors(),
						f.getAbstractContent(), 
						f.getFullText(),
						f.getUrlLink()
						);
			documentList.add(document);
		});
		LOG.info("Records Indexed from Database");
		return documentList;
	}

	/**
	 * This method creates Lucene Document using received parementers from the database results 
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
	private static Document createDocument(Integer id, String title, Date datePublished, List<Author> authors, String abstractContent, String fullText, String urlLink) {
		Document document = new Document();
		document.add(new StringField("id", id.toString(), Store.YES));
		document.add(new TextField("title", title, Store.YES));
		document.add(new TextField("datePublished", datePublished.toString(), Store.YES));
		final StringBuilder authorNames = new StringBuilder();
		if(authors != null && authors.size() > 0)
			authors.stream().forEach(a -> {
			authorNames.append(a.getFirstName() +" "+ a.getLastName()+",");
		});	
		document.add(new TextField("author", authorNames.toString(), Store.YES));
		if(abstractContent != null)
		document.add(new TextField("abstract", abstractContent, Store.YES));
		if(fullText != null)
		document.add(new TextField("fullText", fullText, Store.YES));
		if(urlLink != null)
		document.add(new TextField("urlLink", urlLink, Store.YES));
		
		return document;
	}

	/**
	 * This method writes index file
	 * @return
	 */
	private static IndexWriter createWriter(){
	    FSDirectory dir;
	    IndexWriter writer = null;
		try {
			dir = FSDirectory.open(Paths.get(INDEX_DIR));
		    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		    writer = new IndexWriter(dir, config);
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    return writer;
	}
	
}
