package org.genericspatialdao.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PropertiesUtil {

	private static final ResourceBundle RB = ResourceBundle.getBundle("build");

	private static final String DB_1 = "genericspatialdao1";
	private static final String DB_2 = "genericspatialdao2";
	private static final String DB_3 = "genericspatialdao3";
	private static final String DB_4 = "genericspatialdao4";
	private static final String HOST = getProperty("db.host");
	private static final String PORT = getProperty("db.port");
	private static final String USERNAME = getProperty("db.username");
	private static final String PASSWORD = getProperty("db.password");
	private static final String URL_TEMPLATE = "jdbc:postgresql://%s:%s/%s";

	public static String getProperty(String key) {
		return RB.getString(key);
	}

	public static Map<String, String> buildPropertiesMap(int db) {
		String dbName;
		switch (db) {
		case 1:
			dbName = DB_1;
			break;
		case 2:
			dbName = DB_2;
			break;
		case 3:
			dbName = DB_3;
			break;
		case 4:
			dbName = DB_4;
			break;
		default:
			throw new RuntimeException("Database not configured");
		}

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("hibernate.connection.username", USERNAME);
		properties.put("hibernate.connection.password", PASSWORD);
		properties.put("hibernate.connection.url",
				String.format(URL_TEMPLATE, HOST, PORT, dbName));

		return properties;
	}
}
