package org.genericspatialdao.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.genericspatialdao.dao.Dao;
import org.genericspatialdao.dao.impl.DaoFactory;
import org.genericspatialdao.example.vo.Department;
import org.genericspatialdao.example.vo.Employee;
import org.genericspatialdao.util.DataCreator;
import org.genericspatialdao.util.TestUtils;
import org.genericspatialdao.util.TestUtils.DB;
import org.junit.Test;

public class ExampleTest {

	@Test
	public void oneToManyTest() {
		// setup
		Dao<Department> testD = TestUtils.getDAOTest(Department.class, DB.DB_1);

		Dao<Employee> testE = TestUtils.getDAOTest(Employee.class, DB.DB_1);

		Department d = DataCreator.createDepartment();
		testD.persist(d);
		assertEquals(1, testD.findAll().size());

		Employee e1 = DataCreator.createEmployee();
		Employee e2 = DataCreator.createEmployee();
		testE.persist(e1, e2);
		assertEquals(2, testE.findAll().size());

		// action
		d.getEmployees().add(e1);
		d.getEmployees().add(e2);
		e1.setDepartment(d);
		e2.setDepartment(d);
		testD.merge(d);
		// em.merge(employee1);
		// em.merge(employee2);
		// em.flush();

		// em.refresh(department);
		// department = em.find(Department.class, d.getId());
		assertTrue(d.getEmployees().contains(e1));
		assertTrue(d.getEmployees().contains(e2));
		assertEquals(d, e1.getDepartment());
		assertEquals(d, e2.getDepartment());

		testE.remove(e2, e1);
		testD.remove(d);

		DaoFactory.close(testE, testD);
	}
}
