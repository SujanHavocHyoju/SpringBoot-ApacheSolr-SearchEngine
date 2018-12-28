package com.quertle.demo.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.quertle.demo.exceptions.SomethingWrongException;
import com.quertle.demo.model.Author;
import com.quertle.demo.model.FierceNews;
import com.quertle.demo.repository.AuthorRepository;
import com.quertle.demo.repository.FierceNewsRepository;
import com.quertle.demo.utils.DateUtils;

/**
 * This class checks the data source and prints the data received form the
 * source
 * 
 * @author SUJAN
 *
 */
@Service
public class FetchService {
	private static final Logger LOG = LoggerFactory.getLogger(FetchService.class);

	@Autowired
	private FierceNewsRepository fierceNewsRepository;
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Value("${fierceNews.datasource}")
	private String dataSource;
	
	/**
	 * This method prints the xml if data and datasource exists
	 * 
	 * @param dataSource
	 */
	public void saveIfExists(String dataSource) {
		File file = new File("a.xml");
		byte[] fileContent = readFileContent(file, dataSource);
		List<FierceNews> generalFierceNews = parseXML(fileContent);
		setFullText(generalFierceNews);
		LOG.info("News Content: {}", generalFierceNews);
		loadFierceNewsInToDatabase(generalFierceNews);
	}

	public byte[] readFileContent(File file, String dataSource) {
		try {
			FileUtils.copyURLToFile(new URL(dataSource), file, 60000, 10000);
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			throw new SomethingWrongException(e.getMessage(), "Printing of XML is not working");
		}
	}

	/**
	 * This method loads the XML file
	 */
	public List<FierceNews> parseXML(byte[] content) {
		List<FierceNews> fierceNewsList = new ArrayList<>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			ByteArrayInputStream input = new ByteArrayInputStream(content);
			Document doc = dBuilder.parse(input);
			Element root = doc.getDocumentElement();
			LOG.info("Root Name : {} ", root.getNodeName());
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("doc");
			LOG.info("No. of Docs: {} ", nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				FierceNews fierceNews = new FierceNews();
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					setTitle(fierceNews, element);
					setAbstract(fierceNews, element);
					setAuthors(fierceNews, element);
					setPublishedDate(fierceNews, element);
					setUrlLink(fierceNews, element);
				}
				fierceNewsList.add(fierceNews);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fierceNewsList;
	}

	/**
	 * This method sets the FierceNews title
	 * 
	 * @param fierceNews
	 * @param element
	 */
	private void setTitle(FierceNews fierceNews, Element element) {
		NodeList titles = element.getElementsByTagName("title");
		LOG.info("No. of Titles: {} ", titles.getLength());
		if (titles.getLength() >= 1) {
			Node titleNode = titles.item(0);
			String title = titleNode.getTextContent();
			LOG.info("Title: {} ", titleNode.getTextContent());
			fierceNews.setTitle(title);
		}
	}
	
