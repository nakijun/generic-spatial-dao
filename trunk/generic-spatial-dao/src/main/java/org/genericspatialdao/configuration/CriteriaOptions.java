package org.genericspatialdao.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.criterion.Order;

public class CriteriaOptions {

	private Order order;
	private Integer firstResult;
	private Integer maxResults;

	public CriteriaOptions(Order order) {
		this(order, null, null);
	}

	public CriteriaOptions(Order order, Integer firstResult, Integer maxResults) {
		this.order = order;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(order).append(firstResult)
				.append(maxResults).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CriteriaOptions)) {
			return false;
		}
		CriteriaOptions other = (CriteriaOptions) obj;
		return new EqualsBuilder().append(order, other.getOrder())

		.append(firstResult, other.getFirstResult())
				.append(maxResults, other.getMaxResults()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("order", order).append("firstResult", firstResult)
				.append("maxResults", maxResults).toString();
	}
}