/*
 * SortUtil.java  1.0  12/12/2009
 * 
 * CopyRight
 */
package org.util.comparator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SortUtil allows a user to sort a collection (or array, iterator, etc) on any
 * arbitrary set of properties exposed by the objects contained within the
 * collection.
 * 
 * <p>
 * The sort tool can handle all of the collection types supported by #foreach
 * and the same constraints apply as well as the following. Every object in the
 * collection must support the set of properties selected to sort on. Each
 * property which is to be sorted on must return one of the follow:
 * <ul>
 * <li>Primitive type: e.g. int, char, long etc</li>
 * <li>Standard Object: e.g. String, Integer, Long etc</li>
 * <li>Object which implements the Comparable interface.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * During the sort operation all properties are compared by calling compareTo()
 * with the exception of Strings for which compareToIgnoreCase() is called.
 * </p>
 * 
 * <p>
 * The sort is performed by calling Collections.sort() after marshaling the
 * collection to sort into an appropriate collection type. The original
 * collection will not be re-ordered; a new list containing the sorted elements
 * will always be returned.
 * </p>
 * 
 * @author Haji Uduman
 *
 */
public class SortUtil {
	
	/**
	 * Sort array of objects based on order by list
	 * @param array
	 * @param orderByList
	 * @return
	 */
	public static Object[] sort(Object[] array, List<OrderBy> orderByList) {
		
		return sort(Arrays.asList(array), orderByList).toArray();
	}
	
	/**
	 * Sort given map based on order by list
	 * @param map
	 * @param orderByList
	 * @return
	 */
	public static Map<?, ?> sort(Map<?, ?> map, List<OrderBy> orderByList) {
		
		Map<Object, Object> sortedMap = new TreeMap<Object, Object>(new InternalComparator<Object>(orderByList));
		sortedMap.putAll(map);
		return sortedMap;
	}
	
	/**
	 * Sort given collection based on property
	 * @param collection
	 * @param propertyName
	 * @return
	 */
	public static Collection<?> sort(Collection<?> collection, String propertyName) {		
		
		return sort(collection, propertyName, OrderBy.ASC);
	}
	
	/**
	 * Sort given collection based on property and order
	 * @param collection
	 * @param propertyName
	 * @param order
	 * @return
	 */
	public static Collection<?> sort(Collection<?> collection, String propertyName, int order) {		
		
		return sort(collection, new OrderBy(propertyName, order));
	}
	
	/**
	 * Sort given collection based on given order by
	 * @param collection
	 * @param orderBy
	 * @return
	 */
	public static Collection<?> sort(Collection<?> collection, OrderBy orderBy) {		
		
		List<OrderBy> orderByList = new ArrayList<OrderBy>(0);
		orderByList.add(orderBy);
		return sort(collection, orderByList);
	}
	
	/**
	 * Sort given collection based on order by list
	 * @param collection
	 * @param orderByList
	 * @return
	 */
	public static Collection<?> sort(Collection<?> collection, List<OrderBy> orderByList) {
		
		List<Object> list = new ArrayList<Object>(collection.size());
		list.addAll(collection);
		return sort(list, orderByList);
	}
	
	/**
	 * 
	 * @param list
	 * @param orderByList
	 * @return
	 */
	public static Collection<?> sort(List<?> list, List<OrderBy> orderByList) {		
		
		Collections.sort(list, new InternalComparator<Object>(orderByList));
		return list;
	}
	
	static class InternalComparator<T> implements Comparator<T> {

		/**
		 * orderBy list
		 */
		private List<OrderBy> orderByList = new ArrayList<OrderBy>(0);
		
		/**
		 * Constructor with order by list.
		 * @param orderByList
		 */
		public InternalComparator(List<OrderBy> orderByList) {		
			this.orderByList = orderByList;
		}

		/**
	     * @see Comparator#compare(Object, Object);
	     */
		@Override
		public int compare(Object obj1, Object obj2) {
			
			int cValue = 0;
			for (int i = 0; i < orderByList.size(); i++) {
				
				OrderBy orderBy = orderByList.get(i);
				int compare = compare(obj1, obj2, orderBy);
				if (compare == 0 && ((i + 1) < orderByList.size())) {
					continue;
				}
				cValue = compare;
				break;				
			}
			return cValue;
		}

		/**
		 * This will compare objects based on property name and order
		 * 
		 * @param lhs
		 * @param rhs
		 * @param orderBy
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private int compare(Object lhs, Object rhs, OrderBy orderBy) {

			int cValue = 0;
			try {
				int order = orderBy.getOrder();
				Comparable left = (Comparable<?>) getProperty(lhs, orderBy.getPropertyName());
				Comparable right = (Comparable<?>) getProperty(rhs, orderBy.getPropertyName());

				if (left == null && right == null) {
					cValue = 0;
				} else if (left == null) {
					cValue = order * OrderBy.DESC;
				} else if (right == null) {
					cValue = order;
				} else if(left instanceof String) {
					cValue = order * ((String)left).compareToIgnoreCase((String) right);
				} else if(left instanceof Date) {					
					Long leftValue = ((Date) left).getTime();
					Long rigthValue = ((Date) right).getTime();
					cValue = order * (leftValue.compareTo(rigthValue));
				} else {
					cValue = order * left.compareTo(right);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cValue;
		}
	}

	private static Comparable<?> getProperty(Object obj, String propertyName) throws IllegalArgumentException, IntrospectionException, IllegalAccessException, InvocationTargetException {
		Comparable<?> value = null;
		for(PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors()) {
			if(pd.getName().equals(propertyName)) {
				value = (Comparable<?>) pd.getReadMethod().invoke(obj, new Object[] {});
			}
		}
		return value;
	}
}