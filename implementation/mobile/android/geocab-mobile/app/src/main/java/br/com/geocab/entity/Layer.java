package br.com.geocab.entity;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Vinicius on 25/09/2014.
 */
public class Layer implements Serializable
{

    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * Id {@link Layer}
     */
    private Long id;
    /**
     * Name {@link Layer}
     */
    private String name;
    /**
     * Title {@link Layer}
     */
    private String title;
    /**
     * Legend {@link Layer}
     */
    private String legend;
    /**
     * Indicates that the {@link Layer} will start enabled on map
     */
    private boolean startEnabled;
    /**
     * Indicates that the {@link Layer} will be visible in the layer menu
     */
    private boolean startVisible;
    /**
     * Order of {@link Layer}
     */
    private int orderLayer;
    /**
     * {@link MapScale} minimum of {@link Layer}
     */
    private MapScale minimumScaleMap;
    /**
     * {@link MapScale} maximum of {@link Layer}
     */
    private MapScale maximumScaleMap;
    /**
     * {@link DataSource} of {@link Layer}
     */
    private DataSource dataSource;
    /**
     * Field that informs if the {@link Layer} is published
     */
    private Boolean published;

    /**
     * Field that informs if the {@link Layer} is checked
     */
    private transient Boolean isChecked = false;

    /**
     * Icon of {@link Layer}
     */
    private String icon;


    /*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    public Layer(Long id, String name, String title, String legend, boolean startEnabled, boolean startVisible, int orderLayer, MapScale minimumScaleMap, MapScale maximumScaleMap,
                 DataSource dataSource, Boolean published, String icon) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.legend = legend;
        this.startEnabled = startEnabled;
        this.startVisible = startVisible;
        this.orderLayer = orderLayer;
        this.minimumScaleMap = minimumScaleMap;
        this.maximumScaleMap = maximumScaleMap;
        this.dataSource = dataSource;
        this.published = published;
        this.isChecked = false;
        this.icon = icon;

    }

    public Layer(String name, String title)
    {
        this.name = name;
        this.title = title;
        this.isChecked = false;
    }

    /*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public boolean isStartEnabled() {
        return startEnabled;
    }

    public void setStartEnabled(boolean startEnabled) {
        this.startEnabled = startEnabled;
    }

    public boolean isStartVisible() {
        return startVisible;
    }

    public void setStartVisible(boolean startVisible) {
        this.startVisible = startVisible;
    }

    public int getOrderLayer() {
        return orderLayer;
    }

    public void setOrderLayer(int orderLayer) {
        this.orderLayer = orderLayer;
    }

    public MapScale getMinimumScaleMap() {
        return minimumScaleMap;
    }

    public void setMinimumScaleMap(MapScale minimumScaleMap) {
        this.minimumScaleMap = minimumScaleMap;
    }

    public MapScale getMaximumScaleMap() {
        return maximumScaleMap;
    }

    public void setMaximumScaleMap(MapScale maximumScaleMap) {
        this.maximumScaleMap = maximumScaleMap;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Layer layer = (Layer) o;

        if (id != null ? !id.equals(layer.id) : layer.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
