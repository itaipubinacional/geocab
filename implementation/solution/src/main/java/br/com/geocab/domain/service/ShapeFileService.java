/**
 * 
 */
package br.com.geocab.domain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.AttributeType;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.shapefile.ShapeFile;
import br.com.geocab.domain.repository.attribute.IAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;

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
	 * Log
	 */
	private static final Logger LOG = Logger.getLogger(DataSourceService.class.getName());
	/**
	 * 
	 */
	@Autowired
	private IMarkerRepository markerRepository;
	/**
	 * 
	 */
	@Autowired
	IAttributeRepository attributeRepository;
	/*-------------------------------------------------------------------
	 *				 		    CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * Sempre que o component for instanciado, 
	 * o mesmo vai verificar se existe a pasta shapefile e caso a mesma não exista será criada
	 */
	public ShapeFileService()
	{
		super();
		try
		{
			new File(PATH_SHAPE_FILES_EXPORT).mkdirs();
			new File(PATH_SHAPE_FILES_IMPORT).mkdirs();
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			LOG.info(e.getMessage());
		}
	}
		
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Serviço de importação de shapeFile
	 * 
	 * @param shapeFile
	 * @return
	 */
	public List<Marker> importShapeFile(List<ShapeFile> shapeFiles)
	{
		try
		{
			
			String pathFile = PATH_SHAPE_FILES_IMPORT + String.valueOf("geocab_" + Calendar.getInstance().getTimeInMillis());
			// Lê os arquivos
			List<File> files = new ArrayList<File>();
			
			List<Marker> markers = new ArrayList<>();
			
			for (ShapeFile shapeFile : shapeFiles)
			{
				files.add(this.readFile(shapeFile, pathFile));
			}
			
			
			for (ShapeFile shapeFile : shapeFiles)
			{
				if (shapeFile.getType() == ShpFileType.SHP)
				{
					markers = importt(this.readFile(shapeFile, pathFile));
				}
			}
			
			// Deleta o arquivo
			delete(new File(PATH_SHAPE_FILES_IMPORT));
			
			return markers;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOG.info(e.getMessage());
			throw new RuntimeException("Ocorreu um erro durante a importação: " + e.getMessage());
		}
	}
	
	private List<Marker> importt(File file)
	{
		try
		{
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("url", file.toURI().toURL());

		    DataStore dataStore = DataStoreFinder.getDataStore(map);
		    String typeName = dataStore.getTypeNames()[0];

		    FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);

		    FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();
		    
		    List<Marker> markers = new ArrayList<>();
		    
		    FeatureIterator<SimpleFeature> features = collection.features(); 
		    
	    	while (features.hasNext()) 
	        {
	    		SimpleFeature feature = features.next();
	            
    			Marker marker = new Marker();
    			    			
    			Coordinate coordinate = new Coordinate(getX(feature.getDefaultGeometryProperty().getValue().toString()), getY(feature.getDefaultGeometryProperty().getValue().toString()));
    			
    			Point point = new Point(coordinate);
    			
    			marker.setLocation(point);
    			
	            List<MarkerAttribute> markersAttributes = new ArrayList<>();
				for (Property property : feature.getProperties())
				{
					// Deve ignorar a propriedade "the_geom"
					if (property.getDescriptor().getName().toString() != "the_geom")
					{
						Attribute attribute = new Attribute(null, property.getDescriptor().getName().toString(), property.getDescriptor().getType().getBinding().toString(), null);
						MarkerAttribute markerAttribute = new MarkerAttribute(null, feature.getAttribute(property.getDescriptor().getName().getLocalPart() /*.toString()*/).toString(), marker, attribute);
						markersAttributes.add(markerAttribute);
					}
				}
				marker.setMarkerAttribute(markersAttributes);
				
				markers.add(marker);
	        }
		    	
		    return markers;
		}
		catch ( IOException e )
		{	
			e.printStackTrace();
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
	private File readFile(ShapeFile shapeFile, String pathFile)
	{
		pathFile += "." + shapeFile.getType().toString().toLowerCase();//".shp";

		Base64 decoder = new Base64();
		byte[] shpBytes = decoder.decode(shapeFile.getSource());
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
	@Autowired
	IMarkerAttributeRepository markerAttributeRepository;
	/**
	 * Agrupa as postagens pelas camadas
	 * @param markers
	 * @return
	 */
	private List<Layer> groupByLayers(final List<Marker> markers)
	{
		final Set<Layer> layers = new HashSet<>();
		
		for (final Marker marker : markers)
		{
			layers.add(marker.getLayer());
		}
		
		for (final Layer layer : layers)
		{	
			layer.setMarkers(new ArrayList<Marker>());
			for (final Marker marker : markers)
			{	
				if (marker.getLayer().getId() == layer.getId())
				{
					layer.getMarkers().add(marker);
				}
			}
		}
		return new ArrayList<Layer>(layers);
	}	
	
	/**
	 * Serviço de exportação para shapeFile
	 * 
	 * @param markers
	 * @return
	 */
	public FileTransfer exportShapeFile(final List<Marker> markers)
	{
		
		final List<Layer> layers = groupByLayers(markers);
		
		final String fileExport = String.valueOf("geocab_" + Calendar.getInstance().getTimeInMillis() );
		 
		for (final Layer layer : layers)
		{
			try
			{				
				layer.setAttributes(this.attributeRepository.listAttributeByLayer(layer.getId()));
				
				layer.setName(layer.getName().replaceAll(" ", "_"));
				
				SimpleFeatureType TYPE = null; 
	
				DefaultFeatureCollection collection = new DefaultFeatureCollection();
				
				for (int i = 0; i < layer.getMarkers().size(); i++)
	            {	
					Marker marker = layer.getMarkers().get(i);       
	            	marker = markerRepository.findOne(marker.getId());
	            	
	            	if ((i != 0 && marker.getLayer().getId() != layer.getMarkers().get(i - 1).getId()) || TYPE == null)
	            		TYPE = DataUtilities.createType(layer.getName(), "the_geom:Point,"+marker.formattedAttributes());
					
		            GeometryFactory factory = JTSFactoryFinder.getGeometryFactory(null);
	            	
	                double longitude = marker.getLocation().getX();
	                double latitude = marker.getLocation().getY();
	
	                Point point = factory.createPoint(new Coordinate(longitude, latitude));
	                // O ponto também é um attributo "new Object[]{point}"
	                SimpleFeature feature = SimpleFeatureBuilder.build(TYPE, new Object[]{point}, null);
	                
	                // Extrai os atributos da feature 
	                feature = this.extractAttributes(feature, marker);
	                
	                collection.add(feature);
	            }
	            
		        DataStoreFactorySpi dataStoreFactorySpi = new ShapefileDataStoreFactory();
	
		        final File newFile = new File(PATH_SHAPE_FILES_EXPORT + layer.getName() + ".shp");
		        Map<String, Serializable> create = new HashMap<String, Serializable>();
		        create.put("url", newFile.toURI().toURL());
		        create.put("create spatial index", Boolean.TRUE);
	
		        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactorySpi.createNewDataStore(create);
		        newDataStore.createSchema(TYPE);
		        newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);
	
		        Transaction transaction = new DefaultTransaction("create");
		        String typeName = newDataStore.getTypeNames()[0];
		        
		        @SuppressWarnings("unchecked")
				FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) newDataStore.getFeatureSource(typeName);
	
		        featureStore.setTransaction(transaction);
		        try 
		        {
		            featureStore.addFeatures(collection);
		            transaction.commit();
		        } 
		        catch (Exception problem) 
		        {
		            problem.printStackTrace();
		            transaction.rollback();
		        } 
		        finally 
		        {
		            transaction.close();
		        }
			}
			catch (final SchemaException | IOException e)
			{
				e.printStackTrace();
				LOG.info(e.getMessage());
				throw new RuntimeException("Ocorreu um erro durante a exportação: " + e.getMessage());
			}
	    }
		
		//Compcta os arquivos de exportação e trás para memória
		final FileTransfer fileTransfer = new FileTransfer(fileExport + ".zip", "application/zip", this.compactFilesToZip(PATH_SHAPE_FILES_EXPORT, fileExport + ".zip"));

		//Deleta os arquivos de exportação
		delete(new File(PATH_SHAPE_FILES_EXPORT));

		return fileTransfer;
	}
	
	/**
	 * 
	 * @param feature
	 * @param marker
	 * @return
	 */
	private SimpleFeature extractAttributes(SimpleFeature feature, Marker marker)
	{
		
		for (MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			if (markerAttribute.getAttribute().getType() != AttributeType.PHOTO_ALBUM)
			{
				System.out.println(markerAttribute.getAttribute().getName()+ " "+ markerAttribute.getValue());
				feature.setAttribute(markerAttribute.getAttribute().getName(), markerAttribute.getValue());	
			}
		}
		
		return feature;
	}
	
	/**
	 * Compacta os arquivos .shp, .dbf e shx para o formato zip e devolve um fileTransfer
	 * @param pathExport
	 * @return
	 */
	public FileInputStream compactFilesToZip(final String pathExport, final String fileExport)
	{
		try
		{
			final File file = new File(pathExport);

			byte[] buffer = new byte[10240];
			
			final FileOutputStream fileOutputStream = new FileOutputStream(pathExport + fileExport);

			final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

			final List<ZipEntry> entries = new ArrayList<ZipEntry>();
			
			final List<FileInputStream> filesInputStream = new ArrayList<FileInputStream>();
			
			for (int i = 0; i < file.list().length; i++)
			{
				if (!file.list()[i].contains(".zip"))
				{
					entries.add(new ZipEntry(file.list()[i]));
					filesInputStream.add(new FileInputStream(pathExport +file.list()[i]));
				}
			}

			for (int i = 0; i < entries.size(); i++)
			{
				zipOutputStream.putNextEntry(entries.get(i));

				int len;
				while ((len = filesInputStream.get(i).read(buffer)) > 0)
				{
					zipOutputStream.write(buffer, 0, len);
				}

				filesInputStream.get(i).close();
			}

			zipOutputStream.closeEntry();

			zipOutputStream.close();
			
			return new FileInputStream(pathExport + fileExport);

		}
		catch (final IOException e)
		{
			LOG.info(e.getMessage());
			throw new RuntimeException("Ocorreu um erro durante a exportação: " + e.getMessage());
		}
	}
	
	/**
	 * Deleta uma pasta com todos os arquivos dentro
	 * @param file
	 */
	public static void delete(final File file)
	{
		if (file.isDirectory())
		{
			
			for (final String temp : file.list())
			{
				delete(new File(file, temp));
			}
		}
		else
		{
			file.delete();
		}
	}
	
	/**
	 * Pega a string com as coordenadas e retorna a coordenada 'X'
	 * @param coordinateString
	 * @return
	 */
	private static double getX(String coordinateString)
	{
		coordinateString = coordinateString.replace("POINT (", "");
		coordinateString = coordinateString.replace(")", "");
		coordinateString = coordinateString.substring(0, coordinateString.indexOf(" "));
		double doubleCoordinate = Double.parseDouble(coordinateString);
		return doubleCoordinate;
	}
	
	/**
	 * Pega a string com as coordenadas e retorna a coordenada 'Y'
	 * @param coordinateString
	 * @return
	 */
	private static double getY(String coordinateString)
	{
		coordinateString = coordinateString.replace("POINT (", "");
		coordinateString = coordinateString.replace(")", "");
		coordinateString = coordinateString.substring(coordinateString.indexOf(" "), coordinateString.length());
		double doubleCoordinate = Double.parseDouble(coordinateString);
		return doubleCoordinate;
	}

}
