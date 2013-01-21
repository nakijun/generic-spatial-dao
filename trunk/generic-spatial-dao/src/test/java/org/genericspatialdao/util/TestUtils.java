package org.genericspatialdao.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.genericspatialdao.configuration.DaoConfiguration;
import org.genericspatialdao.dao.Dao;
import org.genericspatialdao.dao.impl.GenericSpatialDao;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

public class TestUtils {

	public static enum Database {
		DB_1("genericspatialdao1"), DB_2("genericspatialdao2"), DB_3(
				"genericspatialdao3"), DB_4("genericspatialdao4");

		private String name;

		private Database(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	public static final String HOST = PropertiesUtil.getProperty("db.host");
	public static final String PORT = PropertiesUtil.getProperty("db.port");
	public static final String USERNAME = PropertiesUtil
			.getProperty("db.username");
	public static final String PASSWORD = PropertiesUtil
			.getProperty("db.password");
	private static final String URL_TEMPLATE = "jdbc:postgresql://%s:%s/%s";

	public static <T> Dao<T> getDAOTest(Class<T> entityClass, Database db) {
		String persistenceUnit = db.toString();
		return new GenericSpatialDao<T>(entityClass, new DaoConfiguration(
				persistenceUnit, buildPropertiesMap(db)));
	}

	public static Map<String, String> buildPropertiesMap(Database db) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("hibernate.connection.username", USERNAME);
		properties.put("hibernate.connection.password", PASSWORD);
		properties.put("hibernate.connection.url",
				String.format(URL_TEMPLATE, HOST, PORT, db));
		return properties;
	}

	public static String randomString() {
		return UUID.randomUUID().toString();
	}

	public static int randomInt() {
		return (int) (Math.random() * Integer.MAX_VALUE);
	}

	public static int randomInt(int low, int high) {
		return (int) (Math.min(low, high) + Math.random() * (high - low));
	}

	public static long randomLong() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	public static double randomDouble() {
		return (double) (Math.random() * Double.MAX_VALUE);
	}

	public static double randomDouble(double low, double high) {
		return (double) (Math.min(low, high) + Math.random() * (high - low));
	}

	public static Point randomLatLongPoint(int srid) {
		Coordinate c = SpatialUtils.createCoordinate(randomDouble(-180, 180),
				randomDouble(-90, 90));
		return SpatialUtils.createPoint(c, srid);
	}

	public static Point randomPoint(int xMin, int xMax, int yMin, int yMax,
			int srid) {
		Coordinate c = SpatialUtils.createCoordinate(randomDouble(xMin, xMax),
				randomDouble(yMin, yMax));
		return SpatialUtils.createPoint(c, srid);
	}
}
