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
<<<<<<< HEAD
	private Boolean startEnabled;
=======
	private Boolean startEnabled = false;
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	/**
	 * Indicates that the {@link Layer} will be visible in the layer menu
	 */
	@Basic
<<<<<<< HEAD
	private Boolean startVisible;
=======
	private Boolean startVisible = false;
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26

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
<<<<<<< HEAD
	@ManyToOne(fetch = FetchType.EAGER)
=======
	@ManyToOne
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	private DataSource dataSource;
	/**
	 * {@link LayerGroup} of {@link Layer}
	 */
	@JsonIgnore
<<<<<<< HEAD
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
=======
	@ManyToOne
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	private LayerGroup layerGroup;
	/**
	 * Draft {@link Layer} that originated the published {@link Layer}
	 */
	@JsonIgnore
<<<<<<< HEAD
	@OneToOne(fetch = FetchType.EAGER, optional = true, cascade =
	{ CascadeType.ALL })
	private Layer publishedLayer;

	@JsonIgnore
	@OneToMany(mappedBy = "layer", fetch = FetchType.EAGER, cascade =
	{ CascadeType.REMOVE })
	private List<Marker> markers = new ArrayList<Marker>();

	@JsonIgnore
	@OneToMany(mappedBy = "layer", fetch = FetchType.EAGER, cascade =
	{ CascadeType.ALL })
=======
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
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
<<<<<<< HEAD
	public Layer(Long id, String name, Integer orderLayer)
=======
	public Layer(Long id, String name, Integer orderLayer, String icon, Long publishedLayerId)
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	{
		
		this.setId(id);
		this.setName(name);
		this.setOrderLayer(orderLayer);
		this.setIcon(icon);
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
	}
<<<<<<< HEAD
=======
	
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
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
	}
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26

	/**
	 * 
	 * @param id
	 * @param title
	 * @param publishedLayerId
	 */
<<<<<<< HEAD
	public Layer(Long id, String title)
=======
	public Layer(Long id, String title, Long publishedLayerId)
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	{
		this.setId(id);
		this.setTitle(title);
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
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
<<<<<<< HEAD
			Boolean published, DataSource dataSource)
=======
			Boolean published, DataSource dataSource, Long publishedLayerId)
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
<<<<<<< HEAD
=======
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
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
		
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param title
	 * @param icon
<<<<<<< HEAD
	 * @param layerGroupName
=======
	 * @param startEnabled
	 * @param startVisible
	 * @param enabled
	 * @param published
	 * @param layerGroupId
	 * @param layerGroupName
	 * @param orderLayerGroup
	 * @param dataSource
	 * @param publishedLayerId
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	 */
	public Layer(Long id, String name, String title, String icon,
			Boolean startEnabled, Boolean startVisible, Boolean enabled,
			Boolean published, Long layerGroupId, String layerGroupName,
<<<<<<< HEAD
			Integer orderLayerGroup)
=======
			Integer orderLayerGroup, DataSource dataSource, Long publishedLayerId)
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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

<<<<<<< HEAD
		this.setLayerGroup(layerGroup);
	}

	/**
	 * 
	 * @param id
	 * @param title
	 * @param icon
	 * @param layerGroupId
	 * @param layerGroupName
	 * @param orderLayerGroup
	 * @param dataSource
	 */
	public Layer(Long id, String name, String title, String icon,
			Boolean startEnabled, Boolean startVisible, Boolean enabled,
			Boolean published, Long layerGroupId, String layerGroupName,
			Integer orderLayerGroup, DataSource dataSource)
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

	}

=======
		this.setDataSource(dataSource);

		this.setLayerGroup(layerGroup);
		
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
	}

>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
<<<<<<< HEAD
	public Layer(Long id, String name, String title, Boolean startEnabled,
			Boolean startVisible, Integer orderLayer, MapScale minimumMapScale,
			MapScale maximumMapScale, Boolean enabled, DataSource dataSource,
			LayerGroup layerGroup)
=======
	public Layer(Long id, String name, String title, 
			Boolean startEnabled, Boolean startVisible, Integer orderLayer, 
			MapScale minimumMapScale, MapScale maximumMapScale, Boolean enabled, 
			DataSource dataSource,	LayerGroup layerGroup, Long publishedLayerId)
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
		
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
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
		
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
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
<<<<<<< HEAD
			DataSource dataSource, LayerGroup layerGroup)
=======
			DataSource dataSource, String layerGroupName, Long publishedLayerId)
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
		
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
	}
<<<<<<< HEAD

=======
	
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
		
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
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
<<<<<<< HEAD
			LayerGroup layerGroup)
