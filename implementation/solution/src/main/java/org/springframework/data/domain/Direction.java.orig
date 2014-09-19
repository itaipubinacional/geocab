package org.springframework.data.domain;

import java.util.Locale;

/**
 * Enumeration for sort directions.
 * 
 * @author Oliver Gierke
 */
public enum Direction
{

	ASC, DESC;

	/**
	 * Returns the {@link Direction} enum for the given {@link String} value.
	 * 
	 * @param value
	 * @return
	 */
	public static Direction fromString( String value )
	{
		try
		{
			return Direction.valueOf(value.toUpperCase(Locale.US));
		}
		catch ( Exception e )
		{
			throw new IllegalArgumentException( String.format( "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
		}
	}
}