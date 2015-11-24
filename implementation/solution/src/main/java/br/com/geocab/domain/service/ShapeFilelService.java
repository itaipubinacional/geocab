/**
 * 
 */
package br.com.geocab.domain.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.simple.SimpleFeatureSource;
import org.springframework.stereotype.Service;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.shapefile.ShapeFile;

/**
 * @author emanuelvictor
 *
 */
@Service
@RemoteProxy(name = "shapeFileService")
public class ShapeFilelService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Log
	 */
	private static final Logger LOG = Logger
			.getLogger(DataSourceService.class.getName());

	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Serviço de importação de shapeFile
	 * 
	 * @param shapeFile
	 * @return
	 */
	public ShapeFile importShapeFile(ShapeFile shapeFile)
	{
		try
		{
			// Lê o arquivo
			File file = this.readFile(shapeFile);
			// ShpFiles shpFiles = new ShpFiles(file);
			//
			// FileDataStore store = FileDataStoreFinder.getDataStore(file);
			// SimpleFeatureSource featureSource = store.getFeatureSource();

			// Deleta o arquivo
			file.delete();
			return shapeFile;
		}
		catch (Exception e)
		{
			LOG.info(e.getMessage());
			throw new RuntimeException(
					"Ocorreu um erro durante a importação: " + e.getMessage());
		}

	}

	/**
	 * Serviço de exportação para shapeFile
	 * 
	 * @param markers
	 * @return
	 */
	public FileTransfer exportShapeFile(List<Marker> markers)
	{
		return null;
	}

	/**
	 * Insere no sistema de arquivos o shapeFile, provisoriamente
	 * 
	 * @param shapeFile
	 * @return
	 */
	private File readFile(ShapeFile shapeFile)
	{
		String pathFile = "/tmp/geocab/files/"
				+ Calendar.getInstance().getTimeInMillis() + ".shp";

		Base64 decoder = new Base64();
		byte[] shpBytes = decoder.decode(shapeFile.getShp());
		FileOutputStream osf;
		try
		{
			osf = new FileOutputStream(new File(pathFile));
			osf.write(shpBytes);
			osf.flush();
			osf.close();
			return new File(pathFile);
		}
		catch (IOException e)
		{
			LOG.info(e.getMessage());
			throw new RuntimeException(
					"Erro ao gravar arquivo de shapefile: " + e.getMessage());
		}
	}

}
