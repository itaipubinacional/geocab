package br.gov.itaipu.geocab.tests;

import net.opengis.wfs.DescribeFeatureTypeType;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.v1_1_0.WFSFeatureSource;
import org.geotools.data.wfs.v1_1_0.parsers.DescribeFeatureTypeParser;
import org.geotools.wfs.bindings.DescribeFeatureTypeTypeBinding;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcvmelo on 20/02/2017.
 */
public class GeoserverWfsTests {

    @Test
    public void testDescribeFeatureTypeRequest() throws IOException {
        // cria uma conexão com o servidor
        String getCapabilities = "http://geoserver.itaipu/wfs?REQUEST=GetCapabilities";

        Map connectionParameters = new HashMap();
        connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities);

        // Step 2 - connection
        DataStore data = DataStoreFinder.getDataStore(connectionParameters);

        // Step 3 - discouvery
        String typeNames[] = data.getTypeNames();
        String typeName = typeNames[0];
        SimpleFeatureType schema = data.getSchema(typeName);

        // Step 4 - target
        FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource(typeName);
        System.out.println("Metadata Bounds:" + source.getBounds());
    }
}
