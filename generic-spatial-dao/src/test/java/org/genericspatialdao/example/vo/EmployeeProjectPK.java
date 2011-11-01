package org.genericspatialdao.example.vo;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class EmployeeProjectPK implements Serializable {

	private static final long serialVersionUID = 4801127906781795087L;

	private long employeeId;
	private long projectId;

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

}
