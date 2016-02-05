/**
 * 
 */
package br.com.geocab.domain.entity.shapefile;

import org.directwebremoting.annotations.DataTransferObject;
import org.geotools.data.shapefile.files.ShpFileType;

/**
 * @author emanuelvictor
 *
 * Classe responsável por trazer os arquivos shapefile do front-end para o back-end
 */
@DataTransferObject(javascript = "Shapefile")
public class Shapefile
{

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	private ShpFileType type;

	/**
	 * 
	 */
	private String source;

	/**
	 * 
	 */
	private Integer contentLength;

	/**
	 * 
	 */
	private String name;

	/*-------------------------------------------------------------------
	 *				 		     CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	public Shapefile()
	{
		super();
	}

	/**
	 * @param type
	 * @param source
	 * @param mimeType
	 * @param contentLength
	 * @param name
	 */
	public Shapefile(ShpFileType type, String source, Integer contentLength, String name)
	{
		super();
		this.type = type;
		this.source = source;
		this.contentLength = contentLength;
		this.name = name;
	}

	/*-------------------------------------------------------------------
	 *				 		     SETTERS/GETTERS
	 *-------------------------------------------------------------------*/
	/**
	 * @return the source
	 */
	public String getSource()
	{
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source)
	{
		this.source = source;
	}

	/**
	 * @return the contentLength
	 */
	public Integer getContentLength()
	{
		return contentLength;
	}

	/**
	 * @param contentLength
	 *            the contentLength to set
	 */
	public void setContentLength(Integer contentLength)
	{
		this.contentLength = contentLength;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public ShpFileType getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ShpFileType type)
	{
		this.type = type;
	}

}
