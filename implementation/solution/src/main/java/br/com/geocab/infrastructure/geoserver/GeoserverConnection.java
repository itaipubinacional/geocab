/**
 * 
 */
package br.com.geocab.infrastructure.geoserver;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import net.opengis.wfs.v_1_1_0.FeatureTypeType;
import net.opengis.wfs.v_1_1_0.WFSCapabilitiesType;
import net.opengis.wms.v_1_3_0.Layer;
import net.opengis.wms.v_1_3_0.WMSCapabilities;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.application.Messages;
import br.com.geocab.domain.entity.layer.ExternalLayer;
import br.com.geocab.domain.entity.datasource.DataSource;

/**
 * 
 * @author Marcos
 * @since 21/07/2014
 * @version 1.0
 * @category geoserver
 */
@Component
public class GeoserverConnection
{
	/**
	 * @param url
	 * @param tipoFonteDados
	 * @return
	 * @throws JAXBException
	 */
	@Transactional(readOnly=true)
	public boolean testaConexao(String url)
	{
			try
			{
				// Criamos o contexto JAXB para WMS 1.3.0
				JAXBContext context = JAXBContext.newInstance(ExternalLayer.CONTEXT_WMS);
				// Usamos o contexto JAXB para construir um unmarshaller
				Unmarshaller unmarshaller = context.createUnmarshaller();
				// Fazemos o unmarshaller da url passada por parâmetro e recuperamos o elemento wmsCapabilites
				JAXBElement<WMSCapabilities> wmsCapabilitiesElement = unmarshaller.unmarshal(new StreamSource(url), WMSCapabilities.class);
				// Recuperamos a instância de WMSCapabilities
				WMSCapabilities wmsCapabilities = (WMSCapabilities) wmsCapabilitiesElement.getValue();

				if (wmsCapabilities.getService().getName().equals("WMS"))
				{
					return true;
				}
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException( Messages.getException( "fontedados.sem_conexao" ) );
			}
		
		return false;
	}
	
	/**
	 * 
	 * @param fonteDados
	 * @return camadas
	 * @throws JAXBException
	 */
	@Transactional(readOnly=true)
	public List<ExternalLayer> listCamadasExternasByFilters( DataSource fonteDados )
	{
		
		List<ExternalLayer> camadas = new ArrayList<ExternalLayer>();

		try{
			JAXBContext context = JAXBContext.newInstance(ExternalLayer.CONTEXT_WMS);
			// Usamos o contexto JAXB para construir um unmarshaller
			Unmarshaller unmarshaller = context.createUnmarshaller();
			// Fazemos o unmarshaller da url passada por parâmetro e recuperamos o elemento wmsCapabilites
			JAXBElement<WMSCapabilities> wmsCapabilitiesElement = unmarshaller.unmarshal(new StreamSource(fonteDados.getUrl()), WMSCapabilities.class);
			// Recuperamos a instância de WMSCapabilities
			WMSCapabilities wmsCapabilities = (WMSCapabilities) wmsCapabilitiesElement.getValue();
			
			for (Layer layer : wmsCapabilities.getCapability().getLayer().getLayer()) 
			{
				ExternalLayer camada = new ExternalLayer();
				camada.setNome(layer.getName());
				camada.setTitulo(layer.getTitle());
				camada.setLegenda(layer.getStyle().get(0).getLegendURL().get(0).getOnlineResource().getHref());
				
				camadas.add(camada);
			}
		}
		catch (Exception e){
			throw new IllegalArgumentException( Messages.getException( "fontedados.sem_conexao" ) );
		}
		
		return camadas;

		
	}
}
