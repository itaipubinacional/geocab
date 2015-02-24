/**
 * 
 */
package br.com.geocab.domain.entity.markermoderation;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerStatus;

/**
 * 
 * Class to define {@link MarkerModeration}
 * 
 * @author Vinicius Ramos Kawamoto 
 * @since 09/01/2015
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject(javascript="MarkerModeration")
@Table(schema=IEntity.SCHEMA)
public class MarkerModeration extends AbstractEntity implements Serializable
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1358373183244765164L;
	
	/**
	 * {@link Marker} of {@link MarkerModeration}
	 */
	@NotNull
	@ManyToOne(fetch=FetchType.EAGER)
	private Marker marker;
	
	/**
	 * status of  {@link MarkerModeration}
	 */
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private MarkerStatus status;
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public MarkerModeration()
	{
		
	}
	/**
	 * 
	 *
	 * @param id
	 */
	public MarkerModeration( Long id )
	{
		this.setId(id);
	}
	/**
	 * 
	 * @param id
	 * @param statusDescription
	 * @param marker
	 */
	public MarkerModeration( Long id, MarkerStatus status, Marker marker)
	{
		this.setId(id);
		this.setStatus(status);
		this.setMarker(marker);
	}
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((marker == null) ? 0 : marker.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		MarkerModeration other = (MarkerModeration) obj;
		if (marker == null)
		{
			if (other.marker != null) return false;
		}
		else if (!marker.equals(other.marker)) return false;
		if (status != other.status) return false;
		return true;
	}
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @return the marker
	 */
	public Marker getMarker()
	{
		return marker;
	}
	/**
	 * @param marker the marker to set
	 */
	public void setMarker(Marker marker)
	{
		this.marker = marker;
	}
	/**
	 * @return the status
	 */
	public MarkerStatus getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(MarkerStatus status)
	{
		this.status = status;
	}
	
}
