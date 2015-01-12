/**
 * 
 */
package br.com.geocab.domain.entity.markermoderation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.marker.Marker;

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
@DataTransferObject
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
	 * Status Description of {@link MarkerModeration}
	 */
	@NotEmpty
	@Column(nullable=false, length=144, unique=true)
	private String statusDescription;
	/**
	 * {@link Marker} of {@link MarkerModeration}
	 */
	@NotNull
	@ManyToOne(fetch=FetchType.EAGER)
	private Marker marker;
	
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
	public MarkerModeration( Long id, String statusDescription, Marker marker)
	{
		this.setId(id);
		this.setStatusDescription(statusDescription);
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
		result = prime
				* result
				+ ((statusDescription == null) ? 0 : statusDescription
						.hashCode());
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
		if (statusDescription == null)
		{
			if (other.statusDescription != null) return false;
		}
		else if (!statusDescription.equals(other.statusDescription)) return false;
		return true;
	}
	
	
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription()
	{
		return statusDescription;
	}
	
	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription)
	{
		this.statusDescription = statusDescription;
	}
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
	
	
}
