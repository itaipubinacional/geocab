/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.marker.Marker;

/**
 * 
 * @author Vinicius Ramos Kawamoto
 * @since 19/09/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject(javascript = "Layer")
public class Layer extends AbstractEntity implements Serializable, ITreeNode
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8456156059496726312L;

	/**
	 * 
	 */
	public static final String LEGEND_GRAPHIC_URL = "ows?service=wms&version=1.3.0&request=GetLegendGraphic&layer=";

	/**
	 * 
	 */
	public static final String LEGEND_GRAPHIC_FORMAT = "&format=image/png";

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Name {@link Layer}
	 */
	@NotEmpty
	@Column(nullable = false, length = 144)
	private String name;
	/**
	 * Title {@link Layer}
	 */
	@NotEmpty
	@Column(nullable = false, length = 144)
	private String title;
	/**
	 * Legend {@link Layer}
	 */
	@Transient
	private String legend;
	/**
	 * Indicates that the {@link Layer} will start enabled on map
	 */
	@Basic
	private Boolean startEnabled = false;
	/**
	 * Indicates that the {@link Layer} will be visible in the layer menu
	 */
	@Basic
	private Boolean startVisible = false;

	/**
	 * status
	 */
	private Boolean enabled;
	/**
	 * Field that informs if the {@link Layer} is published
	 */
	@Column
	private Boolean published;

	/**
	 * Order of {@link Layer}
	 */
	@Column
	private Integer orderLayer;
	/**
	 * Icon of {@link Layer}
	 */
	@Column
	private String icon;
	/**
	 * {@link MapScale} minimum of {@link Layer}
	 */
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private MapScale minimumScaleMap;
	/**
	 * {@link MapScale} maximum of {@link Layer}
	 */
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private MapScale maximumScaleMap;

	/**
	 * {@link DataSource} of {@link Layer}
	 */
	@ManyToOne
	private DataSource dataSource;
	/**
	 * {@link LayerGroup} of {@link Layer}
	 */
	@JsonIgnore
	@ManyToOne
	private LayerGroup layerGroup;
	/**
	 * Draft {@link Layer} that originated the published {@link Layer}
	 */
	@JsonIgnore
	@OneToOne
	private Layer publishedLayer;

	@JsonIgnore
//	@OneToMany(mappedBy = "layer", fetch = FetchType.EAGER, cascade =
//	{ CascadeType.REMOVE })
	@Transient
	private List<Marker> markers = new ArrayList<Marker>();

	@JsonIgnore
