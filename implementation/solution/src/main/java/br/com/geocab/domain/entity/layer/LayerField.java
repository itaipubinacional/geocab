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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.geocab.application.ResourceBundleMessageSource;
import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;

/**
 * 
 * Classe responsável por definir o comportamento de um {@link LayerField}
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/12/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject
@Table(schema=IEntity.SCHEMA)
public class LayerField extends AbstractEntity implements Serializable
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	private static final long serialVersionUID = -5049878026364017108L;
	/**
	 * name of {@link LayerField}
	 */
	@NotEmpty
	@Column(nullable=false, length=144)
	private String name;
	/**
	 * type of  {@link LayerField}
	 */
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private LayerFieldType type;
	/**
	 * description of type layer come from geoserver
	 */
	@Transient
	private String typeGeoServer;
	/**
	 * label of {@link LayerField}
	 */
	@Column(nullable=true, length=144)
	private String label;
	/**
	 * order of {@link LayerField}
	 */
	@Column
	private int orderLayer;
	
	/**
	 * label of {@link LayerField}
	 */
	@Column(nullable=true)
	private Long attributeId;
	
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public LayerField()
	{
		
	}
	/**
	 * @param id
	 */
	public LayerField( Long id )
	{
		this.setId(id);
	}
	/**
	 * 
	 * @param id
	 * @param name
	 * @param label
	 * @param order
	 */
	public LayerField( Long id, String name, String label, int order )
	{
		this.setId(id);
		this.setName(name);
		this.setLabel(label);
		this.setOrderLayer(order);
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
		result = prime * result
				+ ((typeGeoServer == null) ? 0 : typeGeoServer.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + orderLayer;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		LayerField other = (LayerField) obj;
		if (typeGeoServer == null)
		{
			if (other.typeGeoServer != null) return false;
		}
		else if (!typeGeoServer.equals(other.typeGeoServer)) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		if (orderLayer != other.orderLayer) return false;
		if (label == null)
		{
			if (other.label != null) return false;
		}
		else if (!label.equals(other.label)) return false;
		if (type != other.type) return false;
		return true;
	}
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @param descricaoTipo the descricaoTipo to set
	 */
	public void setTipoGeoServer(String descricaoTipo)
	{
		this.typeGeoServer = descricaoTipo;
		
		if( null != parseTipoGeoserver(descricaoTipo))
			this.type = parseTipoGeoserver(descricaoTipo);
	}
	
	/**
	 * @param descricaoTipo the descricaoTipo to set
	 */
	private  LayerFieldType parseTipoGeoserver(String descricaoTipo){
		
		if (descricaoTipo != null)
		{
			if (descricaoTipo.contains("string"))
			{
				return LayerFieldType.STRING;
				
			} else if (descricaoTipo.contains("int"))
			{
				return LayerFieldType.INT;
				
			} else if ((descricaoTipo.contains("number")))
			{
				return LayerFieldType.NUMBER;
				
			} else if ((descricaoTipo.contains("date")))
			{
				return LayerFieldType.DATETIME;
				
			} else if ((descricaoTipo.contains("Geometry")))
			{
				return LayerFieldType.STRING;
				
			}
		}
		
		return null;
	}
	
	/**
	 * Método que verifica se um TipoGeoserver é um tipo mapeado valido 
	 * @return {@link Boolean}
	 */
	public boolean isValidTipoGeoserver(String descricaoTipo)
	{
		return ( null == parseTipoGeoserver(descricaoTipo)) ? false : true;
	}
	
	
	/**
	 * Method that translates a field of type enum
	 * @return the descriptionType
	 */
	public String getDescriptionType()
	{
		/*if (this.type.equals(LayerFieldType.STRING)) return ResourceBundleMessageSource.getEnum("enum.tipoCampoCamada.STRING");
		else if (this.type.equals(LayerFieldType.INT)) return ResourceBundleMessageSource.getEnum("enum.tipoCampoCamada.INT");
		else if (this.type.equals(LayerFieldType.NUMBER)) return ResourceBundleMessageSource.getEnum("enum.tipoCampoCamada.NUMBER");
		else if (this.type.equals(LayerFieldType.DOUBLE)) return ResourceBundleMessageSource.getEnum("enum.tipoCampoCamada.DOUBLE");
		else if (this.type.equals(LayerFieldType.DATETIME)) return ResourceBundleMessageSource.getEnum("enum.tipoCampoCamada.DATETIME");
		else if (this.type.equals(LayerFieldType.SHORT)) return ResourceBundleMessageSource.getEnum("enum.tipoCampoCamada.SHORT");
		else if (this.type.equals(LayerFieldType.DECIMAL)) return ResourceBundleMessageSource.getEnum("enum.tipoCampoCamada.DECIMAL");
		
		else return "";*/
		return null;
	}
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
	public LayerFieldType getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(LayerFieldType type)
	{
		this.type = type;
	}
	/**
	 * @return the typeGeoServer
	 */
	public String getTypeGeoServer()
	{
		return typeGeoServer;
	}
	/**
	 * @param typeGeoServer the typeGeoServer to set
	 */
	public void setTypeGeoServer(String typeGeoServer)
	{
		this.typeGeoServer = typeGeoServer;
		
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
	 * @return the orderLayer
	 */
	public int getOrderLayer()
	{
		return orderLayer;
	}
	/**
	 * @param orderLayer the orderLayer to set
	 */
	public void setOrderLayer(int orderLayer)
	{
		this.orderLayer = orderLayer;
	}
	/**
	 * @return the attributeId
	 */
	public Long getAttributeId()
	{
		return attributeId;
	}
	/**
	 * @param attributeId the attributeId to set
	 */
	public void setAttributeId(Long attributeId)
	{
		this.attributeId = attributeId;
	}
	
}
