package com.quertle.demo.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrDocument;

@Entity
public class FierceNews {

	private static final String DATE_PUBLISHED = "datePublished";

	private static final String URL_LINK = "urlLink";

	private static final String TITLE = "title";
	
	private static final String FULL_TEXT = "fullText";
	
	private static final String ABSTRACT = "abstract";
	
	private static final String AUTHORS = "authors";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String title;

	/*
	 * @JoinTable(name = "fierce_author", joinColumns = @JoinColumn(name =
	 * "fierce_id", referencedColumnName = "id"), inverseJoinColumns
	 * = @JoinColumn(name = "author_id", referencedColumnName = "id"))
	 */
	@OneToMany(mappedBy = "fierceNews", fetch = FetchType.EAGER)
	private List<Author> authors;

	private Date datePublished;
	
	@Column(columnDefinition = "text")
	private String abstractContent;

	@Column(columnDefinition = "text")
	private String fullText;
	private String urlLink;

	public Integer getId() {
		return id;
	}

	//@Field("id")
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	//@Field("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	//@Field("authors")
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public Date getDatePublished() {
		return datePublished;
	}

	//@Field("date_published")
	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}

	public String getAbstractContent() {
		return abstractContent;
	}

	//@Field("abstract_content")
	public void setAbstractContent(String abstractContent) {
		this.abstractContent = abstractContent;
	}

	public String getFullText() {
		return fullText;
	}

	//@Field("full_text")
	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public String getUrlLink() {
		return urlLink;
	}

	//@Field("url_link")
	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public static FierceNews getFierceNews(Document document) throws ParseException {
		FierceNews n = new FierceNews();
		n.setTitle(document.get(TITLE));
		String strDate = document.get(DATE_PUBLISHED);
		n.setDatePublished(new SimpleDateFormat("YYYY-MM-dd").parse(strDate.substring(0, strDate.indexOf(" "))));
		n.setUrlLink(document.get(URL_LINK));
		n.setAbstractContent(document.get(ABSTRACT));
		return n;
	}
	
	public static String[] getFields() {
		String[] s = { AUTHORS, ABSTRACT, FULL_TEXT, TITLE, DATE_PUBLISHED, URL_LINK };
		return s;
	}
	
	public static Occur[] getFlags() {
		BooleanClause.Occur[] flags = { BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
				BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD };
		return flags;
	}

	public static FierceNews getSolrFierceNews(SolrDocument doc) throws ParseException {
		FierceNews n = new FierceNews();
		n.setTitle(doc.getFieldValue(TITLE).toString());
		String strDate = doc.get("date_published").toString();
		n.setDatePublished(new SimpleDateFormat("YYYY-MM-dd").parse(strDate.substring(0, strDate.indexOf(" "))));
		n.setUrlLink(doc.getFieldValue(URL_LINK).toString());
		String authors = doc.getFieldValue(AUTHORS).toString();
		if (authors != null && !authors.isEmpty()) {
			String[] auths = authors.split(",");
			List<Author> authorList = new ArrayList<>();
			for (String auth : auths) {
				if (!auth.trim().isEmpty()) {
					String[] names = auth.split(" ");
					Author author = new Author(names[0], names[1]);
					authorList.add(author);
				}
			}
			if (!authorList.isEmpty())
				n.setAuthors(authorList);
		}
		return n;
	}
}
