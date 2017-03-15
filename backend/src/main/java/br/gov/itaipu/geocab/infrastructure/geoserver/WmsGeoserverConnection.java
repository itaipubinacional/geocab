package br.gov.itaipu.geocab.infrastructure.geoserver;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.entity.datasource.ServiceType;
import br.gov.itaipu.geocab.domain.entity.layer.ExternalLayer;
import br.gov.itaipu.geocab.domain.entity.layer.LayerField;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que representa uma conexão com o Geoserver utilizando serviços WMS para
 * consulta dos dados.
 */
class WmsGeoserverConnection extends GeoserverConnection {

    /**
     * Versão do protocolo WMS a ser utilizado por esta conexão.
     */
    private static final String WMS_VERSION = "1.3.0";

    private WebMapServer wms;

    /**
     * Retorna a URL para a requisição GetCapabilities do serviço WMS do
     * Geoserver.
     * @return A URL para a requisição GetCapabilities.
     */
    private String getGetCapabilitiesURL() {
        // adiciona os parâmetros da query a ser realizada no servidor
        return UriComponentsBuilder
                .fromUriString(this.dataSource.getUrl())
                .queryParam("service", "WMS")
                .queryParam("version", WMS_VERSION)
                .queryParam("request", "GetCapabilities")
                .build()
                .toUriString();
    }

    /**
     * Função que faz a geração da URL para os serviços WFS do servidor a partir da URL
     * dos serviços WMS.
     * @return A URL dos serviços WFS do servidor.
     */
    private String generateWfsUrl() {
        String wmsUrl = this.dataSource.getUrl();

        // o Geoserver suporta letras minúsculas
        String wfsUrl = wmsUrl.toLowerCase();
        // substitui o WMS por WFS
        wfsUrl = wfsUrl.replaceAll("wms", "wfs");

        return wfsUrl;
    }

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
        this.wms = new WebMapServer(new URL(this.getGetCapabilitiesURL()));
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
                .filter(layer -> !StringUtils.isEmpty(layer.getName()))
                .map(layer -> {
                    ExternalLayer externalLayer = new ExternalLayer();
                    externalLayer.setName(layer.getName());
                    externalLayer.setTitle(layer.getTitle());

                    // verifica se a legenda está disponível
                    List<StyleImpl> styles = layer.getStyles();

                    if (!styles.isEmpty()) {
                        String legendUrl = (String) styles.get(0).getLegendURLs().get(0);
                        externalLayer.setLegend(legendUrl);
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
         * no servidor. Para fazer esta tarefa seria utilizado o serviço DescribeLayer do
         * WMS para retornar o endereço do serviço WFS do servidor caso este suportar. Com
         * este endereço seria realizada uma conexão WFS com o servidor.
         *
         * Porém os testes revelaram que existe um bug no Geotools que impossibilita a
         * utilização deste método. Isto também é agravado pelo Geoserver já que este não suporta
         * versões mais recentes do protocolo WMS para a chamada deste serviço (funciona apenas na
         * versão 1.1.1). Logo, para resolver este problema, será feita uma lógica para gerar a URL
         * do serviço WFS a partir da URL do WMS e chamar o serviço diretamente.
         *
         * Página do bug report:
         * https://osgeo-org.atlassian.net/browse/GEOT-4251?page=com.atlassian.jira.plugin.system.issuetabpanels%3Achangehistory-tabpanel
         */

        // gera a URL do WFS a partir da URL do WMS
        String dataSourceUrl = this.generateWfsUrl();
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
