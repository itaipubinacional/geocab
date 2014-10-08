package br.com.geocab.entity;

/**
 * Created by Vinicius on 25/09/2014.
 */
public class Layer
{

    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

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
     * {@link LayerGroup} of {@link Layer}
     */
    private LayerGroup layerGroup;
    /**
     * Draft {@link Layer} that originated the published {@link Layer}
     */
    private Layer publishedLayer;

    /**
     * Field that informs if the {@link Layer} is published
     */
    private Boolean published;

    /**
     * Icon of {@link Layer}
     */
    private int icon;

    /*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    public Layer(String name, String title, String legend, boolean startEnabled, boolean startVisible, int orderLayer, MapScale minimumScaleMap, MapScale maximumScaleMap,
                 DataSource dataSource, LayerGroup layerGroup, Layer publishedLayer, Boolean published, int icon) {
        this.name = name;
        this.title = title;
        this.legend = legend;
        this.startEnabled = startEnabled;
        this.startVisible = startVisible;
        this.orderLayer = orderLayer;
        this.minimumScaleMap = minimumScaleMap;
        this.maximumScaleMap = maximumScaleMap;
        this.dataSource = dataSource;
        this.layerGroup = layerGroup;
        this.publishedLayer = publishedLayer;
        this.published = published;
        this.icon = icon;
    }

    public Layer(String name, String title, int icon)
    {
        this.name = name;
        this.title = title;
        this.icon = icon;
    }

    /*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

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

    public LayerGroup getLayerGroup() {
        return layerGroup;
    }

    public void setLayerGroup(LayerGroup layerGroup) {
        this.layerGroup = layerGroup;
    }

    public Layer getPublishedLayer() {
        return publishedLayer;
    }

    public void setPublishedLayer(Layer publishedLayer) {
        this.publishedLayer = publishedLayer;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
