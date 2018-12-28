package com.quertle.demo.fetch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.quertle.demo.model.FierceNews;
import com.quertle.demo.service.FetchService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchTest {

	private String dataSource;

	@Mock
	private FetchService fetchService;

	@Before
	public void init() {
		//dataSource = "${fierceNews.datasource}";
		dataSource = "http://192.168.50.189:8080/quertle/a.xml";
		//fetchService = new FetchService(); 
	}

	@Test
	public void testFetchMock() {
		File tempFile = new File("file");
		
		List<FierceNews> fierceNewsList = new ArrayList<>();
		fierceNewsList.add(new FierceNews());
		fierceNewsList.add(new FierceNews());
		
		Mockito.when(fetchService.readFileContent(tempFile, dataSource)).thenReturn("".getBytes());
		Mockito.when(fetchService.parseXML("".getBytes())).thenReturn(fierceNewsList);
		Mockito.doNothing().when(fetchService).setFullText(fierceNewsList);
		
		fetchService.saveIfExists(dataSource);
		fetchService.readFileContent(tempFile, dataSource);
		
		Mockito.verify(fetchService, Mockito.times(1)).readFileContent(tempFile, dataSource);
	}
	
	@Test
	public void fetch() {
		fetchService.saveIfExists(dataSource);
	}

}
