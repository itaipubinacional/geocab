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
	 * nome do {@link FieldLayer}
	 */
	@NotEmpty
	@Column(nullable=false, length=144)
	private String nome;
	/**
	 * tipo do {@link FieldLayer}
	 */
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private TypeFieldLayer tipo;
	/**
	 * rótulo do {@link FieldLayer}
	 */
	@Column(nullable=true, length=144)
	private String label;
	/**
	 * ordem do {@link FieldLayer}
	 */
	@Column
	private int orderCampoCamada;
	
	
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
	 * @param nome
	 * @param rotulo
	 */
	public FieldLayer( Long id, String nome, String label, int orderCampoCamada )
	{
		this.setId(id);
		this.setNome(nome);
		this.setLabel(label);
		this.setOrderCampoCamada(orderCampoCamada);
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
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + orderCampoCamada;
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		if (nome == null)
		{
			if (other.nome != null) return false;
		}
		else if (!nome.equals(other.nome)) return false;
		if (orderCampoCamada != other.orderCampoCamada) return false;
		if (tipo != other.tipo) return false;
		return true;
	}
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @return the nome
	 */
	public String getNome()
	{
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	/**
	 * @return the tipo
	 */
	public TypeFieldLayer getTipo()
	{
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TypeFieldLayer tipo)
	{
		this.tipo = tipo;
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
	 * @return the orderCampoCamada
	 */
	public int getOrderCampoCamada()
	{
		return orderCampoCamada;
	}
	/**
	 * @param orderCampoCamada the orderCampoCamada to set
	 */
	public void setOrderCampoCamada(int orderCampoCamada)
	{
		this.orderCampoCamada = orderCampoCamada;
	}
	
}
