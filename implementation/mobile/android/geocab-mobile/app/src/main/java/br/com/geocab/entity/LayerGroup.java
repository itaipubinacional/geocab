package br.com.geocab.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Vinicius on 25/09/2014.
 */
public class LayerGroup
{

    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * Name of {@link LayerGroup}
     */
    private String name;
    /**
     * Order of {@link LayerGroup}
     */
    private int orderLayerGroup;
    /**
     * {@link LayerGroup} upper than the current {@link LayerGroup}
     */
    private LayerGroup layerGroupUpper;
    /**
     * {@link LayerGroup} of {@link Layer}
     */
    private List<Layer> layers = new LinkedList<Layer>();

    /**
     * {@link LayerGroup} of {@link LayerGroup}
     */
    private List<LayerGroup> layersGroup = new LinkedList<LayerGroup>();

    /**
     * Draft {@link LayerGroup} that originated the published {@link LayerGroup}
     */
    private LayerGroup draft;

    /**
     * Field that informs if the {@link LayerGroup} is published
     */
    private Boolean published;

      /*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    public LayerGroup(String name, int orderLayerGroup, LayerGroup layerGroupUpper, List<Layer> layers, List<LayerGroup> layersGroup, LayerGroup draft, Boolean published) {
        this.name = name;
        this.orderLayerGroup = orderLayerGroup;
        this.layerGroupUpper = layerGroupUpper;
        this.layers = layers;
        this.layersGroup = layersGroup;
        this.draft = draft;
        this.published = published;
    }

    /*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderLayerGroup() {
        return orderLayerGroup;
    }

    public void setOrderLayerGroup(int orderLayerGroup) {
        this.orderLayerGroup = orderLayerGroup;
    }

    public LayerGroup getLayerGroupUpper() {
        return layerGroupUpper;
    }

    public void setLayerGroupUpper(LayerGroup layerGroupUpper) {
        this.layerGroupUpper = layerGroupUpper;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public List<LayerGroup> getLayersGroup() {
        return layersGroup;
    }

    public void setLayersGroup(List<LayerGroup> layersGroup) {
        this.layersGroup = layersGroup;
    }

    public LayerGroup getDraft() {
        return draft;
    }

    public void setDraft(LayerGroup draft) {
        this.draft = draft;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}
