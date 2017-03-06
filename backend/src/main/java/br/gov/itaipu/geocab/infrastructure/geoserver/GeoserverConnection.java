/**
 *
 */
package br.gov.itaipu.geocab.infrastructure.geoserver;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.entity.layer.ExternalLayer;
import br.gov.itaipu.geocab.domain.entity.layer.LayerField;
import org.geotools.ows.ServiceException;

import java.io.IOException;
import java.util.List;


/**
 * Classe abstrata que representa uma conexão com o Geoserver. Uma nova
 * conexão deverá ser criada utilizando o método {@link #createConnection}.
 */
public abstract class GeoserverConnection {

    /**
     * Função que cria uma conexão com o Geoserver de acordo com o tipo da fonte
     * de dados.
     *
     * @param dataSource A fonte de dados associada.
     * @return O objeto da conexão com o Geoserver.
     * @throws IOException Lança exceção caso ocorra um problema de conexão.
     */
    public static GeoserverConnection createConnection(DataSource dataSource) throws IOException, ServiceException {
        // verifica o tipo da fonte
        switch (dataSource.getServiceType()) {
            case WMS:
                return new WmsGeoserverConnection(dataSource);
            case WFS:
                return new WfsGeoserverConnection(dataSource);
        }

        throw new IllegalArgumentException("Invalid service type");
    }

    protected DataSource dataSource;

    /**
     * Método que testa se a conexão está ativa.
     *
     * @throws IOException Lança exceção se a conexão não estiver ativa.
     */
    protected void testConnection() throws IOException {
        // checa a conexão
        if (!this.isConnected())
            throw new IOException("Not connected");
    }

    /**
     * Construtor da classe. Esta também realiza a inicialização da conexão com
     * a fonte de dados.
     *
     * @param dataSource A fonte de dados associada a conexão
     * @throws IOException Caso ocorra algum problema na comunicação com o Geoserver.
     * @throws ServiceException Caso o serviço chamado retorna uma resposta inválida.
     */
    protected GeoserverConnection(DataSource dataSource) throws IOException, ServiceException {
        this.dataSource = dataSource;

        // inicializa a conexão
        this.initialize();
    }

    /**
     * Retorna a fonte de dados associada a conexão.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Função que realiza a inicialização da conexão com o Geoserver de acordo
     * do tipo de serviço a ser utilizado pela fonte de dados associada.
     *
     * @throws IOException Caso ocorra algum problema na comunicação com o Geoserver.
     */
    protected abstract void initialize() throws IOException, ServiceException;

    /**
     * Função que informa se a conexão com o servidor do Geoserver está ativa.
     *
     * @return Retorna <code>true</code> se a conexão com o Geoserver estiver
     * ativa. Caso contrário, retorna <code>false</code>.
     */
    public abstract boolean isConnected();

    /**
     * Função que retorna a lista de camadas disponível para consulta da fonte de
     * dados associada.
     *
     * @return A lista de camadas disponíveis na fonte de dados.
     * @throws IOException Caso ocorra algum problema na comunicação com o Geoserver.
     */
    public abstract List<ExternalLayer> getExternalLayers() throws IOException;

    /**
     * Função que retorna a lista de campos de uma camada no Geoserver.
     *
     * @param layerName O nome da camada.
     * @return Retorna a lista de campos que compôem a camada.
     * @throws IOException Caso ocorra algum problema na comunicação com o Geoserver.
     * @throws ServiceException Caso o serviço chamado retorna uma resposta inválida.
     */
    public abstract List<LayerField> getLayerFields(String layerName) throws IOException, ServiceException;
}
