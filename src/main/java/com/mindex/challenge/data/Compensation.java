package com.mindex.challenge.data;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Compensation {

	private String compensationId;
	private String employeeId;
	private Double salary;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate effectiveDate;
	
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
	
	@Override
	public String toString() {
		return "Compensation [compensationId=" + compensationId + ", employeeId=" + employeeId + ", salary=" + salary
				+ ", effectiveDate=" + effectiveDate + "]";
	}
}
