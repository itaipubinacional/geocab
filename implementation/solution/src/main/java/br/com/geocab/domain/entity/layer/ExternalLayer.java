/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;

import javax.persistence.Transient;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * @author Vinicius Ramos Kawamoto
 * @since 22/09/2014
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
	 * Name of {@link ExternalLayer}
	 */
	@Transient
	private String name;
	/**
	 * Title of {@link ExternalLayer}
	 */
	@Transient
	private String title;
	
	/**
	 * Type of {@link ExternalLayer}
	 */
	@Transient
	private String type;
	
	/**
	 * Legend of {@link ExternalLayer}
	 */
	@Transient
	private String legend;
	
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public ExternalLayer()
	{
		
	}
	
	
	public ExternalLayer( String name, String title, String legend )
	{
		this.setName(name);
		this.setTitle(title);
		this.setLegend(legend);
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
		result = prime * result + ((legend == null) ? 0 : legend.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ExternalLayer other = (ExternalLayer) obj;
		if (legend == null)
		{
			if (other.legend != null) return false;
		}
		else if (!legend.equals(other.legend)) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		if (title == null)
		{
			if (other.title != null) return false;
		}
		else if (!title.equals(other.title)) return false;
		if (type == null)
		{
			if (other.type != null) return false;
		}
		else if (!type.equals(other.type)) return false;
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
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}


	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}


	/**
	 * @return the legend
	 */
	public String getLegend()
	{
		return legend;
	}


	/**
	 * @param legend the legend to set
	 */
	public void setLegend(String legend)
	{
		this.legend = legend;
	}
	
	
}
