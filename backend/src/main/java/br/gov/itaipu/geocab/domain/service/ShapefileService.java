/**
 *
 */
package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.application.exceptions.GeodesicCoordinatesAcceptedException;
import br.gov.itaipu.geocab.domain.entity.layer.Attribute;
import br.gov.itaipu.geocab.domain.entity.layer.AttributeType;
import br.gov.itaipu.geocab.domain.entity.layer.Layer;
import br.gov.itaipu.geocab.domain.entity.marker.Marker;
import br.gov.itaipu.geocab.domain.entity.marker.MarkerAttribute;
import br.gov.itaipu.geocab.domain.entity.shapefile.Shapefile;
import br.gov.itaipu.geocab.domain.repository.layer.AttributeRepository;
import br.gov.itaipu.geocab.domain.repository.marker.MarkerAttributeRepository;
import br.gov.itaipu.geocab.domain.repository.marker.MarkerRepository;
import br.gov.itaipu.geocab.infrastructure.jts.PointUtils;
import com.google.common.io.Files;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.Assert;
import org.apache.commons.codec.binary.Base64;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * @author emanuelvictor
 */
@Component
@SuppressWarnings("unchecked")
public class ShapefileService {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * Log
     */
    private static final Logger LOG = Logger.getLogger(DataSourceService.class.getName());
    /**
     *
     */
    @Autowired
    private MarkerRepository markerRepository;
    /**
     *
     */
    @Autowired
    private MarkerAttributeRepository markerAttributeRepository;
    /**
     *
     */
    @Autowired
    private AttributeRepository attributeRepository;
    /**
     * I18n
     */
    @Autowired
    private MessageSource messages;
	/*-------------------------------------------------------------------
	 *				 		    CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * Serviço de importação de shapefile
     *
     * @param shapefiles
     * @return
     */
    public List<Marker> importShapefile(List<Shapefile> shapefiles) {
        try {
            // Lê os arquivos
            List<Marker> markers = new ArrayList<>();

            for (Shapefile shapefile : shapefiles) {
                if (shapefile.getType() == ShpFileType.SHP) {
                    File shpFile = null;
                    try {
                        shpFile = generateShpTmpFile(shapefile);

                        // faz a importação dos markers
                        markers.addAll(importShpFile(shpFile, this.messages));
                    } finally {
                        // apaga o arquivo temporário
                        if (shpFile != null)
                            shpFile.delete();
                    }
                }
            }

            return markers;
        } catch (final GeodesicCoordinatesAcceptedException e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
            throw new RuntimeException(messages.getMessage("map.Tips-coordinate", null, null));
        } catch (final Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
            throw new RuntimeException(messages.getMessage("admin.shape.error.import", null, null));
        }
    }

    /**
     * @param wktPoint
     * @return
     */
    private static Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (com.vividsolutions.jts.io.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }

