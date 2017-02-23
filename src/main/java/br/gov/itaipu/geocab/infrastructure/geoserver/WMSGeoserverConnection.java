package br.gov.itaipu.geocab.infrastructure.geoserver;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.entity.datasource.ServiceType;
import br.gov.itaipu.geocab.domain.entity.layer.ExternalLayer;
import br.gov.itaipu.geocab.domain.entity.layer.LayerField;
import net.opengis.wms.v_1_3_0.LegendURL;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.LayerDescription;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.DescribeLayerRequest;
import org.geotools.data.wms.response.DescribeLayerResponse;
import org.geotools.ows.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que representa uma conexão com o Geoserver utilizando serviços WMS para
 * consulta dos dados.
 */
class WmsGeoserverConnection extends GeoserverConnection {

    private WebMapServer wms;

    /**
     * Construtor da classe. Esta também realiza a inicialização da conexão com
     * a fonte de dados.
     *
     * @param dataSource A fonte de dados associada a conexão
     * @throws IOException Caso ocorra algum problema na comunicação com o Geoserver.
     * @throws ServiceException Caso o serviço chamado retorna uma resposta inválida.
     */
    public WmsGeoserverConnection(DataSource dataSource) throws IOException, ServiceException {
        super(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize() throws IOException, ServiceException {
        // cria o objeto da conexão
        this.wms = new WebMapServer(new URL(this.dataSource.getUrl()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return this.wms != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExternalLayer> getExternalLayers() throws IOException {
        // faz o mapeamento de todas as camadas
        WMSCapabilities capabilities = this.wms.getCapabilities();
        List<Layer> layers = capabilities.getLayerList();

        // processa a lista de camadas
        return layers.parallelStream()
                .map(layer -> {
                    ExternalLayer externalLayer = new ExternalLayer();
                    externalLayer.setName(layer.getName());
                    externalLayer.setTitle(layer.getTitle());

                    // verifica se a legenda está disponível
                    List<StyleImpl> styles = layer.getStyles();

                    if (!styles.isEmpty()) {
                        LegendURL url = (LegendURL) styles.get(0).getLegendURLs().get(0);
                        externalLayer.setLegend(url.getOnlineResource().getHref());
                    }
                    return externalLayer;
                })
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LayerField> getLayerFields(String layerName) throws IOException, ServiceException {

        /*
         * O serviço WMS não suporta o retorno dos atributos das feature types das camadas
         * no servidor. Para fazer esta tarefa será utilizado o serviço DescribeLayer do
         * WMS para retornar o endereço do serviço WFS do servidor caso este suportar. Com
         * este endereço será realizada uma conexão WFS com o servidor.
         */

        // faz a requisição para retornar o endereço do serviço WFS
        DescribeLayerRequest request = this.wms.createDescribeLayerRequest();
        request.setLayers(layerName);
        DescribeLayerResponse response = this.wms.issueRequest(request);

        // checa se recebeu o endereço do serviço
        LayerDescription[] layerDescs = response.getLayerDescs();
        if (layerDescs == null || layerDescs.length == 0)
            throw new ServiceException("Layer not found");
        String wfsUrl = layerDescs[0].getWfs().toString();

        // a conexão com o Geoserver espera o endereço para o GetCapabilities
        String dataSourceUrl = wfsUrl + "request=GetCapabilities";
        DataSource ds = new DataSource(
                this.dataSource.getId(),
                this.dataSource.getName(),
                dataSourceUrl,
                this.dataSource.getLogin(),
                this.dataSource.getPassword(),
                ServiceType.WFS);

        GeoserverConnection wfsConn = GeoserverConnection.createConnection(ds);

        return wfsConn.getLayerFields(layerName);
    }
}
