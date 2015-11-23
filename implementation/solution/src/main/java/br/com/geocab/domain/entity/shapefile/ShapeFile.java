/**
 * 
 */
package br.com.geocab.domain.entity.shapefile;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * @author emanuelvictor
 *
 */
@DataTransferObject(javascript = "ShapeFile")
public class ShapeFile
{

	private String name;

	private Long size;

	private String type;

	private String shp;

	/**
	 * 
	 */
	public ShapeFile()
	{
		super();
	}

	/**
	 * @param name
	 * @param size
	 * @param type
	 * @param shp
	 */
	public ShapeFile(String name, Long size, String type, String shp)
	{
		super();
		this.name = name;
		this.size = size;
		this.type = type;
		this.shp = shp;
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
	 * @return the size
	 */
	public Long getSize()
	{
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Long size)
	{
		this.size = size;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the shp
	 */
	public String getShp()
	{
		return shp;
	}

	/**
	 * @param shp
	 *            the shp to set
	 */
	public void setShp(String shp)
	{
		this.shp = shp;
	}

}