    /**
     * Importa a lista de postagem dos shapefiles já gravados no sistema de arquivos
     *
     * @param file
     * @param messages
     * @return
     * @throws GeodesicCoordinatesAcceptedException
     */
    private static List<Marker> importShpFile(final File file, final MessageSource messages) throws GeodesicCoordinatesAcceptedException {

        try {
            final Map<String, Object> map = new HashMap<>();
            map.put("url", file.toURI().toURL());

            final DataStore dataStore = DataStoreFinder.getDataStore(map);

            final String typeName = dataStore.getTypeNames()[0];

            final FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);

            final FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();

            final List<Marker> markers = new ArrayList<>();

            final FeatureIterator<SimpleFeature> features = collection.features();

            while (features.hasNext()) {
                final SimpleFeature feature = features.next();

                final CoordinateReferenceSystem entryCRS = source.getSchema().getCoordinateReferenceSystem();

                final CoordinateReferenceSystem EPSG3857 = CRS.decode("EPSG:3857");
                final MathTransform transform = CRS.findMathTransform(entryCRS, EPSG3857, true);

                final Geometry targetGeometry = JTS.transform(wktToGeometry(feature.getDefaultGeometryProperty().getValue().toString()).getEnvelope(), transform);

                final Marker marker = new Marker((Point) targetGeometry);

                final List<MarkerAttribute> markersAttributes = new ArrayList<>();
                for (final Property property : feature.getProperties()) {
                    // Deve ignorar a propriedade "the_geom"
                    if (property.getDescriptor().getName().toString() != "the_geom") {
                        try {
                            final Attribute attribute = new Attribute(null, property.getDescriptor().getName().toString(), property.getDescriptor().getType().getBinding().toString(), null);

                            //Extrai os atributos da feature
                            final MarkerAttribute markerAttribute = extractAttributes(feature, attribute, property, marker);

                            //Valida o atributo
                            markersAttributes.add(markerAttribute);

                            marker.setMarkerAttribute(markersAttributes);

                        }
                        //Ignora atributos inválidos, separar em uma função
                        catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
                markers.add(marker);
            }

            return markers;
        } catch (final IOException | FactoryException | MismatchedDimensionException | TransformException e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
            throw new RuntimeException("admin.shape.error.import");
        }
    }

    /**
     * Insere no sistema de arquivos o shapefile, provisoriamente
     *
     * @param shapefile
     * @return
     * @throws IOException
     */
    private static File generateShpTmpFile(Shapefile shapefile) throws IOException {
        // gera o arquivo temporário
        File shpFile = File.createTempFile("geocab", "." + shapefile.getType().toString().toLowerCase());

        // escreve o arquivo temporário
        try (FileOutputStream osf = new FileOutputStream(shpFile)) {

            Base64 decoder = new Base64();
            byte[] shpBytes = decoder.decode(shapefile.getSource());

            osf.write(shpBytes);
            osf.flush();

            return shpFile;
        }
    }

    /**
     * Agrupa as postagens pelas camadas
     *
     * @param markers
     * @return
     */
    private static List<Layer> groupByLayers(List<Marker> markers) {

        // pega todas as camadas referenciadas pelos markers
        Set<Layer> layers = markers.stream()
                .map(Marker::getLayer)
                .collect(Collectors.toSet());

        for (Layer layer : layers) {

            // pega os marcadores para camada atual
            List<Marker> layerMarkers = markers.parallelStream()
                    .filter(m -> m.getLayer().getId() == layer.getId())
                    .collect(Collectors.toList());

            // Inicializa os markers da layer
            layer.setMarkers(layerMarkers);
        }
        return new ArrayList<>(layers);
    }


    /**
     * Função que gera um shapefile a partir da lista de pontos passada.
     *
     * @param markers A lista de pontos a serem inclusos no shapefile.
     * @return O arquivo do shapefile
     */
    public InputStream exportShapefile(final List<Marker> markers) {

        Assert.isTrue(!markers.isEmpty(), "map.No-marker-to-export");

        final List<Layer> layers = groupByLayers(markers);

        final String fileExport = String.valueOf("geocab_" + Calendar.getInstance().getTimeInMillis());

        // gera a pasta temporária para a geração dos shapefiles das camadas
        File tempDir = Files.createTempDir();

        for (final Layer layer : layers) {
            try {
                layer.setAttributes(this.attributeRepository.listAttributeByLayer(layer.getId()));

                layer.setName(layer.getTitle());

                final SimpleFeatureType type = createType(layer);

                final DefaultFeatureCollection collection = new DefaultFeatureCollection();

                for (int i = 0; i < layer.getMarkers().size(); i++) {
                    final Marker marker = markerRepository.findOne(layer.getMarkers().get(i).getId());

                    marker.setMarkerAttribute(this.markerAttributeRepository.listAttributeByMarker(marker.getId()));

                    final CoordinateReferenceSystem EPSG3857 = CRS.decode("EPSG:3857");
                    final MathTransform transform = CRS.findMathTransform(EPSG3857, DefaultGeographicCRS.WGS84, true);

                    final Geometry targetGeometry = JTS.transform(
                            wktToGeometry(PointUtils.coordinateString(marker.getLocation())).getEnvelope(), transform);

                    marker.setLocation((Point) targetGeometry);

                    marker.formattedNameAttributes();

                    marker.handlerDuplicateAttributes();

                    final GeometryFactory factory = JTSFactoryFinder.getGeometryFactory(null);

                    final double longitude = marker.getLocation().getX();
                    final double latitude = marker.getLocation().getY();

                    final Point point = factory.createPoint(new Coordinate(longitude, latitude));

                    // O ponto também é um atributo "new Object[]{point}"
                    SimpleFeature feature = SimpleFeatureBuilder.build(type, new Object[]{point}, null);

                    // Extrai os atributos da feature
                    feature = extractFeatures(feature, marker);

                    collection.add(feature);
                }

                final DataStoreFactorySpi dataStoreFactorySpi = new ShapefileDataStoreFactory();

                final File newFile = new File(tempDir, layer.getName() + ".shp");
                final Map<String, Serializable> create = new HashMap<>();
                create.put("url", newFile.toURI().toURL());
                create.put("create spatial index", Boolean.TRUE);
                DataStore dataStore = dataStoreFactorySpi.createNewDataStore(create);
                SimpleFeatureType featureType = SimpleFeatureTypeBuilder.retype(type, DefaultGeographicCRS.WGS84);
                dataStore.createSchema(featureType);

                final Transaction transaction = new DefaultTransaction("create");
                final String typeName = dataStore.getTypeNames()[0];

                final FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) dataStore.getFeatureSource(typeName);

                featureStore.setTransaction(transaction);
                try {
                    featureStore.addFeatures(collection);
                    transaction.commit();
                } catch (final Exception e) {
                    e.printStackTrace();
                    transaction.rollback();
                } finally {
                    transaction.close();
                }
            } catch (final Exception e) {
                e.printStackTrace();
                LOG.info(e.getMessage());
                throw new RuntimeException("admin.shape.error.export");
            }
        }

        FileInputStream zip = compactFilesToZip(tempDir.getAbsolutePath(), fileExport + ".zip");

        //Deleta os arquivos de exportação
        tempDir.delete();

        return zip;
    }

