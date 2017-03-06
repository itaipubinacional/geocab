package br.gov.itaipu.geocab.infrastructure.geoserver;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.entity.layer.ExternalLayer;
import br.gov.itaipu.geocab.domain.entity.layer.LayerField;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.ows.ServiceException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe que representa uma conexão com o Geoserver utilizando serviços WFS para
 * consulta dos dados.
 */
class WfsGeoserverConnection extends GeoserverConnection {

    /**
     * Versão do protocolo WFS a ser utilizado por esta conexão.
     */
    private static final String WFS_VERSION = "1.1.1";

    private DataStore dataStore;

    /**
     * Retorna a URL para a requisição GetCapabilities do serviço WMS do
     * Geoserver.
     * @return A URL para a requisição GetCapabilities.
     */
    private String getGetCapabilitiesURL() {
        // adiciona os parâmetros da query a ser realizada no servidor
        return UriComponentsBuilder
                .fromUriString(this.dataSource.getUrl())
                .queryParam("service", "WFS")
                .queryParam("version", WFS_VERSION)
                .queryParam("request", "GetCapabilities")
                .build()
                .toUriString();
    }

    /**
     * Construtor da classe. Esta também realiza a inicialização da conexão com
     * a fonte de dados.
     *
     * @param dataSource A fonte de dados associada a conexão
     * @throws IOException Caso ocorra algum problema na comunicação com o Geoserver.
     * @throws ServiceException Caso o serviço chamado retorna uma resposta inválida.
     */
    protected WfsGeoserverConnection(DataSource dataSource) throws IOException, ServiceException {
        super(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize() throws IOException {
        // inicializa a conexão a partir dos atributos da conexão
        Map connectionParameters = new HashMap();
        connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", this.getGetCapabilitiesURL());

        this.dataStore = DataStoreFinder.getDataStore(connectionParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        // caso o objeto da conexão tenha sido ajustado
        return this.dataStore != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExternalLayer> getExternalLayers() throws IOException {

        this.testConnection();

        List<ExternalLayer> layers = new ArrayList<>();

        /*
         * Pega a lista de todas as camadas para depois pegar as informações
         * de cada uma das camadas.
         */
        String[] typeNames = this.dataStore.getTypeNames();

        for (String typeName : typeNames) {
            SimpleFeatureSource featureSource = this.dataStore.getFeatureSource(typeName);
            ResourceInfo info = featureSource.getInfo();
            layers.add(
                    new ExternalLayer(
                            info.getName(), info.getTitle(), null));
        }

        return layers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LayerField> getLayerFields(String layerName) throws IOException, ServiceException {
        // pega a definição da camada passada
        SimpleFeatureType schema = this.dataStore.getSchema(layerName);

        List<AttributeDescriptor> attributeDescriptors = schema.getAttributeDescriptors();

        return attributeDescriptors.parallelStream()
                .map(desc -> {
                    LayerField field = new LayerField();
                    field.setName(desc.getLocalName());
                    field.setTipoGeoServer(desc.getType().getBinding().getSimpleName());
                    return field;
                })
                .collect(Collectors.toList());
    }
}
