package org.springframework.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
* Copyright 2008-2010 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


/**
 * PropertyPath implements the pairing of an {@code Order} and a property. It is
 * used to provide input for {@link Sort}
 * 
 * @author Oliver Gierke
 */
public class Order implements Serializable
{

	private static final long	serialVersionUID	= 1522511010900108987L;
	
	public static final Direction	DEFAULT_DIRECTION	= Direction.ASC;

	private Direction			direction;
	private String				property;

	public Order()
	{
	}
	
	/**
	 * Creates a new {@link Order} instance. if order is {@literal null} then
	 * order defaults to {@value Sort#DEFAULT_DIRECTION}
	 * 
	 * @param direction
	 *            can be {@code null}
	 * @param property
	 *            must not be {@code null} or empty
	 */
	public Order( Direction direction, String property )
	{

		if ( property == null || "".equals(property.trim()) )
		{
			throw new IllegalArgumentException(
					"PropertyPath must not null or empty!");
		}

		this.direction = direction == null ? DEFAULT_DIRECTION : direction;
		this.property = property;
	}

	/**
	 * Creates a new {@link Order} instance. Takes a single property. Order
	 * defaults to {@value Sort.DEFAULT_ORDER}
	 * 
	 * @param property
	 *            - must not be {@code null} or empty
	 */
	public Order( String property )
	{
		this(DEFAULT_DIRECTION, property);
	}

	public static List<Order> create( Direction direction, Iterable<String> properties )
	{
		List<Order> orders = new ArrayList<Order>();
		for ( String property : properties )
		{
			orders.add(new Order(direction, property));
		}
		return orders;
	}

	/**
	 * Returns the order the property shall be sorted for.
	 * 
	 * @return
	 */
	public Direction getDirection()
	{
		return direction;
	}

	/**
	 * Returns the property to order for.
	 * 
	 * @return
	 */
	public String getProperty()
	{
		return property;
	}

	/**
	 * Returns whether sorting for this property shall be ascending.
	 * 
	 * @return
	 */
	public boolean isAscending()
	{
		return this.direction.equals(Direction.ASC);
	}

	/**
	 * Returns a new {@link Order} with the given {@link Order}.
	 * 
	 * @param order
	 * @return
	 */
	public Order with( Direction order )
	{
		return new Order(order, this.property);
	}

	/**
	 * Returns a new {@link Sort} instance for the given properties.
	 * 
	 * @param properties
	 * @return
	 */
	public Sort withProperties( String... properties )
	{
		return new Sort(this.direction, properties);
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

		result = 31 * result + direction.hashCode();
		result = 31 * result + property.hashCode();

		return result;
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

		if ( !(obj instanceof Order) )
		{
			return false;
		}

		Order that = (Order) obj;

		return this.direction.equals(that.direction)
				&& this.property.equals(that.property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("%s: %s", property, direction);
	}

	public void setDirection( Direction direction )
	{
		this.direction = direction;
	}

	public void setProperty( String property )
	{
		this.property = property;
	}
}