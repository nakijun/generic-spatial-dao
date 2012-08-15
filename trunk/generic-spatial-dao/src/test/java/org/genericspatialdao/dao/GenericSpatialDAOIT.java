package org.genericspatialdao.dao;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.genericspatialdao.data.TestVO;
import org.genericspatialdao.utils.TestUtils;
import org.junit.Test;

public class GenericSpatialDAOIT {

	public static final int TIMES = 100;
	public static final int THREADS = 10;
	private ExecutorService executor = Executors
			.newFixedThreadPool(THREADS * 4);

	@Test
	public void persistUpdateRemoveMultiThreadSamePUTest()
			throws InterruptedException {
		System.out.println("persistUpdateRemoveMultiThreadTest");
		for (int i = 0; i < THREADS; i++) {
			executor.execute(new MultiThreadDefaultPUTest());
		}
		executor.awaitTermination(5L, TimeUnit.SECONDS);
		executor.shutdown();
	}

	@Test
	public void persistUpdateRemoveMultiThreadDifferentPUTest()
			throws InterruptedException {
		System.out.println("persistUpdateRemoveMultiThreadDifferentPUTest");
		for (int i = 0; i < THREADS; i++) {
			executor.execute(new MultiThreadDefaultPUTest());
			executor.execute(new MultiThreadPU2Test());
			executor.execute(new MultiThreadPU3Test());
			executor.execute(new MultiThreadPU4Test());
		}
		executor.awaitTermination(8L, TimeUnit.SECONDS);
		executor.shutdown();
	}

}

abstract class MultiThreadTest implements Runnable {
	@Override
	public void run() {
		DAO<TestVO> testDAO = null;
		for (int i = 0; i < GenericSpatialDAOIT.TIMES; i++) {
			testDAO = getDAO();
			TestVO testVO = new TestVO();
			testVO.setLogin(TestUtils.randomString());
			testVO.setPassword(TestUtils.randomString());

			testDAO.persist(testVO);

			assertEquals(testVO, testDAO.find(testVO.getId()));

			String newPassword = TestUtils.randomString();
			testVO.setPassword(newPassword);
			testDAO.merge(testVO);

			TestVO updatedTestVO = testDAO.find(testVO.getId());
			assertEquals(newPassword, updatedTestVO.getPassword());

			testDAO.remove(updatedTestVO);
			assertEquals(null, testDAO.find(testVO.getId()));
		}
		testDAO.close();
	}

	abstract DAO<TestVO> getDAO();
}

class MultiThreadDefaultPUTest extends MultiThreadTest {

	@Override
	DAO<TestVO> getDAO() {
		return DAOFactory.getDAO(TestVO.class);
	}
}

class MultiThreadPU2Test extends MultiThreadTest {

	@Override
	DAO<TestVO> getDAO() {
		return DAOFactory.getDAO(TestVO.class, "genericspatialdao2");
	}
}

class MultiThreadPU3Test extends MultiThreadTest {

	@Override
	DAO<TestVO> getDAO() {
		return DAOFactory.getDAO(TestVO.class, "genericspatialdao3");
	}
}

class MultiThreadPU4Test extends MultiThreadTest {

	@Override
	DAO<TestVO> getDAO() {
		return DAOFactory.getDAO(TestVO.class, "genericspatialdao4");
	}
}

class MultiThreadDifferentPUTest implements Runnable {

	@Override
	public void run() {
		DAO<TestVO> testDAO1 = DAOFactory.getDAO(TestVO.class);
		DAO<TestVO> testDAO2 = DAOFactory.getDAO(TestVO.class,
				"genericspatialdao2");
		doTest(testDAO1);
		doTest(testDAO2);

		testDAO1.close();
		testDAO2.close();
	}

	public void doTest(DAO<TestVO> testDAO) {
		for (int i = 0; i < GenericSpatialDAOIT.TIMES; i++) {
			TestVO testVO = new TestVO();
			testVO.setLogin(TestUtils.randomString());
			testVO.setPassword(TestUtils.randomString());

			testDAO.persist(testVO);

			assertEquals(testVO, testDAO.find(testVO.getId()));

			String newPassword = TestUtils.randomString();
			testVO.setPassword(newPassword);
			testDAO.merge(testVO);

			TestVO updatedTestVO = testDAO.find(testVO.getId());
			assertEquals(newPassword, updatedTestVO.getPassword());

			testDAO.remove(updatedTestVO);
			assertEquals(null, testDAO.find(testVO.getId()));
		}
	}
}
