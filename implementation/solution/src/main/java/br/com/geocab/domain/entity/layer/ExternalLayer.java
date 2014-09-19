/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;

import javax.persistence.Transient;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * @author Vinicius 
 * @since 26/06/2014
 * @version 1.0
 * @category Entity
 */
@DataTransferObject
public class ExternalLayer implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1749949287538515868L;
	
	/**
	 * 
	 */
	public static final String CONTEXT_WMS = "net.opengis.wms.v_1_3_0";
	
	/**
	 * 
	 */
	public static final String CONTEXT_WFS = "net.opengis.wfs.v_1_1_0";
	
	/**
	 * 
	 */
	public static final String CAMPO_CAMADA_URL = "wfs?request=describeFeatureType&outputFormat=application/json&typename=";
	
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * nome da {@link ExternalLayer}
	 */
	@Transient
	private String nome;
	/**
	 * título da {@link ExternalLayer}
	 */
	@Transient
	private String titulo;
	
	/**
	 * tipo da {@link ExternalLayer}
	 */
	@Transient
	private String tipo;
	
	/**
	 * legenda da {@link ExternalLayer}
	 */
	@Transient
	private String legenda;
	
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public ExternalLayer()
	{
		
	}
	
	
	public ExternalLayer( String nome, String titulo, String legenda )
	{
		this.setNome(nome);
		this.setTitulo(titulo);
		this.setLegenda(legenda);
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
		int result = 1;
		result = prime * result + ((legenda == null) ? 0 : legenda.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ExternalLayer other = (ExternalLayer) obj;
		if (legenda == null)
		{
			if (other.legenda != null) return false;
		}
		else if (!legenda.equals(other.legenda)) return false;
		if (nome == null)
		{
			if (other.nome != null) return false;
		}
		else if (!nome.equals(other.nome)) return false;
		if (tipo == null)
		{
			if (other.tipo != null) return false;
		}
		else if (!tipo.equals(other.tipo)) return false;
		if (titulo == null)
		{
			if (other.titulo != null) return false;
		}
		else if (!titulo.equals(other.titulo)) return false;
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
	 * @return the titulo
	 */
	public String getTitulo()
	{
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo)
	{
		this.titulo = titulo;
	}


	/**
	 * @return the tipo
	 */
	public String getTipo()
	{
		return tipo;
	}


	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}


	/**
	 * @return the legenda
	 */
	public String getLegenda()
	{
		return legenda;
	}


	/**
	 * @param legenda the legenda to set
	 */
	public void setLegenda(String legenda)
	{
		this.legenda = legenda;
	}
	
}