    /**
     * Cria o TYPE da feature conforme a postagem com mais campos
     *
     * @param layer
     * @return
     * @throws SchemaException
     */
    private final SimpleFeatureType createType(Layer layer) throws SchemaException {
        //Verifica qual marker tem mais atributos
        int sum = 0;
        long markerIndex = 0L;

        Marker markerTest = null;
        for (int i = 0; i < layer.getMarkers().size(); i++) {
            markerTest = markerRepository.findOne(layer.getMarkers().get(i).getId());

            List<MarkerAttribute> markerAttributes = markerAttributeRepository.listAttributeByMarker(markerTest.getId());

            if (markerAttributes.size() > sum) {
                markerIndex = markerTest.getId();
                sum = markerAttributes.size();
            }
        }

        markerTest = markerRepository.findOne(markerIndex);

        markerTest.setMarkerAttribute(markerAttributeRepository.listAttributeByMarker(markerTest.getId()));

        markerTest.formattedNameAttributes();

        markerTest.handlerDuplicateAttributes();

        return DataUtilities.createType(layer.getName(), "the_geom:Point," + markerTest.formattedAttributes());
    }

    /**
     * @param feature
     * @param marker
     * @return
     */
    private static final SimpleFeature extractFeatures(SimpleFeature feature, final Marker marker) {
        for (final MarkerAttribute markerAttribute : marker.getMarkerAttribute()) {
            try {
                validateMarkerAttribute(markerAttribute);

                feature = extractFeatures(feature, markerAttribute);
            } catch (RuntimeException e) {
                // Se o attributo não é inválido parte para o próximo
                LOG.info(e.getMessage());
                continue;
            }
        }
        return feature;
    }


    /**
     * Encapsula comportamento de extração dos atributos da feature
     *
     * @param feature
     * @param attribute
     * @param property
     * @param marker
     * @return
     */
    private static final MarkerAttribute extractAttributes(final SimpleFeature feature, final Attribute attribute, final Property property, final Marker marker) {
        final String propertyDescriptor = property.getDescriptor().getName().getLocalPart();
        final Object featureAttribute = feature.getAttribute(propertyDescriptor);
        if (featureAttribute == null) {
            return new MarkerAttribute(null, "", marker, attribute);
        }
        // Se a feature for no formato tipo data, deve formatar a mesma
        if (attribute.getType() == AttributeType.DATE) {
            Date date = (Date) featureAttribute;
            return new MarkerAttribute(null, new SimpleDateFormat("dd/MM/yyyy").format(date), marker, attribute);
        } else if (attribute.getType() == AttributeType.BOOLEAN) {
            if ((boolean) featureAttribute) {
                return new MarkerAttribute(null, "Yes", marker, attribute);
            } else {
                return new MarkerAttribute(null, "No", marker, attribute);
            }
        } else {
            return new MarkerAttribute(null, featureAttribute.toString(), marker, attribute);
        }
    }


