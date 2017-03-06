/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.layer;

import br.gov.itaipu.geocab.domain.entity.AbstractEntity;
import br.gov.itaipu.geocab.domain.entity.marker.MarkerAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @since 02/10/2014
 */
@Entity
public class Attribute extends AbstractEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 754889878712215160L;

	/*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    @Transient
    private Long temporaryId;

    /**
     * Name {@link Attribute}
     */
    @NotNull(message = "admin.layer-config.The-all-fields-name-in-attributes-must-be-set")
    private String name;

    /**
     * Type {@link Attribute}
     */
    @NotNull(message = "admin.layer-config.The-all-fields-type-in-attributes-must-be-set")
    private AttributeType type;

    /**
     * Required {@link Attribute}
     */
    @NotNull
    private Boolean required;

    /**
     * Visible {@link Attribute}
     */
    @NotNull
    private Boolean visible;

    /**
     *
     */
    private Boolean attributeDefault;

    /**
     * Order {@link Attribute}
     */
    @Column
    private Integer orderAttribute;

    /**
     * Layer {@link Layer}
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Layer layer;

    /**
     *
     */
    @JsonIgnore
//	@OneToMany(mappedBy = "attribute", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
    @Transient
    private List<MarkerAttribute> markerAttribute = new ArrayList<MarkerAttribute>();

	/*-------------------------------------------------------------------
     *								CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public Attribute() {

    }

    /**
     * @param id
     */
    public Attribute(Long id) {
        this.setId(id);
    }

    /**
     * @param id
     * @param name
     * @param type
     * @param required
     * @param orderAttribute
     * @param visible
     */
    public Attribute(Long id, String name, AttributeType type, Boolean required,
                     Integer orderAttribute, Boolean visible) {
        this.setId(id);
        this.setType(type);
        this.setName(name);
        this.setRequired(required);
        this.setOrderAttribute(orderAttribute);
        this.setVisible(visible);
    }

    /**
     * @param name
     */
    public Attribute(String name) {
        this.setName(name);
    }

    /**
     * @param id
     * @param name
     * @param required
     * @param type
     * @param orderAttribute
     * @param visible
     */
    public Attribute(Long id, String name, Boolean required, AttributeType type, Integer orderAttribute, Boolean visible) {
        this.setId(id);
        this.setTemporaryId(id);
        this.setType(type);
        this.setName(name);
        this.setRequired(required);
        this.setOrderAttribute(orderAttribute);
        this.setVisible(visible);
    }

    /**
     * @param id
     * @param name
     * @param type
     * @param layer
     */
    public Attribute(Long id, String name, AttributeType type, Layer layer) {
        this.setId(id);
        this.setType(type);
        this.setName(name);
        this.setLayer(layer);
    }

    /**
     * @param id
     * @param name
     * @param type
     * @param layer
     */
    public Attribute(Long id, String name, String type, Layer layer) {
        this.setId(id);
        this.setType(formmatAttributes(type));
        this.setName(name);
        this.setLayer(layer);
    }

	/*-------------------------------------------------------------------
	 *								BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * Formata os atributos para importação
     *
     * @return
     */
    public void validate() {
        Assert.notNull(this.getType(), "admin.layer-config.The-all-fields-type-in-attributes-must-be-set");
    }

    /**
     * Formata os atributos para importação
     *
     * @return
     */
    public String formmattedTypeAttributes() {
        if (this.getType() == AttributeType.TEXT) {
            return "String";
        } else if (this.getType() == AttributeType.DATE) {
            return "Date";
        } else if (this.getType() == AttributeType.BOOLEAN) {
            return "Boolean";
        } else if (this.getType() != AttributeType.PHOTO_ALBUM) {
            return "Integer";
        } else // Se for do tipo PHOTO_ALBUM retorna null
        {
            return null;
        }
    }

    /**
     * Formata o nome do atributo para exportação (A documentação informa que o nome não pode ser maior que 15 caracteres)
     *
     * @return
     */
    public void formmatNameAttribute() {
        if (this.getName().length() >= 10) {
            this.setName(this.getName().substring(0, 5) + "...");
        }
    }

    /**
     * Formata os atributos para exportação
     *
     * @param type
     * @return
     */
    private static AttributeType formmatAttributes(String type) {
        AttributeType attributeType = null;
        if (type.contains("java.lang.String")) {
            attributeType = AttributeType.TEXT;
        } else if (type.contains("java.lang.Integer") || type.contains("java.lang.Long")) {
            attributeType = AttributeType.NUMBER;
        } else if (type.contains("java.util.Date")) {
            attributeType = AttributeType.DATE;
        } else if (type.contains("java.lang.Boolean")) {
            attributeType = AttributeType.BOOLEAN;
        }
        return attributeType;
    }

    /**
     * @return the required
     */
    public Boolean getRequired() {
        if (required == null) {
            required = false;
        }
        return required;
    }

    /**
     * @param required the required to set
     */
    public void setRequired(Boolean required) {
        if (required == null) {
            required = false;
        }
        this.required = required;
    }

    /**
     * @return the visible
     */
    public Boolean getVisible() {
        if (visible == null) {
            visible = false;
        }
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(Boolean visible) {
        if (visible == null) {
            visible = false;
        }
        this.visible = visible;
    }

	/*-------------------------------------------------------------------
	 *								SETTERS/GETTERS
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
     * @return the type
     */
    public AttributeType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AttributeType type) {
        this.type = type;
    }

    /**
     * @return the layer
     */
    public Layer getLayer() {
        return layer;
    }

    /**
     * @param layer the layer to set
     */
    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    /**
     * @return the attributeDefault
     */
    public Boolean getAttributeDefault() {
        return attributeDefault;
    }

    /**
     * @param attributeDefault the attributeDefault to set
     */
    public void setAttributeDefault(Boolean attributeDefault) {
        this.attributeDefault = attributeDefault;
    }

    /**
     * @return the temporaryId
     */
    public Long getTemporaryId() {
        return temporaryId;
    }

    /**
     * @param temporaryId the temporaryId to set
     */
    public void setTemporaryId(Long temporaryId) {
        this.temporaryId = temporaryId;
    }

    /**
     * @return the orderAttribute
     */
    public Integer getOrderAttribute() {
        return orderAttribute;
    }

    /**
     * @param orderAttribute the orderAttribute to set
     */
    public void setOrderAttribute(Integer orderAttribute) {
        this.orderAttribute = orderAttribute;
    }
}