=======
			LayerGroup layerGroup, Long publishedLayerId)
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
				
		this.setPublishedLayer(publishedLayerId != null ? new Layer(publishedLayerId) : null);
		
	}

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
<<<<<<< HEAD
	 * @return the legend
	 */
	public String getLegend()
	{
=======
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
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
<<<<<<< HEAD
	 * @param legend
	 *            the legend to set
=======
	 * 
	 * @param legend
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
<<<<<<< HEAD
	 * @return the startEnabled
	 */
	public Boolean isStartEnabled()
	{
		if (startEnabled == null)
		{
			this.startEnabled = false;
		}
		return startEnabled;
	}

	/**
	 * @param startEnabled
	 *            the startEnabled to set
	 */
	public void setStartEnabled(Boolean startEnabled)
	{
		if (startEnabled == null)
		{
			this.startEnabled = false;
		}
		this.startEnabled = startEnabled;
	}

	/**
	 * @return the published
	 */
	public Boolean getPublished()
	{
		if (published == null)
		{
			this.published = false;
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
			this.published = false;
		}
		this.published = published;
=======
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
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	}
	/**
<<<<<<< HEAD
	 * @return the enabled
	 */
	public Boolean getEnabled()
	{
		if (enabled == null)
		{
			this.enabled = false;
		}
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
		if (enabled == null)
		{
			this.enabled = false;
		}
=======
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
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	}
	
	/**
<<<<<<< HEAD
	 * @return the startVisible
	 */
	public Boolean isStartVisible()
	{
		if (startVisible == null)
		{
			this.startVisible = false;
		}
		return startVisible;
	}

	/**
	 * @param startVisible
	 *            the startVisible to set
	 */
	public void setStartVisible(Boolean startVisible)
	{
		if (startVisible == null)
		{
			this.startVisible = false;
		}
		this.startVisible = startVisible;
	}
	
	/**
	 * @return the startEnabled
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
	 * @return the startVisible
	 */
	public Boolean getStartVisible()
	{
		if (startVisible == null)
		{
			startVisible = false;
		}
		return startVisible;
=======
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
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	/**
<<<<<<< HEAD
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
=======
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
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	/**
<<<<<<< HEAD
	 * @param maximumScaleMap the maximumScaleMap to set
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
	 * @return the title
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
=======
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
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	}

	/**
	 * 
	 * @return
	 */
<<<<<<< HEAD
	public Integer getOrderLayer()
=======
	public String getTitle()
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	{
		return title;
	}

	/**
<<<<<<< HEAD
	 * @param order
	 *            the order to set
	 */
	public void setOrderLayer(Integer orderLayer)
=======
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
<<<<<<< HEAD
	 * @return the attributes
=======
	 * 
	 * @return
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
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
<<<<<<< HEAD
	 */
	public List<Marker> getMarkers()
	{
		return markers;
	}

	/**
	 * @param markers
	 *            the markers to set
	 */
	public void setMarkers(List<Marker> markers)
	{
		this.markers = markers;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
=======
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result
				+ ((dataSource == null) ? 0 : dataSource.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result
				+ ((layerGroup == null) ? 0 : layerGroup.hashCode());
		result = prime * result + ((legend == null) ? 0 : legend.hashCode());
		result = prime * result + ((markers == null) ? 0 : markers.hashCode());
		result = prime * result
				+ ((maximumScaleMap == null) ? 0 : maximumScaleMap.hashCode());
		result = prime * result
				+ ((minimumScaleMap == null) ? 0 : minimumScaleMap.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((orderLayer == null) ? 0 : orderLayer.hashCode());
		result = prime * result
				+ ((published == null) ? 0 : published.hashCode());
		result = prime * result
				+ ((publishedLayer == null) ? 0 : publishedLayer.hashCode());
		result = prime * result
				+ ((startEnabled == null) ? 0 : startEnabled.hashCode());
		result = prime * result
				+ ((startVisible == null) ? 0 : startVisible.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

<<<<<<< HEAD
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
=======
	/**
	 * 
	 * @param markers
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Layer other = (Layer) obj;
		if (attributes == null)
		{
			if (other.attributes != null) return false;
		}
		else if (!attributes.equals(other.attributes)) return false;
		if (dataSource == null)
		{
			if (other.dataSource != null) return false;
		}
		else if (!dataSource.equals(other.dataSource)) return false;
		if (enabled == null)
		{
			if (other.enabled != null) return false;
		}
		else if (!enabled.equals(other.enabled)) return false;
		if (icon == null)
		{
			if (other.icon != null) return false;
		}
		else if (!icon.equals(other.icon)) return false;
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
		if (markers == null)
		{
			if (other.markers != null) return false;
		}
		else if (!markers.equals(other.markers)) return false;
		if (maximumScaleMap != other.maximumScaleMap) return false;
		if (minimumScaleMap != other.minimumScaleMap) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		if (orderLayer == null)
		{
			if (other.orderLayer != null) return false;
		}
		else if (!orderLayer.equals(other.orderLayer)) return false;
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
		if (startEnabled == null)
		{
			if (other.startEnabled != null) return false;
		}
		else if (!startEnabled.equals(other.startEnabled)) return false;
		if (startVisible == null)
		{
			if (other.startVisible != null) return false;
		}
		else if (!startVisible.equals(other.startVisible)) return false;
		if (title == null)
		{
			if (other.title != null) return false;
		}
		else if (!title.equals(other.title)) return false;
		return true;
	}

}
