/**
 * 
 */
package br.com.geocab.tests.service;

import org.junit.Test;

import br.com.geocab.domain.service.ShapefileService;

/**
 * @author emanuelvictor
 *
 */
public class ShapeFileServiceTest
{

	@Test
	public void testShapeFile(){
		ShapefileService shapeFileService = new ShapefileService();
		shapeFileService.exportShapefile(null);
	}
}
