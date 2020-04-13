package com.mindex.challenge.transport;

import com.mindex.challenge.data.Employee;

public class ReportingStructure {
	private Employee employee;
	private int numberOfReports;
	
	public ReportingStructure() {
		super();
	}
	
	public ReportingStructure(Employee employee, int numberOfReports) {
		super();
		this.employee = employee;
		this.numberOfReports = numberOfReports;
	}

	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public int getNumberOfReports() {
		return numberOfReports;
	}
	public void setNumberOfReports(int numberOfReports) {
		this.numberOfReports = numberOfReports;
	}
	
	@Override
	public String toString() {
		return "ReportingStructure [employee=" + employee.toString() + ", numberOfReports=" + numberOfReports + "]";
	}
}
