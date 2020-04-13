package com.mindex.challenge.transport;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.mindex.challenge.data.Employee;

public class CompWithEmployee {
	private String compensationId;
	private String employeeId;
	private Double salary;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate effectiveDate;
	private Employee employee;
	public String getCompensationId() {
		return compensationId;
	}
	public void setCompensationId(String compensationId) {
		this.compensationId = compensationId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	@Override
	public String toString() {
		return "CompWithEmployee [compensationId=" + compensationId + ", employeeId=" + employeeId + ", salary="
				+ salary + ", effectiveDate=" + effectiveDate + ", employee=" + employee + "]";
	}	
}
