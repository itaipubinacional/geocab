/**
 * 
 */
package br.com.geocab.domain.entity.marker;

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
import br.com.geocab.domain.entity.layer.Attribute;

import com.fasterxml.jackson.annotation.JsonBackReference;
/**
 * @author Thiago Rossetto Afonso
 * @since 02/10/2014
 * @version 1.0
 */
@Entity
@Audited
@DataTransferObject(javascript="MarkerAttribute")
@Table(schema=IEntity.SCHEMA)
public class MarkerAttribute extends AbstractEntity implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7579901947534822117L;
	
	@NotNull
	private String value;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="marker_id")
	@JsonBackReference
	private Marker marker;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="attribute_id")
	private Attribute attribute;
	
	public MarkerAttribute(){
		
	}
	
	public MarkerAttribute(Long id){
		super(id);
	}
	
	public MarkerAttribute(Long id, String value, Marker marker, Attribute attribute){
		super(id);
		this.setValue(value);
		this.setMarker(marker);
		this.setAttribute(attribute);
	}
	
	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
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
	/**
	 * @return the attribute
	 */
	public Attribute getAttribute()
	{
		return attribute;
	}
	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
	}
	
}
