/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;

/**
 * 
 * @author Vinicius Ramos Kawamoto 
 * @since 22/09/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject
@Table(schema=IEntity.SCHEMA)
public class FieldLayer extends AbstractEntity implements Serializable
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	private static final long serialVersionUID = -5049878026364017108L;
	/**
	 * name do {@link FieldLayer}
	 */
	@NotEmpty
	@Column(nullable=false, length=144)
	private String name;
	/**
	 * type do {@link FieldLayer}
	 */
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private TypeFieldLayer type;
	/**
	 * rótulo do {@link FieldLayer}
	 */
	@Column(nullable=true, length=144)
	private String label;
	/**
	 * ordem do {@link FieldLayer}
	 */
	@Column
	private int orderLayerField;
	
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public FieldLayer()
	{
		
	}
	/**
	 * @param id
	 */
	public FieldLayer( Long id )
	{
		this.setId(id);
	}
	/**
	 * @param id
	 * @param name
	 * @param rotulo
	 */
	public FieldLayer( Long id, String name, String label, int orderLayerField )
	{
		this.setId(id);
		this.setName(name);
		this.setLabel(label);
		this.setOrderLayerField(orderLayerField);
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
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + orderLayerField;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		FieldLayer other = (FieldLayer) obj;
		if (label == null)
		{
			if (other.label != null) return false;
		}
		else if (!label.equals(other.label)) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		if (orderLayerField != other.orderLayerField) return false;
		if (type != other.type) return false;
		return true;
	}
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public TypeFieldLayer getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(TypeFieldLayer type)
	{
		this.type = type;
	}
	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}
	/**
	 * @return the orderLayerField
	 */
	public int getOrderLayerField()
	{
		return orderLayerField;
	}
	/**
	 * @param orderLayerField the orderLayerField to set
	 */
	public void setOrderLayerField(int orderLayerField)
	{
		this.orderLayerField = orderLayerField;
	}
	
}
