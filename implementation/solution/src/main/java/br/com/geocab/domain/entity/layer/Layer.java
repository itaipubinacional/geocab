/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.marker.Marker;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Vinicius Ramos Kawamoto 
 * @since 19/09/2014
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject(javascript="Layer")
@Table(schema=IEntity.SCHEMA)
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
	@Column(nullable=false, length=144)
	private String name;
	/**
	 * Title {@link Layer}
	 */
	@NotEmpty
	@Column(nullable=false, length=144)
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
	private boolean startEnabled;
	/**
	 * Indicates that the {@link Layer} will be visible in the layer menu
	 */
	@Basic
	private boolean startVisible;
	/**
	 * Order of {@link Layer}
	 */
	@Column
	private int orderLayer;
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
	 * status
	 */
	private Boolean enabled;
	
	/**
	 * {@link DataSource} of {@link Layer}
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	private DataSource dataSource;
	/**
	 * {@link LayerGroup} of {@link Layer}
	 */
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	private LayerGroup layerGroup;
	/**
	 * Draft {@link Layer} that originated the published {@link Layer}
	 */
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER, optional=true, cascade={CascadeType.ALL})
	private Layer publishedLayer;
	
	@JsonIgnore
	@OneToMany(mappedBy="layer", fetch=FetchType.EAGER, cascade={CascadeType.REMOVE})
	private List<Marker> markers = new ArrayList<Marker>();
	
	@JsonIgnore
	@OneToMany(mappedBy="layer", fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	private List<Attribute> attributes = new ArrayList<Attribute>();

	
	/**
	 * Field that informs if the {@link Layer} is published
	 */
	@Column
	private Boolean published;
	
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
	 *
	 * @param id
	 */
	public Layer( Long id )
	{
		this.setId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param order
	 */
	public Layer( Long id, String name, int orderLayer )
	{
		this.setId(id);
		this.setName(name);
		this.setOrderLayer(orderLayer);
	}
	
	
	/**
	 * 
	 * @param id
	 * @param title
	 */
	public Layer( Long id, String title )
	{
		this.setId(id);
		this.setTitle(title);
	}
	
	/**
	 * 
	 * @param id
	 * @param title
	 * @param group
	 */
	public Layer( Long id, String title, String icon, LayerGroup group )
	{
		this.setId(id);
		this.setTitle(title);
		this.setLayerGroup(group);
		this.setIcon(icon);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param startEnabled
	 * @param startVisible
	 * @param order
	 * @param minimumMapScale
	 * @param maximumMapScale
	 * @param dataSource
	 * @param layerGroup
	 */
	public Layer( Long id, String name, String title, Boolean startEnabled, Boolean startVisible, int orderLayer, MapScale minimumMapScale, MapScale maximumMapScale, Boolean enabled, DataSource dataSource,
			LayerGroup layerGroup )
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setOrderLayer(orderLayer);
		this.setMinimumScaleMap(maximumMapScale);
		this.setMaximumScaleMap(maximumMapScale);
		this.setEnabled(enabled);
		this.setDataSource(dataSource);
		this.setLayerGroup(layerGroup);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param icon
	 * @param title
	 * @param startEnabled
	 * @param startVisible
	 * @param orderLayer
	 * @param minimumMapScale
	 * @param maximumMapScale
	 * @param enabled
	 * @param dataSource
	 * @param layerGroup
	 */
	public Layer( Long id, String name, String icon, String title, Boolean startEnabled, Boolean startVisible, int orderLayer, MapScale minimumMapScale, MapScale maximumMapScale, Boolean enabled, DataSource dataSource,
			LayerGroup layerGroup )
	{
		this.setId(id);
		this.setName(name);
		this.setTitle(title);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setOrderLayer(orderLayer);
		this.setMinimumScaleMap(maximumMapScale);
		this.setMaximumScaleMap(maximumMapScale);
		this.setEnabled(enabled);
		this.setDataSource(dataSource);
		this.setLayerGroup(layerGroup);
		this.setIcon(icon);
	}

	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param startEnabled
	 * @param startVisible
	 * @param order
	 * @param minimumMapScale
	 * @param maximumMapScale
	 * @param dataSource
	 * @param layerGroup
	 */
	public Layer( Long id, String name, Boolean startEnabled, Boolean startVisible, int orderLayer, MapScale minimumMapScale, MapScale maximumMapScale, DataSource dataSource, LayerGroup layerGroup )
	{
		this.setId(id);
		this.setName(name);
		this.setStartEnabled(startEnabled);
		this.setStartVisible(startVisible);
		this.setOrderLayer(orderLayer);
		this.setMinimumScaleMap(maximumMapScale);
		this.setMaximumScaleMap(maximumMapScale);
		this.setDataSource(dataSource);
		this.setLayerGroup(layerGroup);
	}
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dataSource == null) ? 0 : dataSource.hashCode());
		result = prime * result
				+ ((layerGroup == null) ? 0 : layerGroup.hashCode());
		result = prime * result + ((legend == null) ? 0 : legend.hashCode());
		result = prime * result
				+ ((maximumScaleMap == null) ? 0 : maximumScaleMap.hashCode());
		result = prime * result
				+ ((minimumScaleMap == null) ? 0 : minimumScaleMap.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + orderLayer;
		result = prime * result
				+ ((published == null) ? 0 : published.hashCode());
		result = prime * result
				+ ((publishedLayer == null) ? 0 : publishedLayer.hashCode());
		result = prime * result + (startEnabled ? 1231 : 1237);
		result = prime * result + (startVisible ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Layer other = (Layer) obj;
		if (dataSource == null)
		{
			if (other.dataSource != null) return false;
		}
		else if (!dataSource.equals(other.dataSource)) return false;
		if (layerGroup == null)
		{
			if (other.layerGroup != null) return false;
		}
		else if (!layerGroup.equals(other.layerGroup)) return false;
		if (legend == null)
		{
			if (other.legend != null) return false;
		}
		else if (!legend.equals(other.legend)) return false;
		if (maximumScaleMap != other.maximumScaleMap) return false;
		if (minimumScaleMap != other.minimumScaleMap) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		if (orderLayer != other.orderLayer) return false;
		if (published == null)
		{
			if (other.published != null) return false;
		}
		else if (!published.equals(other.published)) return false;
		if (publishedLayer == null)
		{
			if (other.publishedLayer != null) return false;
		}
		else if (!publishedLayer.equals(other.publishedLayer)) return false;
		if (startEnabled != other.startEnabled) return false;
		if (startVisible != other.startVisible) return false;
		if (title == null)
		{
			if (other.title != null) return false;
		}
		else if (!title.equals(other.title)) return false;
		return true;
	}
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	@Override
	public List<? extends ITreeNode> getNodes()
	{
		return null;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the legend
	 */
	public String getLegend()
	{
		return legend;
	}

	/**
	 * @param legend the legend to set
	 */
	public void setLegend(String legend)
	{
		this.legend = legend;
	}

	/**
	 * @return the startEnabled
	 */
	public boolean isStartEnabled()
	{
		return startEnabled;
	}

	/**
	 * @param startEnabled the startEnabled to set
	 */
	public void setStartEnabled(boolean startEnabled)
	{
		this.startEnabled = startEnabled;
	}

	/**
	 * @return the startVisible
	 */
	public boolean isStartVisible()
	{
		return startVisible;
	}

	/**
	 * @param startVisible the startVisible to set
	 */
	public void setStartVisible(boolean startVisible)
	{
		this.startVisible = startVisible;
	}

	/**
	 * @return the order
	 */
	public int getOrderLayer()
	{
		return orderLayer;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrderLayer(int orderLayer)
	{
		this.orderLayer = orderLayer;
	}

	/**
	 * @return the minimumScaleMap
	 */
	public MapScale getMinimumScaleMap()
	{
		return minimumScaleMap;
	}

	/**
	 * @param minimumScaleMap the minimumScaleMap to set
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
	 * @param maximumScaleMap the maximumScaleMap to set
	 */
	public void setMaximumScaleMap(MapScale maximumScaleMap)
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
	 * @param dataSource the dataSource to set
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
	 * @param layerGroup the layerGroup to set
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
	 * @param publishedLayer the publishedLayer to set
	 */
	public void setPublishedLayer(Layer publishedLayer)
	{
		this.publishedLayer = publishedLayer;
	}

	/**
	 * @return the published
	 */
	public Boolean getPublished()
	{
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(Boolean published)
	{
		this.published = published;
	}

	/**
	 * @return the attributes
	 */
	public List<Attribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
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
	 * @param icon the icon to set
	 */
	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	/**
	 * @return the enabled
	 */
	public Boolean getEnabled()
	{
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * @return the markers
	 */
	public List<Marker> getMarkers()
	{
		return markers;
	}

	/**
	 * @param markers the markers to set
	 */
	public void setMarkers(List<Marker> markers)
	{
		this.markers = markers;
	}
	
}
