package br.com.geocab.domain.entity.layer;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
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
	@ManyToOne
	private Attribute attribute;
	
	/**
	 * 
	 */
	@OneToMany
	@JoinColumn(referencedColumnName = "id", name = "attribute_option_id")
	private List<MarkerAttribute> markerAttributes;

	
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
	
	
	/*-------------------------------------------------------------------
	 *								SETTERS/GETTERS
	 *-------------------------------------------------------------------*/

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public List<MarkerAttribute> getMarkerAttributes() {
		return markerAttributes;
	}

	public void setMarkerAttributes(List<MarkerAttribute> markerAttributes) {
		this.markerAttributes = markerAttributes;
	}

}
