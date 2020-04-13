package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.transport.ReportingStructure;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reporting-structure";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);

        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

	@Test
    public void testReportingStructureGenerate() {
		//Add the test employees for the reporting structure (1 indirect, 2 direct, 3 total)
		Employee testIndirectReport1 = new Employee();
    	testIndirectReport1 = restTemplate.postForEntity(employeeUrl, testIndirectReport1, Employee.class).getBody();
    	
    	List<Employee> indirectReports = new ArrayList<Employee>();
    	indirectReports.add(testIndirectReport1);
    	
		Employee testDirectReport1 = new Employee();
		testDirectReport1.setDirectReports(indirectReports);
		testDirectReport1 = restTemplate.postForEntity(employeeUrl, testDirectReport1, Employee.class).getBody();
		
		Employee testDirectReport2 = new Employee();
		testDirectReport2 = restTemplate.postForEntity(employeeUrl, testDirectReport2, Employee.class).getBody();
		
		List<Employee> directReports = new ArrayList<Employee>();
    	directReports.add(testDirectReport1);
    	directReports.add(testDirectReport2);
    	
    	Employee testManager = new Employee();
    	testManager.setDirectReports(directReports);
    	testManager.setFirstName("JeanLuc");
    	testManager.setLastName("Picard");
    	testManager.setDepartment("Command");
    	testManager.setPosition("Captain");
    	testManager = restTemplate.postForEntity(employeeUrl, testManager, Employee.class).getBody();
    	
    	//Run the tests
    	ReportingStructure generatedReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testManager.getEmployeeId()).getBody();
    	
    	assertReportingStructureEquivalence(generatedReportingStructure, 3);
    	assertEmployeeEquivalence(testManager, generatedReportingStructure.getEmployee());
	}
    
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
    
    private static void assertReportingStructureEquivalence(ReportingStructure reportingStructure, int expectedCount) {
    	assertEquals(expectedCount, reportingStructure.getNumberOfReports());
    }   
}
