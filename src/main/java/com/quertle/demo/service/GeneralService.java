package com.quertle.demo.service;

import org.springframework.stereotype.Service;

import com.quertle.demo.model.General;

@Service
public class GeneralService {
	
	public General getGeneral() {
		General general = new General();
		general.setFirstName("First Name");
		general.setLastName("Last Name");
		return general;
	}
}
