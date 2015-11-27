/**
 * 
 */
package br.com.geocab.tests.service;

import org.junit.Test;

import br.com.geocab.domain.service.ShapeFileService;

/**
 * @author emanuelvictor
 *
 */
public class ShapeFileServiceTest
{

	@Test
	public void testShapeFile(){
		ShapeFileService shapeFileService = new ShapeFileService();
		shapeFileService.exportShapeFile(null);
	}
}
