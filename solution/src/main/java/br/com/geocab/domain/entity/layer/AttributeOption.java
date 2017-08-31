package br.com.geocab.domain.entity.layer;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.marker.MarkerAttribute;

@Entity
@Audited
@DataTransferObject(javascript = "AttributeOption")
public class AttributeOption extends AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8501978128049108233L;

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Column( length= 50, nullable = false )
	private String description;

	/**
	 * 
	 */
	@ManyToOne( fetch = FetchType.EAGER, optional = false )
	private Attribute attribute;
	
	/*-------------------------------------------------------------------
	 *								CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	public AttributeOption() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public AttributeOption(Long id) {
		super(id);
	}
	
	/**
	 * 
	 * @param id
	 */
	public AttributeOption(Long id, String description, Long attributeId ) {
		super(id);
		this.description = description;
		this.attribute = new Attribute(attributeId);
	}
	
	
	/**
	 * @param description
	 * @param attribute
	 */
	public AttributeOption(String description, Attribute attribute) {
		super();
		this.description = description;
		this.attribute = attribute;
	}

	/*-------------------------------------------------------------------
	 *								SETTERS/GETTERS
	 *-------------------------------------------------------------------*/


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	

}
