package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.transport.CompWithEmployee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
	private String compensationCWEUrl;
	private String compensationCompUrl;
	private String compensationWithEmpIdUrl;
	
	
	@Autowired
	private CompensationService compensationService;
	
	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationCWEUrl = "http://localhost:" + port + "/compensation/employee";
        compensationCompUrl = "http://localhost:" + port + "/compensation";
        compensationWithEmpIdUrl = "http://localhost:" + port + "/compensation/employee/{id}";
    }
    
    @Test
    public void testCreateReadUpdateFromCWE() throws ParseException {
    	// Create a test employee to attach the comp to
    	Employee testEmployee = new Employee();
    	testEmployee.setFirstName("William");
    	testEmployee.setLastName("Riker");
    	testEmployee.setDepartment("Command");
    	testEmployee.setPosition("First Officer");
    	testEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
    
    	// Create the test compensation
    	Compensation testComp = new Compensation();
    	testComp.setSalary(150000.0);
    	testComp.setEffectiveDate(LocalDate.of(2020, Month.APRIL, 11));
    	testComp.setEmployeeId(testEmployee.getEmployeeId());
    	    	
    	// Create the CompWithEmployee object that will be passed in to create the comp
    	CompWithEmployee testCompWithEmployee = new CompWithEmployee();
    	testCompWithEmployee.setSalary(testComp.getSalary());
    	testCompWithEmployee.setEffectiveDate(testComp.getEffectiveDate());
    	testCompWithEmployee.setEmployee(testEmployee);
    	
    	
    	// Test the creation
    	Compensation createdComp = restTemplate.postForEntity(compensationCWEUrl, testCompWithEmployee, Compensation.class).getBody();
    	assertCompensationEquivalence(testComp, createdComp);
    	
    	// Test the reading
    	CompWithEmployee readCompWithEmployee = restTemplate.getForEntity(compensationWithEmpIdUrl, CompWithEmployee.class, testEmployee.getEmployeeId()).getBody();
    	Compensation readComp = new Compensation();
    	readComp.setCompensationId(readCompWithEmployee.getCompensationId());
    	readComp.setSalary(readCompWithEmployee.getSalary());
    	readComp.setEffectiveDate(readCompWithEmployee.getEffectiveDate());
    	readComp.setEmployeeId(readCompWithEmployee.getEmployeeId());
    	
    	assertEquals(createdComp.getCompensationId(), readComp.getCompensationId());
    	assertCompensationEquivalence(createdComp, readComp);
    	
    	//Test the updating
    	readComp.setSalary(175000.0);
    	readComp.setEffectiveDate(LocalDate.of(2021, Month.APRIL, 11));
    	readCompWithEmployee.setSalary(readComp.getSalary());
    	readCompWithEmployee.setEffectiveDate(readComp.getEffectiveDate());
    	
    	Compensation updatedComp = restTemplate.postForEntity(compensationCWEUrl, readCompWithEmployee, Compensation.class).getBody();
    	
    	assertEquals(readComp.getCompensationId(), updatedComp.getCompensationId());
    	assertCompensationEquivalence(readComp, updatedComp);
    }
    
    @Test
    public void testCreateFromComp() throws ParseException {
    	// Create a test employee to attach the comp to
    	Employee testEmployee = new Employee();
    	testEmployee.setFirstName("Jordi");
    	testEmployee.setLastName("LaForge");
    	testEmployee.setDepartment("Command");
    	testEmployee.setPosition("Helmsman");
    	testEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
    
    	// Create the test compensation
    	Compensation testComp = new Compensation();
    	testComp.setSalary(125000.0);
    	testComp.setEffectiveDate(LocalDate.of(2020, Month.APRIL, 11));
    	testComp.setEmployeeId(testEmployee.getEmployeeId());
    	
    	// Test the creation
    	Compensation createdComp = restTemplate.postForEntity(compensationCompUrl, testComp, Compensation.class).getBody();
    	assertCompensationEquivalence(testComp, createdComp);
    }
    
    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
    	assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
    }
	
}
