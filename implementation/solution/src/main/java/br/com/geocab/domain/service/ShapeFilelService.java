/**
 * 
 */
package br.com.geocab.domain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ser.ArraySerializers;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
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
	 * shapeFile path
	 */
	private static String PATH_SHAPE_FILES = "/tmp/geocab/files/shapefile/";
	/**
	 * export shapeFile path
	 */
	private static String PATH_SHAPE_FILES_EXPORT = PATH_SHAPE_FILES
			+ "export/";

	/**
	 * import shapeFile path
	 */
	private static String PATH_SHAPE_FILES_IMPORT = PATH_SHAPE_FILES
			+ "import/";

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

		// TODO
		final String pathExport = PATH_SHAPE_FILES_EXPORT + "test"/* Calendar.getInstance().getTimeInMillis() */;

		FileTransfer fileTransfer = this.compactFiles(pathExport);

		delete(new File(pathExport));

		return fileTransfer;
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

	/**
	 * Deleta uma pasta com todos os arquivos dentro
	 * @param file
	 */
	public static void delete(File file)
	{
		if (file.isDirectory())
		{
			if (file.list().length == 0)
			{
				file.delete();
			}
			else
			{
				String files[] = file.list();

				for (String temp : files)
				{
					File fileDelete = new File(file, temp);

					delete(fileDelete);
				}

				if (file.list().length == 0)
				{
					file.delete();
				}
			}
		}
		else
		{
			file.delete();
		}
	}

	/**
	 * Compacta os arquivos .shp, .dbf e shx para o formato zip e devolve um
	 * fileTransfer
	 * 
	 * @param pathExport
	 * @return
	 */
	private FileTransfer compactFiles(String pathExport)
	{
		try
		{

			byte[] buffer = new byte[10240];

			// TODO especificar nome do arquivo
			FileOutputStream fileOutputStream = new FileOutputStream(
					pathExport + "/export.zip");

			ZipOutputStream zipOutputStream = new ZipOutputStream(
					fileOutputStream);

			// TODO especificar nome do arquivo
			ZipEntry entries[] =
			{ new ZipEntry("shapeFile.shp"), new ZipEntry("shapeFile.dpf"),
					new ZipEntry("shapeFile.shx") };

			// TODO especificar nome do arquivo
			FileInputStream filesInputStream[] =
			{ new FileInputStream(pathExport + "/shapeFile.shp"),
					new FileInputStream(pathExport + "/shapeFile.dbf"),
					new FileInputStream(pathExport + "/shapeFile.shx") };

			for (int i = 0; i < entries.length; i++)
			{
				zipOutputStream.putNextEntry(entries[i]);

				int len;
				while ((len = filesInputStream[i].read(buffer)) > 0)
				{
					zipOutputStream.write(buffer, 0, len);
				}

				filesInputStream[i].close();
			}

			zipOutputStream.closeEntry();

			zipOutputStream.close();

			// TODO AJustar nome do arquivo
			FileInputStream zip = new FileInputStream(
					pathExport + "/export.zip");

			// TODO AJustar nome do arquivo
			return new FileTransfer("export.zip", "application/zip", zip);

		}
		catch (IOException e)
		{
			LOG.info(e.getMessage());
			throw new RuntimeException(
					"Ocorreu um erro durante a exportação: " + e.getMessage());
		}
	}

}
