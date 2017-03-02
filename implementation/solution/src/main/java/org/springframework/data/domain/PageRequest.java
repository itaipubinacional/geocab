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

import org.springframework.data.domain.Sort.Direction;

/**
 * Basic Java Bean implementation of {@code Pageable}.
 * 
 * @author Oliver Gierke
 */
public class PageRequest implements Pageable, Serializable
{

	private static final long	serialVersionUID	= 8280485938848398236L;

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	private int	page;
	/**
	 * 
	 */
	private int	size;
	/**
	 * 
	 */
	private Sort sort;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public PageRequest()
	{
	}

	/**
	 * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing
	 * 0 for {@code page} will return the first page.
	 * 
	 * @param size
	 * @param page
	 */
	public PageRequest( int page, int size )
	{
		this(page, size, null);
	}

	/**
	 * Creates a new {@link PageRequest} with sort parameters applied.
	 * 
	 * @param page
	 * @param size
	 * @param direction
	 * @param properties
	 */
	public PageRequest( int page, int size, Direction direction, String... properties )
	{

		this(page, size, new Sort(direction, properties));
	}

	/**
	 * Creates a new {@link PageRequest} with sort parameters applied.
	 * 
	 * @param page
	 * @param size
	 * @param sort
	 */
	public PageRequest( int page, int size, Sort sort )
	{
		if ( 0 > page )
		{
			throw new IllegalArgumentException(
					"Page index must not be less than zero!");
		}

		if ( 0 >= size )
		{
			throw new IllegalArgumentException(
					"Page size must not be less than or equal to zero!");
		}

		this.page = page;
		this.size = size;
		this.sort = sort;
	}

	/*-------------------------------------------------------------------
	 * 		 					 BEHAVIORS
	 *-------------------------------------------------------------------*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( final Object obj )
	{
		if ( this == obj )
		{
			return true;
		}

		if ( !(obj instanceof PageRequest) )
		{
			return false;
		}

		PageRequest that = (PageRequest) obj;

		boolean pageEqual = this.page == that.page;
		boolean sizeEqual = this.size == that.size;

		boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);

		return pageEqual && sizeEqual && sortEqual;
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

		result = 31 * result + page;
		result = 31 * result + size;
		result = 31 * result + (null == sort ? 0 : sort.hashCode());

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
		return String.format("Page request [number: %d, size %d, sort: %s]", page, size, sort == null ? null : sort.toString());
	}

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getPageSize()
	 */
	@Override
	public int getPageSize()
	{

		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getPageNumber()
	 */
	@Override
	public int getPageNumber()
	{
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getOffset()
	 */
	@Override
	public int getOffset()
	{
		return page * size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getSort()
	 */
	@Override
	public Sort getSort()
	{
		return sort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#hasPrevious()
	 */
	@Override
	public boolean hasPrevious()
	{
		return page > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#next()
	 */
	@Override
	public Pageable next()
	{
		return new PageRequest(page + 1, size, sort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#previousOrFirst()
	 */
	@Override
	public Pageable previousOrFirst()
	{
		return hasPrevious() ? new PageRequest(page - 1, size, sort) : this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#first()
	 */
	@Override
	public Pageable first()
	{
		return new PageRequest(0, size, sort);
	}
	
	//--------------------
	/**
	 *
	 * @return the page
	 */
	public int getPage()
	{
		return page;
	}

	/**
	 *
	 * @param page the page to set
	 */
	public void setPage( int page )
	{
		this.page = page;
	}

	/**
	 *
	 * @return the size
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 *
	 * @param size the size to set
	 */
	public void setSize( int size )
	{
		this.size = size;
	}

	/**
	 *
	 * @param sort the sort to set
	 */
	public void setSort( Sort sort )
	{
		this.sort = sort;
	}
}