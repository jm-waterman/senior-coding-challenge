package com.mindex.challenge.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.transport.CompWithEmployee;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CompensationServiceImpl implements CompensationService {

	private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
	
	@Autowired
	private CompensationRepository compensationRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Compensation create(Compensation compensation) {
		LOG.debug("Creating compensation [{}]", compensation);

		compensation.setCompensationId(UUID.randomUUID().toString());
		compensationRepository.insert(compensation);

		return compensation;
	}
	
	@Override
	public Compensation update(Compensation compensation) {
		LOG.debug("Updating compensation [{}]", compensation);
		
		return compensationRepository.save(compensation);
	}

	@Override
	public Compensation createFromCWE(CompWithEmployee compWithEmployee) {
		if (compWithEmployee == null) {
			throw new RuntimeException(
					"Attempted to create Compensation from CompWithEmployee, but CompWithEmployee was null.");
		}
		
		Compensation compensation = mapFromCompWithEmployee(compWithEmployee);
		
		//Assumption is that each employee can have only one comp, so if
		// the employee already has one, update it instead of creating it
		Compensation existingComp = fetchByEmployeeId(compensation.getEmployeeId());
		if (existingComp == null) {
			return create(compensation);
		}
		else {
			compensation.setCompensationId(existingComp.getCompensationId());
			return update(compensation);
		}
	}

	@Override
	public Compensation read(String compensationId) {
		LOG.debug("Reading compensation with id [{}]", compensationId);

		Compensation compensation = compensationRepository.findByCompensationId(compensationId);

		if (compensation == null) {
			throw new RuntimeException("Invalid compensationId: " + compensationId);
		}

		return compensation;
	}

	@Override
	public Compensation fetchByEmployeeId(String employeeId) {
		LOG.debug("Reading compensation with employee id [{}]", employeeId);

		Compensation compensation = compensationRepository.findByEmployeeId(employeeId);

		if (compensation == null) {
			LOG.info("No compensation found for employee with id [{}]", employeeId);
		}

		return compensation;
	}

	@Override
	public CompWithEmployee fetchCWEByEmployeeId(String employeeId) {
		Compensation compensation = compensationRepository.findByEmployeeId(employeeId);
		if (compensation == null) {
			throw new RuntimeException("No compensation found for employeeId " + employeeId);
		}

		return mapToCompWithEmployee(compensation);
	}

	private CompWithEmployee mapToCompWithEmployee(Compensation compensation) {
		CompWithEmployee compWithEmployee = new CompWithEmployee();

		Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployeeId());
		if (employee == null) {
			throw new RuntimeException("No employee found for employeeId " + compensation.getEmployeeId());
		}

		compWithEmployee.setEffectiveDate(compensation.getEffectiveDate());
		compWithEmployee.setCompensationId(compensation.getCompensationId());
		compWithEmployee.setSalary(compensation.getSalary());
		compWithEmployee.setEmployeeId(compensation.getEmployeeId());
		compWithEmployee.setEmployee(employee);

		return compWithEmployee;
	}

	private Compensation mapFromCompWithEmployee(CompWithEmployee compWithEmployee) {
		Compensation compensation = new Compensation();

		compensation.setEffectiveDate(compWithEmployee.getEffectiveDate());
		compensation.setCompensationId(compWithEmployee.getCompensationId());
		compensation.setEmployeeId(compWithEmployee.getEmployee().getEmployeeId());
		compensation.setSalary(compWithEmployee.getSalary());
		
		return compensation;
	}
}
