package org.genericspatialdao.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * 
 * @author joaosavio
 */
public class PropertiesUtils {

	private static final String APPLICATION_PROPERTIES = "genericspatialdao.properties";
	private static final Logger LOG = Logger.getLogger(PropertiesUtils.class);
	private static Configuration configApp;

	static {
		try {
			configApp = new PropertiesConfiguration(APPLICATION_PROPERTIES);
		} catch (ConfigurationException e) {
			LOG.warn("Failed to load " + APPLICATION_PROPERTIES
					+ ConstantsUtils.DOT_SPACE + "Error: " + e.getMessage());
			configApp = null;
		}
	}

	public static String getString(String key) {
		if (configApp == null) {
			return null;
		}
		return configApp.getString(key);
	}
}