	/**
	 * This method sets the FierceNews title
	 * 
	 * @param fierceNews
	 * @param element
	 */
	private void setAbstract(FierceNews fierceNews, Element element) {
		NodeList abstractContents = element.getElementsByTagName("section");
		LOG.info("No. of Sections: {} ", abstractContents.getLength());
		if (abstractContents.getLength() >= 1) {
			for (int i = 0; i < abstractContents.getLength(); i++) {
				Node abstractContentNode = abstractContents.item(i);
				if (abstractContentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) abstractContentNode;
					String abstractContentText = eElement.getElementsByTagName("text").item(0).getTextContent();
					LOG.info("Abstract Content: {} ", abstractContentText);
					fierceNews.setAbstractContent(abstractContentText);
				}
			}
		}
	}

	/**
	 * This method sets the FierceNews authors
	 * 
	 * @param fierceNews
	 * @param element
	 */
	private void setAuthors(FierceNews fierceNews, Element element) {
		List<Author> authorList = new ArrayList<>();
		NodeList authors = element.getElementsByTagName("author");
		LOG.info("No. of Authors: {} ", authors.getLength());
		if (authors.getLength() >= 1) {
			for (int i = 0; i < authors.getLength(); i++) {
				Node authorNode = authors.item(i);
				String lastName = authorNode.getFirstChild().getTextContent();
				String firstName = authorNode.getLastChild().getTextContent();
				LOG.info("Author : {} ", firstName + " " + lastName);
				authorList.add(new Author(firstName, lastName));
			}
			LOG.info("Author : {} ", authorList);
			fierceNews.setAuthors(authorList);
		}
	}

	/**
	 * This method sets the FierceNews published date
	 * 
	 * @param fierceNews
	 * @param element
	 */
	private void setPublishedDate(FierceNews fierceNews, Element element) {
		NodeList datePublished = element.getElementsByTagName("date-published");
		LOG.info("No. of datePublished: {} ", datePublished.getLength());
		if (datePublished.getLength() >= 1) {
			for (int i = 0; i < datePublished.getLength(); i++) {
				Node datePublishedNode = datePublished.item(i);
				/*Alternative
				 * if(datePublished.item(0)!=null){
					 Node datePublishedNode = datePublished.item(i);
					 String year = datePublishedNode.getFirstChild().getTextContent();
					 String month = datePublishedNode.getFirstChild().getNextSibling().getTextContent();
					 String day = datePublishedNode.getLastChild().getTextContent();
				 }
				 */
				if (datePublishedNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) datePublishedNode;
					String year = eElement.getElementsByTagName("year").item(0).getTextContent();
					String month = eElement.getElementsByTagName("month").item(0).getTextContent();
					String day = eElement.getElementsByTagName("day").item(0).getTextContent();
					LOG.info("Date Published: {} ", year + "/" + month + "/" + day);
					fierceNews.setDatePublished(DateUtils.getDate(year, month, day));
				}
			}
		}
	}
	
	/**
	 * This method sets full text from the URL.
	 * 
	 * @param generalFierceNews
	 */
	public void setFullText(List<FierceNews> generalFierceNews) {
		generalFierceNews.parallelStream().filter(n -> n.getUrlLink() != null).forEach(n -> {
			n.setFullText(getContent(n.getUrlLink()));
		});
		/*for(FierceNews n : generalFierceNews) {
			if(n.getUrlLink() != null) {
				n.setFullText(getContent(n.getUrlLink()));
			}
		}*/
	}
	
	/**
	 * This method returns the full text string by crawling into the url with JSOUP
	 * @param url
	 * @return
	 */
	private String getContent(String url) {
		org.jsoup.nodes.Document doc;
		try {
			doc = Jsoup.connect(url).get();
			return doc.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * This method sets URL link to fiercenews
	 * 
	 * @param fierceNewsDoc
	 * @param eElement
	 */
	private void setUrlLink(FierceNews fierceNewsDoc, Element eElement) {
		NodeList urlLinks = eElement.getElementsByTagName("ext-link");
		LOG.info("No. of urlLink: {}", urlLinks.getLength());
		if (urlLinks.getLength() >= 1) {
			Node fullTextNode = urlLinks.item(0);
			String urlLink = fullTextNode.getTextContent();
			LOG.info("Url Link Value: {}", fullTextNode.getTextContent());
			fierceNewsDoc.setUrlLink(urlLink);
		}
	}

	/**
	 * This method saves/loads FierceNews data into our database
	 * @param generalFierceNews
	 */
	public void loadFierceNewsInToDatabase(List<FierceNews> generalFierceNews) {
		/*for(FierceNews fierceNews: generalFierceNews) {
			List<Author> authors= fierceNews.getAuthors();
			authorRepository.saveAll(authors);
			fierceNewsRepository.save(fierceNews);
		}
		*/	
		fierceNewsRepository.saveAll(generalFierceNews);
		List<Author> authorsList = new ArrayList<>();
		generalFierceNews.stream().forEach(g -> {
			g.getAuthors().forEach(a -> {
				a.setFierceNews(g);
			});
			authorsList.addAll(g.getAuthors());
		});
		authorRepository.saveAll(authorsList);
	}
	
	/**
	 * This menthod Loads xml data from datasource to the database. 
	 * This Async separates the process from the request one which runs in the background.
	 * The return method for Async method should always be void
	 * 
	 */
	@Async
	public void saveFromXML() {
		saveIfExists(dataSource);
		System.out.println("Data Inserted");
	}
}
