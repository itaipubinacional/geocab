/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.layer;

import br.gov.itaipu.geocab.domain.entity.AbstractEntity;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category Entity
 * @since 19/09/2014
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames =
        {"name", "layer_group_upper_id"}))
public class LayerGroup extends AbstractEntity
        implements Serializable, TreeNode {

    /**
     *
     */
    private static final long serialVersionUID = -8774128410822834963L;

	/*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     * nome do {@link LayerGroup}
     */
    @NotEmpty
    @Column(nullable = false, length = 144)
    private String name;
    /**
     *
     */
    @Column
    private Integer orderLayerGroup;
    /**
     * {@link LayerGroup} upper than the current {@link LayerGroup}
     */
    @ManyToOne
    private LayerGroup layerGroupUpper;
    /**
     * {@link LayerGroup} of {@link Layer}
     */
    @OrderBy(value = "orderLayer")
    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "layer_group_id")
    @JsonIgnore
    private List<Layer> layers = new LinkedList<Layer>();

    /**
     * {@link LayerGroup} of {@link LayerGroup}
     */
    @OrderBy(value = "orderLayerGroup")
    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "layer_group_upper_id")    
    private List<LayerGroup> layersGroup = new LinkedList<LayerGroup>();

    /**
     * Draft {@link LayerGroup} that originated the published {@link LayerGroup}
     */
    @OneToOne
    private LayerGroup draft;

    /**
     * Field that informs if the {@link LayerGroup} is published
     */
    @Column
    private Boolean published;

    /**
     * Informa se a camada tem filhos ou n�o (sejam outros grupos de camadas ou
     * outras camadas)
     */
    @Transient
    private Boolean hasChildren;

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public LayerGroup() {

    }

    /**
     * @param id
     */
    public LayerGroup(Long id) {
        this.setId(id);
    }

    /**
     * @param id
     */
    public LayerGroup(Long id, LayerGroup draft) {
        this.setId(id);
        this.setDraft(draft);
    }

    /**
     * @param id
     * @param name
     * @param layerGroupUpper
     */
    public LayerGroup(Long id, String name, LayerGroup layerGroupUpper) {
        this.setId(id);
        this.setName(name);
        this.setLayerGroupUpper(layerGroupUpper);
    }

    /**
     * @param id
     * @param name
     */
    public LayerGroup(Long id, String name) {
        this.setId(id);
        this.setName(name);
    }

    /**
     *
     */
    public LayerGroup(Long id, String name, Integer orderLayerGroup) {
        this.setId(id);
        this.setName(name);
        this.setOrderLayerGroup(orderLayerGroup);
    }

    /**
     * @param id
     * @param name
     * @param orderLayerGroup
     * @param published
     * @param layerGroupUpperId
     */
    public LayerGroup(Long id, String name, Integer orderLayerGroup,
                      Boolean published, Long layerGroupUpperId) {
        this.setId(id);
        this.setName(name);
        this.setOrderLayerGroup(orderLayerGroup);
        this.setPublished(published);
        this.setLayerGroupUpper(layerGroupUpperId != null
                ? new LayerGroup(layerGroupUpperId) : null);
    }

	/*-------------------------------------------------------------------
     *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @return the published
     */
    public Boolean getPublished() {
        if (published == null) {
            published = false;
        }
        return published;
    }

    /**
     * @param published the published to set
     */
    public void setPublished(Boolean published) {
        if (published == null) {
            published = false;
        }
        this.published = published;
    }

    /**
     * @return the orderLayerGroup
     */
    public Integer getOrderLayerGroup() {
        if (orderLayerGroup == null) {
            orderLayerGroup = 0;
        }
        return orderLayerGroup;
    }

    /**
     * @param orderLayerGroup the orderLayerGroup to set
     */
    public void setOrderLayerGroup(Integer orderLayerGroup) {
        if (orderLayerGroup == null) {
            orderLayerGroup = 0;
        }
        this.orderLayerGroup = orderLayerGroup;
    }

    /**
     * @return the hasChildren
     */
    public Boolean getHasChildren() {
        if (hasChildren == null) {
            hasChildren = (this.getLayers() != null
                    && this.getLayers().size() > 0)
                    || (this.getLayersGroup() != null
                    && this.getLayersGroup().size() > 0);
        }
        return hasChildren;
    }

    /**
     * @param hasChildren the hasChildren to set
     */
    public void setHasChildren(Boolean hasChildren) {
        if (hasChildren == null) {
            hasChildren = (this.getLayers() != null
                    && this.getLayers().size() > 0)
                    || (this.getLayersGroup() != null
                    && this.getLayersGroup().size() > 0);
        }
        this.hasChildren = hasChildren;
    }

    /*
     * (non-Javadoc)
     * @see br.com.geocab.domain.entity.layer.TreeNode#getNodes()
     */
    @Override
    public List<? extends TreeNode> getNodes() {
        if (layersGroup != null && !layersGroup.isEmpty()) {
            return this.layersGroup;
        } else {
            return this.layers;
        }
    }

    /* (non-Javadoc)
     * @see br.com.geocab.domain.entity.layer.TreeNode#getTitle()
     */
    @Override
    public String getTitle() {
        return this.getName();
    }
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the layerGroupUpper
     */
    public LayerGroup getLayerGroupUpper() {
        return layerGroupUpper;
    }

    /**
     * @param layerGroupUpper the layerGroupUpper to set
     */
    public void setLayerGroupUpper(LayerGroup layerGroupUpper) {
        this.layerGroupUpper = layerGroupUpper;
    }

    /**
     * @return the layers
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * @param layers the layers to set
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    /**
     * @return the layersGroup
     */
    public List<LayerGroup> getLayersGroup() {
        return layersGroup;
    }

    /**
     * @param layersGroup the layersGroup to set
     */
    public void setLayersGroup(List<LayerGroup> layersGroup) {
        this.layersGroup = layersGroup;
    }

    /**
     * @return the draft
     */
    public LayerGroup getDraft() {
        return draft;
    }

    /**
     * @param draft the draft to set
     */
    public void setDraft(LayerGroup draft) {
        this.draft = draft;
    }


}
