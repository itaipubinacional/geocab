/*
 * Copyright 2008-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Sort option for queries. You have to provide at least a list of properties to
 * sort for that must not include {@code null} or empty strings. The direction
 * defaults to {@value Sort#DEFAULT_DIRECTION}.
 * 
 * @author Oliver Gierke
 */
public class Sort implements Iterable<Order>, Serializable
{

	private static final long serialVersionUID	= 5737186511678863905L;

	private List<Order>	orders;

	public Sort()
	{
	}
	
	public Sort( Order... orders )
	{

		this(Arrays.asList(orders));
	}

	/**
	 * Creates a new {@link Sort} instance.
	 * 
	 * @param orders
	 *            must not be {@literal null} or contain {@literal null} or
	 *            empty strings
	 */
	public Sort( List<Order> orders )
	{

		if ( null == orders || orders.isEmpty() )
		{
			throw new IllegalArgumentException(
					"You have to provide at least one sort property to sort by!");
		}

		this.orders = orders;
	}

	/**
	 * Creates a new {@link Sort} instance. Order defaults to
	 * {@value Direction#ASC}.
	 * 
	 * @param properties
	 *            must not be {@literal null} or contain {@literal null} or
	 *            empty strings
	 */
	public Sort( String... properties )
	{

		this(Order.DEFAULT_DIRECTION, properties);
	}

	/**
	 * Creates a new {@link Sort} instance.
	 * 
	 * @param direction
	 *            defaults to {@value Sort#DEFAULT_DIRECTION} (for
	 *            {@literal null} cases, too)
	 * @param properties
	 *            must not be {@literal null} or contain {@literal null} or
	 *            empty strings
	 */
	public Sort( Direction direction, String... properties )
	{

		this(direction, properties == null ? new ArrayList<String>() : Arrays
				.asList(properties));
	}

	/**
	 * Creates a new {@link Sort} instance.
	 * 
	 * @param direction
	 * @param properties
	 */
	public Sort( Direction direction, List<String> properties )
	{

		if ( properties == null || properties.isEmpty() )
		{
			throw new IllegalArgumentException(
					"You have to provide at least one property to sort by!");
		}

		this.orders = new ArrayList<Order>(properties.size());

		for ( String property : properties )
		{
			this.orders.add(new Order(direction, property));
		}
	}

	/**
	 * Returns a new {@link Sort} consisting of the {@link Order}s of the
	 * current {@link Sort} combined with the given ones.
	 * 
	 * @param sort
	 *            can be {@literal null}.
	 * @return
	 */
	public Sort and( Sort sort )
	{

		if ( sort == null )
		{
			return this;
		}

		ArrayList<Order> these = new ArrayList<Order>(this.orders);

		for ( Order order : sort )
		{
			these.add(order);
		}

		return new Sort(these);
	}

	/**
	 * Returns the order registered for the given property.
	 * 
	 * @param property
	 * @return
	 */
	public Order getOrderFor( String property )
	{

		for ( Order order : this )
		{
			if ( order.getProperty().equals(property) )
			{
				return order;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Order> iterator()
	{
		return this.orders.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{

		if ( this == obj )
		{
			return true;
		}

		if ( !(obj instanceof Sort) )
		{
			return false;
		}

		Sort that = (Sort) obj;

		return this.orders.equals(that.orders);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{

		int result = 17;
		result = 31 * result + orders.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return StringUtils.collectionToCommaDelimitedString(orders);
	}

	/**
	 *
	 * @return the orders
	 */
	public List<Order> getOrders()
	{
		return orders;
	}

	/**
	 *
	 * @param orders the orders to set
	 */
	public void setOrders( List<Order> orders )
	{
		this.orders = orders;
	}
}
