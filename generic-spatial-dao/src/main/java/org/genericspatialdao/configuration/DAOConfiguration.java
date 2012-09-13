package org.genericspatialdao.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DAOConfiguration implements Serializable {

	private static final long serialVersionUID = 689563783546032476L;

	private String persistenceUnit;
	private Map<String, String> properties;
	private boolean autoTransaction;

	public DAOConfiguration(String persistenceUnit) {
		this(persistenceUnit, null);
	}

	public DAOConfiguration(String persistenceUnit,
			Map<String, String> properties) {
		this(persistenceUnit, properties, true);
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

	public String getProperty(String key) {
		if (properties == null) {
			return null;
		}
		return properties.get(key);
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
		return new HashCodeBuilder().append(persistenceUnit).append(properties)
				.append(autoTransaction).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DAOConfiguration)) {
			return false;
		}
		DAOConfiguration other = (DAOConfiguration) obj;
		return new EqualsBuilder()
				.append(persistenceUnit, other.getPersistenceUnit())
				.append(properties, other.getProperties()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("persistenceUnit", persistenceUnit)
				.append("properties", properties)
				.append("autoTransaction", autoTransaction).toString();
	}
}