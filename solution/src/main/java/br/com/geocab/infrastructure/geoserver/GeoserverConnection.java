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
	public boolean testConnection(String url)
	{	
		
		if( url.toLowerCase().indexOf("service=wms") >= 0) //Test WMS service
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
				//TODO: Translate
				throw new IllegalArgumentException( "no connection" );
			}
		}
		else if(url.toLowerCase().indexOf("service=wfs") >= 0) //Test WFS service
		{
			try
			{
				JAXBContext context = JAXBContext.newInstance(ExternalLayer.CONTEXT_WFS);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				JAXBElement<WFSCapabilitiesType> wfsCapabilitiesElement = unmarshaller.unmarshal(new StreamSource(url), WFSCapabilitiesType.class);
				WFSCapabilitiesType wfsCapabilitiesType = (WFSCapabilitiesType) wfsCapabilitiesElement.getValue();
				
				if (wfsCapabilitiesType.getServiceIdentification().getServiceType().getValue().equals("WFS"))
				{
					return true;
				}
			}
			catch (Exception e)
			{
				//TODO: Translate
				throw new IllegalArgumentException( "no connection" );
			}
		}
		
		return false;
	}
	

	@Transactional(readOnly=true)
	public List<ExternalLayer> listExternalLayersByFilters( DataSource dataSource )
	{
		
		List<ExternalLayer> layers = new ArrayList<ExternalLayer>();
		
		if( dataSource.getUrl().toLowerCase().indexOf("service=wms") >= 0) //Test WMS service
		{
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
					
					if(!layer.getStyle().isEmpty()){
						externalLayer.setLegend(layer.getStyle().get(0).getLegendURL().get(0).getOnlineResource().getHref());
                    }
					
					layers.add(externalLayer);
				}
			}
			catch (Exception e){
				throw new IllegalArgumentException( "no connection" );
			}
			
			return layers;
		}
		else if( dataSource.getUrl().toLowerCase().indexOf("service=wfs") >= 0) //Test WFS service
		{
			try
			{
				JAXBContext context = JAXBContext.newInstance(ExternalLayer.CONTEXT_WFS);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				JAXBElement<WFSCapabilitiesType> wfsCapabilitiesElement = unmarshaller.unmarshal(new StreamSource(dataSource.getUrl()), WFSCapabilitiesType.class);
				WFSCapabilitiesType wfsCapabilitiesType = (WFSCapabilitiesType) wfsCapabilitiesElement.getValue();
				
				for(FeatureTypeType feature : wfsCapabilitiesType.getFeatureTypeList().getFeatureType() )
				{
					ExternalLayer camada = new ExternalLayer();
					camada.setName(feature.getName().getNamespaceURI()+":"+feature.getName().getLocalPart());
					camada.setTitle(feature.getTitle());
					
					layers.add(camada);
				}
			}
			catch (Exception e){
				throw new IllegalArgumentException( "no connection" );
			}
			
			return layers;
			
		}
		
		return layers;
	}
}
