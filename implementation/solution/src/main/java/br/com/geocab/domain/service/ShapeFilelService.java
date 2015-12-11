/**
 * 
 */
package br.com.geocab.domain.service;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import br.com.geocab.domain.entity.shapefile.ShapeFile;

/**
 * @author emanuelvictor
 *
 */
@Service
@RemoteProxy(name = "shapeFileService")
public class ShapeFilelService
{
	public ShapeFile importShapeFile(ShapeFile shapeFile)
	{
		return shapeFile;
	}

	public ShapeFile exportShapeFile(ShapeFile shapeFile)
	{

		return shapeFile;
	}

}
