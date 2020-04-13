package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.transport.ReportingStructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);
        
        return employeeRepository.save(employee);
    }
    
    
    @Override
    public ReportingStructure generateReportingStructure(Employee employee) { 
    	LOG.debug("Generating reporting structure for employee [{}]", employee);
    	
    	List<Employee> allReports = buildAllReportsList(employee);
    	
    	ReportingStructure reportingStructure = new ReportingStructure();
    	reportingStructure.setEmployee(employee);
    	reportingStructure.setNumberOfReports(allReports.size());
    	
    	return reportingStructure;
    }
    
    private List<Employee> buildAllReportsList(Employee employee) {
    	List<Employee> allReports = new ArrayList();
    	
    	List<Employee> directReports = employee.getDirectReports();
    	if (directReports != null) {
	    	allReports = directReports.stream().map((directReport) -> {
	    			List<Employee> runningList = new ArrayList();
	    			Employee fullyFormedDirectReport = read(directReport.getEmployeeId());
	    			if (fullyFormedDirectReport != null) {
	    				runningList.add(fullyFormedDirectReport);
	    				runningList.addAll(buildAllReportsList(fullyFormedDirectReport));
	    			}
	    			return runningList;
	    		}).flatMap(list -> list.stream()).collect(Collectors.toList());
	    
    	}
    	return allReports;
    }
}
