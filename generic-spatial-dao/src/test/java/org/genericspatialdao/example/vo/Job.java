package org.genericspatialdao.example.vo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Job {

	@EmbeddedId
	private EmployeeProjectPK employeeProjectPK;

	private int hours;

	public EmployeeProjectPK getEmployeeProjectPK() {
		return employeeProjectPK;
	}

	public void setEmployeeProjectPK(EmployeeProjectPK employeeProjectPK) {
		this.employeeProjectPK = employeeProjectPK;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

}
