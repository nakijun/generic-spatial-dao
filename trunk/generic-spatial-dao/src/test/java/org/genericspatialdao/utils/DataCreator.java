package org.genericspatialdao.utils;

import java.util.Date;

import org.genericspatialdao.example.vo.Department;
import org.genericspatialdao.example.vo.Employee;
import org.genericspatialdao.example.vo.EmployeeProjectPK;
import org.genericspatialdao.example.vo.Job;
import org.genericspatialdao.example.vo.Project;


public class DataCreator {

	public static Employee createEmployee() {
		Employee e = new Employee();
		e.setName(TestUtils.randomString());
		e.setBirthdate(new Date());
		return e;
	}

	public static Department createDepartment() {
		Department d = new Department();
		d.setName(TestUtils.randomString());
		return d;
	}

	public static Project createProject() {
		Project p = new Project();
		p.setName(TestUtils.randomString());
		return p;
	}

	public static Job createJob() {
		Job j = new Job();
		j.setHours(TestUtils.randomInt());
		return j;
	}

	public static void bindEmployeeToDepartment(Employee e, Department d) {
		e.setDepartment(d);
		d.getEmployees().add(e);
	}

	public static void bindBossToDepartment(Employee boss, Department d) {
		d.setBoss(boss);
	}

	public static void bindProjectToDepartment(Project p, Department d) {
		p.setDepartment(d);
		d.getProjects().add(p);
	}

	public static void bindEmployeeAndProjectToJob(Employee e, Project p, Job j) {
		EmployeeProjectPK pk = new EmployeeProjectPK();
		pk.setEmployeeId(e.getId());
		pk.setProjectId(p.getId());
		j.setEmployeeProjectPK(pk);
	}
}
