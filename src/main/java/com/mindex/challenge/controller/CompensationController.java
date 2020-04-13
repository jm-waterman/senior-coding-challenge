package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.transport.CompWithEmployee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;
    
    @GetMapping("/compensation/employee/{id}")
    public CompWithEmployee fetchCWEByEmployeeId(@PathVariable String id) {
    	LOG.debug("Received compensation request for employeeId [{}]", id);
    	
    	return compensationService.fetchCWEByEmployeeId(id);	
    }
    @PostMapping("/compensation/employee") 
	public Compensation createCompFromCWE(@RequestBody CompWithEmployee compWithEmployee){
        LOG.debug("Received compensation create request for compWithEmployee: [{}]", compWithEmployee);
    	return compensationService.createFromCWE(compWithEmployee);
    }
    
    @PostMapping("/compensation")
    public Compensation create(@RequestBody Compensation compensation) {
    	LOG.debug("Received compensation create request for compensation: [{}]");
    	
    	return compensationService.create(compensation);
    	
    }
}
