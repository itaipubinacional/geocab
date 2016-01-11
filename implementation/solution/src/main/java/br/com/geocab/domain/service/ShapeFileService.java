/**
 * 
 */
package br.com.geocab.domain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.vividsolutions.jts.util.Assert;

import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.AttributeType;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.shapefile.ShapeFile;
import br.com.geocab.domain.repository.attribute.IAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;

/**
 * @author emanuelvictor
 *
 */
@Service
@RemoteProxy(name = "shapeFileService")
@SuppressWarnings("unchecked")
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
		catch (final RuntimeException e)
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
	public final List<Marker> importShapeFile(final List<ShapeFile> shapeFiles)
	{
		try
		{
			
			final String pathFile = PATH_SHAPE_FILES_IMPORT + String.valueOf("geocab_" + Calendar.getInstance().getTimeInMillis());
			// Lê os arquivos
			final List<File> files = new ArrayList<File>();
			
			for (final ShapeFile shapeFile : shapeFiles)
			{
				files.add(readFile(shapeFile, pathFile));
			}
			
			List<Marker> markers = new ArrayList<>();
			
			for (final ShapeFile shapeFile : shapeFiles)
			{
				if (shapeFile.getType() == ShpFileType.SHP)
				{
					markers = importt(readFile(shapeFile, pathFile));
				}
			}
			
			// Deleta o arquivo
			delete(new File(PATH_SHAPE_FILES_IMPORT));
			
			return markers;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			LOG.info(e.getMessage());
			throw new RuntimeException("Ocorreu um erro durante a importação: " + e.getMessage());
		}
	}
	
	/**
	 * Importa a lista de postagem dos shapeFiles já gravados no sistema de arquivos
	 * @param file
	 * @return
	 */
	private static final List<Marker> importt(final File file)
	{
		try
		{
			final Map<String, Object> map = new HashMap<String, Object>();
		    map.put("url", file.toURI().toURL());

		    final DataStore dataStore = DataStoreFinder.getDataStore(map);
		    final String typeName = dataStore.getTypeNames()[0];

		    final FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);

		    final FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();
		    
		    final List<Marker> markers = new ArrayList<>();
		    
		    final FeatureIterator<SimpleFeature> features = collection.features(); 
		    
	    	while (features.hasNext()) 
	        {
	    		final SimpleFeature feature = features.next();
	            
	    		final Marker marker = new Marker();
    			    			
    			final Coordinate coordinate = new Coordinate(getX(feature.getDefaultGeometryProperty().getValue().toString()), getY(feature.getDefaultGeometryProperty().getValue().toString()));
    			
    			marker.setLocation(new Point(coordinate));
    			
	            final List<MarkerAttribute> markersAttributes = new ArrayList<>();
				for (final Property property : feature.getProperties())
				{
					// Deve ignorar a propriedade "the_geom"
					if (property.getDescriptor().getName().toString() != "the_geom")
					{
						try
						{
							final Attribute attribute = new Attribute(null, property.getDescriptor().getName().toString(), property.getDescriptor().getType().getBinding().toString(), null);
							final MarkerAttribute markerAttribute = new MarkerAttribute(null, feature.getAttribute(property.getDescriptor().getName().getLocalPart() /*.toString() TODO NEM SEMPRE É STRING*/).toString(), marker, attribute);
							//Valida o atributo
							validateMarkerAttribute(markerAttribute);
							markersAttributes.add(markerAttribute);
							
							marker.setMarkerAttribute(markersAttributes);
							
						}
						//Ignora atributos inválidos, separar em uma função
						catch (Exception e)
						{
							e.printStackTrace();
							continue; 
						}
					}
				}
				markers.add(marker);
	        }
		    	
		    return markers;
		}
		catch ( final IOException e )
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
	private static final File readFile(final ShapeFile shapeFile, String pathFile)
	{
		pathFile += "." + shapeFile.getType().toString().toLowerCase();//".shp";

		final Base64 decoder = new Base64();
		final byte[] shpBytes = decoder.decode(shapeFile.getSource());
		try
		{
			final FileOutputStream osf = new FileOutputStream(new File(pathFile));
			osf.write(shpBytes);
			osf.flush();
			osf.close();
			return new File(pathFile);
		}
		catch (final IOException e)
		{
			LOG.info(e.getMessage());
			throw new RuntimeException("Erro ao gravar arquivo de shapefile: " + e.getMessage());// FIXME LOCALIZE
		}
	}
	
	/**
	 * Agrupa as postagens pelas camadas
	 * @param markers
	 * @return
	 */
	private static final List<Layer> groupByLayers(final List<Marker> markers)
	{		
		final Set<Layer> layers = new HashSet<>();
		
		for (final Marker marker : markers)
		{
			layers.add(marker.getLayer());
		}
		
		for (final Layer layer : layers)
		{	
			// Inicializa os markers da layer
			layer.setMarkers(new ArrayList<Marker>());
			for (final Marker marker : markers)
			{	
				if (marker.getLayer().getId().equals(layer.getId()))
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
		
		String fileExport = String.valueOf("geocab_" + Calendar.getInstance().getTimeInMillis() );
		 
		for (final Layer layer : layers)
		{
			try
			{				
				layer.setAttributes(this.attributeRepository.listAttributeByLayer(layer.getId()));
				
				layer.setName(layer.getName().replaceAll(" ", "_"));
				
				SimpleFeatureType TYPE = null; 
	
				final DefaultFeatureCollection collection = new DefaultFeatureCollection();
				
				for (int i = 0; i < layer.getMarkers().size(); i++)
	            {	
					final Marker marker = markerRepository.findOne(layer.getMarkers().get(i).getId());
	            	
	            	if ((i != 0 && marker.getLayer().getId() != layer.getMarkers().get(i - 1).getId()) || TYPE == null)
	            		TYPE = DataUtilities.createType(layer.getName(), "the_geom:Point,"+marker.formattedAttributes());
					
	            	final GeometryFactory factory = JTSFactoryFinder.getGeometryFactory(null);
	            	
	            	final double longitude = marker.getLocation().getX();
	                final double latitude = marker.getLocation().getY();
	
	                final Point point = factory.createPoint(new Coordinate(longitude, latitude));
	                // O ponto também é um atributo "new Object[]{point}"
	                SimpleFeature feature = SimpleFeatureBuilder.build(TYPE, new Object[]{point}, null);
	                
	                // Extrai os atributos da feature 
	                feature = extractAttributes(feature, marker);
	                
	                collection.add(feature);
	            }
	            
		        final DataStoreFactorySpi dataStoreFactorySpi = new ShapefileDataStoreFactory();
	
		        final File newFile = new File(PATH_SHAPE_FILES_EXPORT + layer.getName() + ".shp");
		        final Map<String, Serializable> create = new HashMap<String, Serializable>();
		        create.put("url", newFile.toURI().toURL());
		        create.put("create spatial index", Boolean.TRUE);
	
		        final ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactorySpi.createNewDataStore(create);
		        newDataStore.createSchema(TYPE);
		        newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);
	
		        final Transaction transaction = new DefaultTransaction("create");
		        final String typeName = newDataStore.getTypeNames()[0];
		        
				final FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) newDataStore.getFeatureSource(typeName);
	
		        featureStore.setTransaction(transaction);
		        try 
		        {
		            featureStore.addFeatures(collection);
		            transaction.commit();
		        } 
		        catch (final Exception e) 
		        {
		            e.printStackTrace();
		            transaction.rollback();
		        } 
		        finally 
		        {
		            transaction.close();
		        }
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				LOG.info(e.getMessage());
				throw new RuntimeException("Ocorreu um erro durante a exportação: " + e.getMessage()); //FIXME localize
			}
	    }
		
		//Compcta os arquivos de exportação e trás para memória
		final FileTransfer fileTransfer = new FileTransfer(fileExport + ".zip", "application/zip", compactFilesToZip(PATH_SHAPE_FILES_EXPORT, fileExport + ".zip"));

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
	private static final SimpleFeature extractAttributes(SimpleFeature feature, final Marker marker)
	{
		
		for (final MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			try
			{
				validateMarkerAttribute(markerAttribute);
				
				feature = extractAttributes(feature, markerAttribute);
			}
			catch (RuntimeException e)
			{
				// Se o attributo não é inválido parte para o próximo
				LOG.info(e.getMessage());
				continue;
			}
		}
		
		return feature;
	}
	
	/**
	 * Valida o markerAttribute (sobrecarga de método)
	 * @param feature
	 * @param markerAttribute
	 * @return
	 */
	private static final SimpleFeature extractAttributes(final SimpleFeature feature, final MarkerAttribute markerAttribute)
	{		
		if (markerAttribute.getValue().toLowerCase().trim().equals("yes") || markerAttribute.getValue().toLowerCase().trim().equals("sim") && markerAttribute.getAttribute().getType() == AttributeType.BOOLEAN)
		{
			feature.setAttribute(markerAttribute.getAttribute().getName(), new Boolean(true));
		}
		else if (markerAttribute.getValue().toLowerCase().trim().equals("no") || markerAttribute.getValue().toLowerCase().trim().equals("nao") && markerAttribute.getAttribute().getType() == AttributeType.BOOLEAN)
		{
			feature.setAttribute(markerAttribute.getAttribute().getName(), new Boolean(false));
		}
		else if (markerAttribute.getAttribute().getType() == AttributeType.DATE)
		{
			try
			{
				Date date = new SimpleDateFormat("dd/MM/yyyy").parse(markerAttribute.getValue());
				feature.setAttribute(markerAttribute.getAttribute().getName(), date);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
				LOG.info("Problema na conversão da data " +e.getMessage()); // FIXME Localize
				throw new RuntimeException(e);
			}
		}
		else
		{
			feature.setAttribute(markerAttribute.getAttribute().getName(), markerAttribute.getValue());
		}
		
		return feature;
	}
	
	/**
	 * Valida o attributo para exportação
	 * @param markerAttribute
	 */
	private static final void validateMarkerAttribute(final MarkerAttribute markerAttribute)
	{
		Assert.isTrue(markerAttribute.getAttribute().getType()  != null, "O tipo do atributo não pode ser nulo ");
		Assert.isTrue(markerAttribute.getAttribute().getType() != AttributeType.PHOTO_ALBUM, "O atributo não pode ser um album de fotos");
		Assert.isTrue(markerAttribute.getAttribute().getName() != null, "O nome do atributo não pode ser nulo "); // FIXME LOCALIZE
		Assert.isTrue(markerAttribute.getValue() != null , "O valor do atributo não pode ser nulo ");
	}
	
	/**
	 * Compacta os arquivos .shp, .dbf e shx para o formato zip e devolve um fileTransfer
	 * @param pathExport
	 * @return
	 */
	public static final FileInputStream compactFilesToZip(final String pathExport, final String fileExport)
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
	public static final void delete(final File file)
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
	private static final double getX(String coordinateString)
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
	private static final double getY(String coordinateString)
	{
		coordinateString = coordinateString.replace("POINT (", "");
		coordinateString = coordinateString.replace(")", "");
		coordinateString = coordinateString.substring(coordinateString.indexOf(" "), coordinateString.length());
		double doubleCoordinate = Double.parseDouble(coordinateString);
		return doubleCoordinate;
	}

}