//	@OneToMany(mappedBy = "layer", fetch = FetchType.EAGER, cascade =
//	{ CascadeType.ALL })
	@Transient
	private List<Attribute> attributes = new ArrayList<Attribute>();

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	public Layer()
	{

	}

	/**
	 * 
	 * @param id
	 */
	public Layer(Long id)
	{
		this.setId(id);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param orderLayer
	 * @param icon
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, Integer orderLayer, String icon, Long publishedLayerId)
	{
		
		this.setId(id);
		this.setName(name);
		this.setOrderLayer(orderLayer);
		this.setIcon(icon);
		this.setPublishedLayer(new Layer(publishedLayerId));
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param orderLayer
	 * @param icon
	 * @param dataSource
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, Integer orderLayer, String icon, DataSource dataSource, Long publishedLayerId)
	{		
		this.setId(id);
		this.setName(name);
		this.setOrderLayer(orderLayer);
		this.setIcon(icon);
		this.setDataSource(dataSource);
		this.setPublishedLayer(new Layer(publishedLayerId));
	}

	/**
	 * 
	 * @param id
	 * @param title
	 * @param publishedLayerId
	 */
	public Layer(Long id, String title, Long publishedLayerId)
	{
		this.setId(id);
		this.setTitle(title);
		this.setPublishedLayer(new Layer(publishedLayerId));
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param icon
	 * @param startEnabled
	 * @param startVisible
	 * @param enabled
	 * @param published
	 * @param dataSource
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, String title, String icon,
			Boolean startEnabled, Boolean startVisible, Boolean enabled,
			Boolean published, DataSource dataSource, Long publishedLayerId)
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setIcon(icon);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setEnabled(enabled);
		this.setPublished(published);
		this.setDataSource(dataSource);
		this.setPublishedLayer(new Layer(publishedLayerId));
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param icon
	 * @param startEnabled
	 * @param startVisible
	 * @param enabled
	 * @param published
	 * @param layerGroupId
	 * @param layerGroupName
	 * @param orderLayerGroup
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, String title, String icon,
			Boolean startEnabled, Boolean startVisible, Boolean enabled,
			Boolean published, Long layerGroupId, String layerGroupName,
			Integer orderLayerGroup, Long publishedLayerId)
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setIcon(icon);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setEnabled(enabled);
		this.setPublished(published);

		LayerGroup layerGroup = new LayerGroup();
		layerGroup.setId(layerGroupId);
		layerGroup.setName(layerGroupName);
		layerGroup.setOrderLayerGroup(orderLayerGroup);

		this.setLayerGroup(layerGroup);
		
		this.setPublishedLayer(new Layer(publishedLayerId));
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param icon
	 * @param startEnabled
	 * @param startVisible
	 * @param enabled
	 * @param published
	 * @param layerGroupId
	 * @param layerGroupName
	 * @param orderLayerGroup
	 * @param dataSource
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, String title, String icon,
			Boolean startEnabled, Boolean startVisible, Boolean enabled,
			Boolean published, Long layerGroupId, String layerGroupName,
			Integer orderLayerGroup, DataSource dataSource, Long publishedLayerId)
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setIcon(icon);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setEnabled(enabled);
		this.setPublished(published);

		LayerGroup layerGroup = new LayerGroup();
		layerGroup.setId(layerGroupId);
		layerGroup.setName(layerGroupName);
		layerGroup.setOrderLayerGroup(orderLayerGroup);

		this.setDataSource(dataSource);

		this.setLayerGroup(layerGroup);
		
		this.setPublishedLayer(new Layer(publishedLayerId));
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param startEnabled
	 * @param startVisible
	 * @param orderLayer
	 * @param minimumMapScale
	 * @param maximumMapScale
	 * @param enabled
	 * @param dataSource
	 * @param layerGroup
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, String title, 
			Boolean startEnabled, Boolean startVisible, Integer orderLayer, 
			MapScale minimumMapScale, MapScale maximumMapScale, Boolean enabled, 
			DataSource dataSource,	LayerGroup layerGroup, Long publishedLayerId)
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setOrderLayer(orderLayer);
		this.setMinimumScaleMap(minimumMapScale);
		this.setMaximumScaleMap(maximumMapScale);
		this.setEnabled(enabled);
		this.setDataSource(dataSource);
		this.setLayerGroup(layerGroup);
		
		this.setPublishedLayer(new Layer(publishedLayerId));
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param icon
	 * @param startEnabled
	 * @param startVisible
	 * @param orderLayer
	 * @param minimumMapScale
	 * @param maximumMapScale
	 * @param enabled
	 * @param dataSource
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, String title, String icon, 
			Boolean startEnabled, Boolean startVisible, Integer orderLayer, 
			MapScale minimumMapScale, MapScale maximumMapScale, Boolean enabled, 
			DataSource dataSource, Long publishedLayerId)
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setIcon(icon);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setOrderLayer(orderLayer);
		this.setMinimumScaleMap(minimumMapScale);
		this.setMaximumScaleMap(maximumMapScale);
		this.setEnabled(enabled);
		this.setDataSource(dataSource);
		
		this.setPublishedLayer(new Layer(publishedLayerId));
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param icon
	 * @param startEnabled
	 * @param startVisible
	 * @param orderLayer
	 * @param minimumMapScale
	 * @param maximumMapScale
	 * @param enabled
	 * @param dataSource
	 * @param layerGroupName
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, String title, String icon,
			Boolean startEnabled, Boolean startVisible, Integer orderLayer,
			MapScale minimumMapScale, MapScale maximumMapScale, Boolean enabled,
			DataSource dataSource, String layerGroupName, Long publishedLayerId)
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setOrderLayer(orderLayer);
		this.setMinimumScaleMap(minimumMapScale);
		this.setMaximumScaleMap(maximumMapScale);
		this.setEnabled(enabled);
		
		this.setDataSource(dataSource);
		
		LayerGroup layerGroup = new LayerGroup();
		layerGroup.setName(layerGroupName);
		this.setLayerGroup(layerGroup);
		
		this.setIcon(icon);
		
		this.setPublishedLayer(new Layer(publishedLayerId));
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param icon
	 * @param startEnabled
	 * @param startVisible
	 * @param orderLayer
	 * @param minimumScaleMap
	 * @param maximumScaleMap
	 * @param enabled
	 * @param published
	 * @param dataSource
	 * @param layerGroupName
	 * @param layerGroupId
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, String title, String icon,
			Boolean startEnabled, Boolean startVisible, Integer orderLayer,
			MapScale minimumScaleMap, MapScale maximumScaleMap, Boolean enabled, 
			Boolean published, 
			DataSource dataSource, String layerGroupName, Long layerGroupId, Long publishedLayerId)
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setIcon(icon);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setOrderLayer(orderLayer);
		this.setMinimumScaleMap(minimumScaleMap);
		this.setMaximumScaleMap(maximumScaleMap);
		this.setEnabled(enabled);
		this.setPublished(published);
		this.setDataSource(dataSource);
		
		LayerGroup layerGroup = new LayerGroup(); 
		layerGroup.setName(layerGroupName);
		layerGroup.setId(layerGroupId);
		this.setLayerGroup(layerGroup);
		
		this.setPublishedLayer(new Layer(publishedLayerId));
	}
	

	/**
	 * 
	 * @param id
	 * @param name
	 * @param startEnabled
	 * @param startVisible
	 * @param orderLayer
	 * @param minimumMapScale
	 * @param maximumMapScale
	 * @param dataSource
	 * @param layerGroup
	 * @param publishedLayerId
	 */
	public Layer(Long id, String name, Boolean startEnabled,
			Boolean startVisible, Integer orderLayer, MapScale minimumMapScale,
			MapScale maximumMapScale, DataSource dataSource,
			LayerGroup layerGroup, Long publishedLayerId)
	{
		this.setId(id);
		this.setName(name);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setOrderLayer(orderLayer);
		this.setMinimumScaleMap(minimumMapScale);
		this.setMaximumScaleMap(maximumMapScale);
		this.setDataSource(dataSource);
		this.setLayerGroup(layerGroup);
		
		this.setPublishedLayer(new Layer(publishedLayerId));
	}

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Valida os atributos da camada
	 * Verifica se não existem atributos nulos
	 */
	public void validate()
	{
		for (Attribute attribute : attributes)
		{
			attribute.validate();
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getLegend()
	{
		if (this.legend != null && this.legend.contains("&authkey="))
		{
			this.legend = this.legend.replace(
					this.legend.substring(this.legend.indexOf("&authkey="),
							this.legend.length()),
					"");
		}
		if (this.dataSource != null && this.dataSource.getToken() != null)
		{
			return this.legend + "&authkey=" + this.dataSource.getToken();
		}
		return legend;
	}

	/**
	 * 
	 * @param legend
	 */
	public void setLegend(String legend)
	{
		if (this.legend != null && this.legend.contains("&authkey="))
		{
			this.legend = this.legend.replace(
					this.legend.substring(this.legend.indexOf("&authkey="),
							this.legend.length()),
					"");
		}
		if (legend != null && legend.contains("&authkey="))
		{
			legend = legend.replace(legend.substring(
					legend.indexOf("&authkey="), legend.length()), "");
		}
		if (this.dataSource != null && this.dataSource.getToken() != null)
		{
			legend += "&authkey=" + this.dataSource.getToken();
		}
		this.legend = legend;
	}

	/**
	 * 
	 * @param startEnabled
	 */
	public void setStartEnabled(Boolean startEnabled)
	{
		if (startEnabled == null)
		{
			startEnabled = false;
		}
		this.startEnabled = startEnabled;
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getStartEnabled()
	{
		if (startEnabled == null)
		{
			startEnabled = false;
		}
		return startEnabled;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getEnabled()
	{
		if (enabled == null)
		{
			enabled = false;
		}
		return enabled;
	}

	/**
	 * 
	 * @param enabled
	 */
	public void setEnabled(Boolean enabled)
	{
		if (enabled == null)
		{
			enabled = false;
		}
		this.enabled = enabled;
	}
	/**
	 * 
	 * @param startVisible
	 */
	public void setStartVisible(Boolean startVisible)
	{
		if (startVisible == null)
		{
			startVisible = false;
		}
		this.startVisible = startVisible;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getStartVisible()
	{
		if (startVisible == null)
		{
			startVisible = false;
		}
		return startVisible;
	}
	
	/**
	 * @return the order
	 */
	public Integer getOrderLayer()
	{
		if (orderLayer == null)
		{
			orderLayer = 0;
		}
		return orderLayer;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrderLayer(Integer orderLayer)
	{
		if (orderLayer == null)
		{
			orderLayer = 0;
		}
		this.orderLayer = orderLayer;
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getPublished()
	{
		if (published == null)
		{
			published = false;
		}
		return published;
	}

	/**
	 * @param published
	 *            the published to set
	 */
	public void setPublished(Boolean published)
	{
		if (published == null)
		{
			published = false;
		}
		this.published = published;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<? extends ITreeNode> getNodes()
	{
		return null;
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @param maximumScaleMap
	 */
	public void setMaximumScaleMap(MapScale maximumScaleMap)
	{
		this.maximumScaleMap = maximumScaleMap;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the minimumScaleMap
	 */
	public MapScale getMinimumScaleMap()
	{
		return minimumScaleMap;
	}

	/**
	 * @param minimumScaleMap
	 *            the minimumScaleMap to set
	 */
	public void setMinimumScaleMap(MapScale minimumScaleMap)
	{
		this.minimumScaleMap = minimumScaleMap;
	}

	/**
	 * @return the maximumScaleMap
	 */
	public MapScale getMaximumScaleMap()
	{
		return maximumScaleMap;
	}

	/**
	 * @param maximumScaleMap
	 *            the maximumScaleMap to set
	 */
	public void setMafindLayerByIdximumScaleMap(MapScale maximumScaleMap)
	{
		this.maximumScaleMap = maximumScaleMap;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource()
	{
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * @return the layerGroup
	 */
	public LayerGroup getLayerGroup()
	{
		return layerGroup;
	}

	/**
	 * @param layerGroup
	 *            the layerGroup to set
	 */
	public void setLayerGroup(LayerGroup layerGroup)
	{
		this.layerGroup = layerGroup;
	}

	/**
	 * @return the publishedLayer
	 */
	public Layer getPublishedLayer()
	{
		return publishedLayer;
	}

	/**
	 * @param publishedLayer
	 *            the publishedLayer to set
	 */
	public void setPublishedLayer(Layer publishedLayer)
	{
		this.publishedLayer = publishedLayer;
	}

	/**
	 * 
	 * @return
	 */
	public List<Attribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<Attribute> attributes)
	{
		this.attributes = attributes;
	}

	/**
	 * @return the icon
	 */
	public String getIcon()
	{
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	/**
	 * @return the markers
	 */
	public List<Marker> getMarkers()
	{
		return markers;
	}

	/**
	 * 
	 * @param markers
	 */
	public void setMarkers(List<Marker> markers)
	{
		this.markers = markers;
	}

}
