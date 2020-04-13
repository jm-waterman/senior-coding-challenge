package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.transport.CompWithEmployee;

public interface CompensationService {
	Compensation create(Compensation compensation);
	Compensation read(String compensationId);
	Compensation update(Compensation compensation);
	Compensation fetchByEmployeeId(String employeeId);
	Compensation createFromCWE(CompWithEmployee compWithEmployee);
	CompWithEmployee fetchCWEByEmployeeId(String employeeId);
}
