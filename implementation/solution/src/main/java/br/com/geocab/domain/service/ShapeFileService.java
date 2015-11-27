/**
 * 
 */
package br.com.geocab.domain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.files.FileWriter;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.WKTReader2;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.io.ParseException;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.shapefile.ShapeFile;

/**
 * @author emanuelvictor
 *
 */
@Service
@RemoteProxy(name = "shapeFileService")
public class ShapeFileService
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
	private static String PATH_SHAPE_FILES_EXPORT = PATH_SHAPE_FILES + "export/";

	/**
	 * import shapeFile path
	 */
	private static String PATH_SHAPE_FILES_IMPORT = PATH_SHAPE_FILES + "import/";
	
	/**
	 * zip file name path
	 */
	private static String ZIP_FILE_NAME = "export_shape_file.zip";

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
			throw new RuntimeException("Ocorreu um erro durante a importação: " + e.getMessage());
		}

	}
	
	/**
	 * Insere no sistema de arquivos o shapeFile, provisoriamente
	 * 
	 * @param shapeFile
	 * @return
	 */
	private File readFile(ShapeFile shapeFile)
	{
		String pathFile = "/tmp/geocab/files/" + Calendar.getInstance().getTimeInMillis() + ".shp";

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
			throw new RuntimeException("Erro ao gravar arquivo de shapefile: " + e.getMessage());
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
	
		final String pathExport = PATH_SHAPE_FILES_EXPORT + /*"test"*/ Calendar.getInstance().getTimeInMillis() ;
		
		//Cria shapeFiles
		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
		
		
		try
		{
			SimpleFeatureType TYPE = DataUtilities.createType("location","geom:Point,name:String" /*name camada tal*/);

			WKTReader2 wkt = new WKTReader2();		
			
			for (Marker marker : markers)
			{
				featureCollection.add( SimpleFeatureBuilder.build( TYPE, new Object[]{ wkt.read("POINT(" + marker.getLocation().getX() + ","+ marker.getLocation().getY() + ")")}, null) );
			}
			
			SimpleFeatureSource source = DataUtilities.source( featureCollection );
			
			
			ShpFiles shpFiles = new ShpFiles("asdfa");
			
//			FileWriter fileWriter = FileWriterI
			shpFiles.acquireWriteFile(ShpFileType.SHP, new FilePathWriter());
			shpFiles.acquireWriteFile(ShpFileType.SHX, new FilePathWriter());
			shpFiles.acquireWriteFile(ShpFileType.DBF, new FilePathWriter());
			
		}
		catch (SchemaException | ParseException | MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		MemoryDataStore memory = new MemoryDataStore();
		
//		DataStore dataStore = 
//		SimpleFeatureSource featureSource = memory.getFeatureSource(typeName);
//	    SimpleFeatureType ft = featureSource.getSchema();
		
		//Compcta os arquivos de exportação
		FileTransfer fileTransfer = new FileTransfer(ZIP_FILE_NAME, "application/zip", this.compactFilesToZip(pathExport));

		//Deleta os arquivos de exportação
		delete(new File(pathExport));

		return fileTransfer;
	}
	/**
	 * Compacta os arquivos .shp, .dbf e shx para o formato zip e devolve um
	 * fileTransfer
	 * TODO alterar para fazer genérico, da mesma forma que o delete
	 * @param pathExport
	 * @return
	 */
	public FileInputStream compactFilesToZip(String pathExport)
	{
		try
		{
			File file = new File(pathExport);

			byte[] buffer = new byte[10240];
			
			FileOutputStream fileOutputStream = new FileOutputStream(pathExport + ZIP_FILE_NAME);

			ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

			ZipEntry entries[] = new ZipEntry[file.list().length];
			
			FileInputStream filesInputStream[] = new FileInputStream[file.list().length];
			
			for (int i = 0; i < file.list().length; i++)
			{
				entries[i] = new ZipEntry(file.list()[i]);
				filesInputStream[i] = new FileInputStream(pathExport + "/" +file.list()[i]);
			}

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
			
			return new FileInputStream(pathExport + ZIP_FILE_NAME);

		}
		catch (IOException e)
		{
			LOG.info(e.getMessage());
			throw new RuntimeException("Ocorreu um erro durante a exportação: " + e.getMessage());
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
	
	class FilePathWriter implements FileWriter{

		/* (non-Javadoc)
		 * @see org.geotools.data.shapefile.files.FileWriter#id()
		 */
		@Override
		public String id()
		{
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
