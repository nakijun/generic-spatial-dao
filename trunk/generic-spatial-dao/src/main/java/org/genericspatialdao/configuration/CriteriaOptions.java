package org.genericspatialdao.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.criterion.Order;
import org.hibernate.transform.ResultTransformer;

public class CriteriaOptions {

	private ResultTransformer resultTransformer;
	private Order order;
	private Integer firstResult;
	private Integer maxResults;

	public CriteriaOptions(ResultTransformer resultTransformer) {
		this(resultTransformer, null, null, null);
	}

	public CriteriaOptions(Order order) {
		this(null, order, null, null);
	}

	public CriteriaOptions(Integer firstResult, Integer maxResults) {
		this(null, null, firstResult, maxResults);
	}

	public CriteriaOptions(ResultTransformer resultTransformer, Order order,
			Integer firstResult, Integer maxResults) {
		this.resultTransformer = resultTransformer;
		this.order = order;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public ResultTransformer getResultTransformer() {
		return resultTransformer;
	}

	public void setResultTransformer(ResultTransformer resultTransformer) {
		this.resultTransformer = resultTransformer;
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
		return new HashCodeBuilder().append(resultTransformer).append(order)
				.append(firstResult).append(maxResults).toHashCode();
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
		return new EqualsBuilder()
				.append(resultTransformer, other.getResultTransformer())
				.append(order, other.getOrder())
				.append(firstResult, other.getFirstResult())
				.append(maxResults, other.getMaxResults()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("resultTransformer", resultTransformer)
				.append("order", order).append("firstResult", firstResult)
				.append("maxResults", maxResults).toString();
	}
}