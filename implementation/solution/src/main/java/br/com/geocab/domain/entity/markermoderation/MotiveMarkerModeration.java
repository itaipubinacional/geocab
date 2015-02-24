/**
 * 
 */
package br.com.geocab.domain.entity.markermoderation;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 09/01/2015
 * @version 1.0
 */

@Entity
@Audited
@DataTransferObject(javascript="MotiveMarkerModeration")
@Table(schema=IEntity.SCHEMA)
public class MotiveMarkerModeration extends AbstractEntity implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7579901947534822117L;
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	@NotNull
	private String description;
	
	/**
	 * 
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="markerModeration_id")
	@JsonBackReference
	private MarkerModeration markerModeration;
	
	/**
	 * 
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="motive_id")
	private Motive motive;
	
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	public MotiveMarkerModeration()
	{
		
	}
	
	/**
	 * 
	 * @param id
	 */
	public MotiveMarkerModeration(Long id)
	{
		super(id);
	}
	
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @param value
	 * @param markerModeration
	 * @param motive
	 */
	public MotiveMarkerModeration(Long id, String description, MarkerModeration markerModeration, Motive motive)
	{
		super(id);
		this.description = description;
		this.markerModeration = markerModeration;
		this.motive = motive;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the markerModeration
	 */
	public MarkerModeration getMarkerModeration()
	{
		return markerModeration;
	}

	/**
	 * @param markerModeration the markerModeration to set
	 */
	public void setMarkerModeration(MarkerModeration markerModeration)
	{
		this.markerModeration = markerModeration;
	}

	/**
	 * @return the motive
	 */
	public Motive getMotive()
	{
		return motive;
	}

	/**
	 * @param motive the motive to set
	 */
	public void setMotive(Motive motive)
	{
		this.motive = motive;
	}

	
	
}
