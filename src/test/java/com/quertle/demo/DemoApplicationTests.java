package com.quertle.demo;

import org.junit.Before;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.quertle.demo.controller.GeneralController;
import com.quertle.demo.model.FierceNews;
import com.quertle.demo.model.General;
import com.quertle.demo.repository.FierceNewsRepository;
import com.quertle.demo.service.GeneralService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Mock
	private GeneralService generalService;
	
	@Mock
	private FierceNewsRepository fierceNewsRepo;
	
	@Before
	public void init() {
		//generalService = new GeneralService();
	}
	
	@Test
	public void contextLoads() {
		General answer = new General();
		answer.setFirstName("First Name");
		answer.setLastName("Last Name");
		Mockito.when(generalService.getGeneral()).thenReturn(answer);
		
		GeneralController generalController = new GeneralController(this.generalService);
		General general = generalController.getGeneral();
		Assert.assertEquals(general.getFirstName(), "First Name");
		Assert.assertEquals(general.getLastName(), "Last Name");
		
		Mockito.when(fierceNewsRepo.getOne(1)).thenReturn(new FierceNews());  //Db Id value
		Mockito.when(fierceNewsRepo.findAll()).thenReturn(new ArrayList<FierceNews>());
	}
	
	@Test
	public void testMock() {
		General answer = new General();
		answer.setFirstName("First Name");
		answer.setLastName("Last Name");
		Mockito.when(generalService.getGeneral()).thenReturn(answer);
		
		System.out.println(generalService.getGeneral());
	}
	
}
