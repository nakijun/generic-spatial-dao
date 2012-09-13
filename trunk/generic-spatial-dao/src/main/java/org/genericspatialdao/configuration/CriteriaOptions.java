package org.genericspatialdao.configuration;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.criterion.Order;
import org.hibernate.transform.ResultTransformer;

public class CriteriaOptions implements Serializable {

	private static final long serialVersionUID = 1050408961262485169L;

	private ResultTransformer resultTransformer;
	private Order[] orders;
	private Integer firstResult;
	private Integer maxResults;

	public CriteriaOptions(ResultTransformer resultTransformer) {
		this(null, null, resultTransformer, (Order[]) null);
	}

	public CriteriaOptions(Order... orders) {
		this(null, null, null, orders);
	}

	public CriteriaOptions(Integer firstResult, Integer maxResults) {
		this(firstResult, maxResults, null, (Order[]) null);
	}

	public CriteriaOptions(Integer firstResult, Integer maxResults,
			ResultTransformer resultTransformer, Order... orders) {
		this.firstResult = firstResult;
		this.maxResults = maxResults;
		this.resultTransformer = resultTransformer;
		if (orders != null) {
			this.orders = orders.clone();
		}
	}

	public ResultTransformer getResultTransformer() {
		return resultTransformer;
	}

	public void setResultTransformer(ResultTransformer resultTransformer) {
		this.resultTransformer = resultTransformer;
	}

	public Order[] getOrders() {
		if (orders == null) {
			return null;
		}
		return orders.clone();
	}

	public void setOrders(Order[] orders) {
		if (orders != null) {
			this.orders = orders.clone();
		}
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
		return new HashCodeBuilder().append(resultTransformer).append(orders)
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
				.append(orders, other.getOrders())
				.append(firstResult, other.getFirstResult())
				.append(maxResults, other.getMaxResults()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("resultTransformer", resultTransformer)
				.append("orders", Arrays.toString(orders))
				.append("firstResult", firstResult)
				.append("maxResults", maxResults).toString();
	}
}