package org.genericspatialdao.configuration;

import java.util.HashMap;
import java.util.Map;

public class DAOConfiguration {

	private String persistenceUnit;
	private Map<String, String> properties;
	private boolean autoTransaction = true;

	public DAOConfiguration(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}

	public DAOConfiguration(String persistenceUnit,
			Map<String, String> properties) {
		this.persistenceUnit = persistenceUnit;
		this.properties = properties;
	}

	public DAOConfiguration(String persistenceUnit,
			Map<String, String> properties, boolean autoTransaction) {
		this.persistenceUnit = persistenceUnit;
		this.properties = properties;
		this.autoTransaction = autoTransaction;
	}

	public void addProperty(String key, String value) {
		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		properties.put(key, value);
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	public void setPersistenceUnit(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public boolean isAutoTransaction() {
		return autoTransaction;
	}

	public void setAutoTransaction(boolean autoTransaction) {
		this.autoTransaction = autoTransaction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (autoTransaction ? 1231 : 1237);
		result = prime * result
				+ ((persistenceUnit == null) ? 0 : persistenceUnit.hashCode());
		result = prime * result
				+ ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DAOConfiguration other = (DAOConfiguration) obj;
		if (autoTransaction != other.autoTransaction)
			return false;
		if (persistenceUnit == null) {
			if (other.persistenceUnit != null)
				return false;
		} else if (!persistenceUnit.equals(other.persistenceUnit))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DAOConfiguration [persistenceUnit=");
		builder.append(persistenceUnit);
		builder.append(", properties=");
		builder.append(properties);
		builder.append(", autoTransaction=");
		builder.append(autoTransaction);
		builder.append("]");
		return builder.toString();
	}
}