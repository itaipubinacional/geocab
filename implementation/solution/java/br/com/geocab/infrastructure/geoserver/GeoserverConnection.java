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

import net.opengis.wms.v_1_3_0.Layer;
import net.opengis.wms.v_1_3_0.WMSCapabilities;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.layer.ExternalLayer;

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
			//TO DO
			//sem conexão
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
	public List<ExternalLayer> listExternalLayersByFilters( DataSource dataSource )
	{
		
		List<ExternalLayer> camadas = new ArrayList<ExternalLayer>();

		try{
			JAXBContext context = JAXBContext.newInstance(ExternalLayer.CONTEXT_WMS);
			// Usamos o contexto JAXB para construir um unmarshaller
			Unmarshaller unmarshaller = context.createUnmarshaller();
			// Fazemos o unmarshaller da url passada por parâmetro e recuperamos o elemento wmsCapabilites
			JAXBElement<WMSCapabilities> wmsCapabilitiesElement = unmarshaller.unmarshal(new StreamSource(dataSource.getUrl()), WMSCapabilities.class);
			// Recuperamos a instância de WMSCapabilities
			WMSCapabilities wmsCapabilities = (WMSCapabilities) wmsCapabilitiesElement.getValue();
			
			for (Layer layer : wmsCapabilities.getCapability().getLayer().getLayer()) 
			{
				ExternalLayer externalLayer = new ExternalLayer();
				externalLayer.setName(layer.getName());
				externalLayer.setTitle(layer.getTitle());
				externalLayer.setLegend(layer.getStyle().get(0).getLegendURL().get(0).getOnlineResource().getHref());
				
				camadas.add(externalLayer);
			}
		}
		catch (Exception e)
		{
			//TO DO
			//sem conexão
		}
		
		return camadas;

	}
}
