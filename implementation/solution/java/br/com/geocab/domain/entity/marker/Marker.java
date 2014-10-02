/**
 * 
 */
package br.com.geocab.domain.entity.marker;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.datasource.DataSource;

/**
 * 
 *  {@link DataSource}
 * 
 * @author Cristiano Correa
 * @since 22/05/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject
@Table(schema=IEntity.SCHEMA)
public class Marker extends AbstractEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1806026076674494131L;
	
	private String latitude;
	private String longitude;
	private StatusMarker status;
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public Marker()
	{
		
	}
	
	/**
	 * 
	 *
	 * @param id
	 */
	public Marker( Long id )
	{
		this.setId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param url
	 * @param login
	 * @param password
	 */
	public Marker( Long id, String latitude, String longitude, StatusMarker status )
	{
		this.setId(id);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setStatus(status);
	}
	
	/**
	 * @return the latitude
	 */
	public String getLatitude()
	{
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude()
	{
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}
	/**
	 * @return the status
	 */
	public StatusMarker getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusMarker status)
	{
		this.status = status;
	}

}
