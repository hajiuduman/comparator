/*
 * OrderBy.java  1.0  12/12/2009
 * 
 * CopyRight
 */
package org.util.comparator;

/**
 * 
 * @author Haji Uduman
 * @version 1.0 12/12/2009
 * 
 */
public class OrderBy {

	/**
	 * Constant for ascending order.
	 */
	public static final int ASC = 1;
	
	/**
	 * Constant for descending order.
	 */
	public static final int DESC = -1;

	/**
	 * name of the property to be sorted.
	 */
	private String propertyName;
	
	/**
	 * sorting order.
	 */
	private int order = OrderBy.ASC;
	
	/**
	 * Constructor with property name.
	 * @param propertyName
	 */
	public OrderBy(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Constructor with property name & order.
	 * @param propertyName
	 * @param order
	 */
	public OrderBy(String propertyName, int order) {

		if (order == OrderBy.DESC) {
			this.order = OrderBy.DESC;
		}
		this.propertyName = propertyName;
	}

	/**
	 * @param order
	 */
	public void setOrder(int order) {
		if (order == OrderBy.DESC) {
			this.order = OrderBy.DESC;
		}
	}

	/**
	 * @return order
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * @return
	 */
	public String getPropertyName() {
		return propertyName;
	}

	public static int getOrderBy(String order) {
		return "asc".equals(order) ? ASC : DESC;
	}
}