    /**
     * Valida o markerAttribute (sobrecarga de método)
     *
     * @param feature
     * @param markerAttribute
     * @return
     */
    private static final SimpleFeature extractFeatures(final SimpleFeature feature, final MarkerAttribute markerAttribute) {
        final String attribute = markerAttribute.getAttribute().getName();

        if (markerAttribute.getValue().toLowerCase().trim().equals("yes") || markerAttribute.getValue().toLowerCase().trim().equals("sim") && markerAttribute.getAttribute().getType() == AttributeType.BOOLEAN) {
            feature.setAttribute(attribute, true);
        } else if (markerAttribute.getValue().toLowerCase().trim().equals("no") || markerAttribute.getValue().toLowerCase().trim().equals("nao") && markerAttribute.getAttribute().getType() == AttributeType.BOOLEAN) {
            feature.setAttribute(attribute, false);
        } else if (markerAttribute.getAttribute().getType() == AttributeType.DATE) {
            try {
                Date date = null;
                if (markerAttribute.getValue() != null && !markerAttribute.getValue().equals("")) {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(markerAttribute.getValue());
                }
                feature.setAttribute(attribute, date);
            } catch (ParseException e) {
                e.printStackTrace();
                LOG.info(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            feature.setAttribute(attribute, markerAttribute.getValue());
        }

        return feature;
    }

    /**
     * Valida o attributo para exportação
     *
     * @param markerAttribute
     */
    private static final void validateMarkerAttribute(final MarkerAttribute markerAttribute) {
        Assert.isTrue(markerAttribute.getAttribute().getType() != null, "admin.shape.error.type-attribute-can-not-be-null");
        Assert.isTrue(markerAttribute.getAttribute().getType() != AttributeType.PHOTO_ALBUM, "admin.shape.error.type-attribute-can-not-be-a-photoalbum");
        Assert.isTrue(markerAttribute.getAttribute().getName() != null, "admin.shape.error.name-attribute-can-not-be-null");
        Assert.isTrue(markerAttribute.getValue() != null, "admin.shape.error.value-attribute-can-not-be-null");
    }

    /**
     * Compacta os arquivos .shp, .dbf e shx para o formato zip e devolve um fileTransfer
     *
     * @param pathExport
     * @return
     */
    private static FileInputStream compactFilesToZip(String pathExport, String fileExport) {
        try {
            final File file = new File(pathExport);

            byte[] buffer = new byte[10240];

            final FileOutputStream fileOutputStream = new FileOutputStream(pathExport + fileExport);

            final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            final List<ZipEntry> entries = new ArrayList<>();

            final List<FileInputStream> filesInputStream = new ArrayList<>();

            for (int i = 0; i < file.list().length; i++) {
                if (!file.list()[i].contains(".zip")) {
                    entries.add(new ZipEntry(file.list()[i]));
                    filesInputStream.add(new FileInputStream(pathExport + file.list()[i]));
                }
            }

            for (int i = 0; i < entries.size(); i++) {
                zipOutputStream.putNextEntry(entries.get(i));

                int len;
                while ((len = filesInputStream.get(i).read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }

                filesInputStream.get(i).close();
            }

            zipOutputStream.closeEntry();

            zipOutputStream.close();

            return new FileInputStream(pathExport + fileExport);

        } catch (final IOException e) {
            LOG.info(e.getMessage());
            throw new RuntimeException("admin.shape.error.export");
        }
    }

    /**
     * Deleta uma pasta com todos os arquivos dentro
     *
     * @param file
     */
    public static final void delete(final File file) {
        if (file.isDirectory()) {
            for (final String temp : file.list()) {
                delete(new File(file, temp));
            }
        } else {
            file.delete();
        }
    }


}