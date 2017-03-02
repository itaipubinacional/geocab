/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.marker;

import br.gov.itaipu.geocab.domain.entity.AbstractEntity;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.layer.AttributeType;
import br.gov.itaipu.geocab.domain.entity.layer.Layer;
import br.gov.itaipu.geocab.domain.entity.markermoderation.MarkerModeration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * {@link Marker}
 *
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @category Entity
 * @since 03/10/2014
 */
@Entity
public class Marker extends AbstractEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1806026076674494131L;
    /*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    public static final String PICTURE_FOLDER = "/marker/%d";
    /**
     *
     */
    public static final String PICTURE_PATH = PICTURE_FOLDER + "/%d";
    /**
     *
     */
    @Transient
    private Boolean imageToDelete;
    /**
     *
     */
    @Transient
    private String wktCoordenate;
    /**
     *
     */
    @Column(columnDefinition = "Geometry")
    @JsonIgnore
    private Point location;
    /**
     *
     */
    @NotNull
    private MarkerStatus status;
    /**
     *
     */
    private Boolean deleted;
    /**
     *
     */
    @ManyToOne
    private Layer layer;
    /**
     *
     */
    @ManyToOne
    private User user;
    /**
     *
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "marker", cascade = {CascadeType.ALL})
    private List<MarkerAttribute> markerAttributes = new ArrayList<MarkerAttribute>();
    /**
     *
     */
    @JsonIgnore
    @OneToMany(mappedBy = "marker", cascade = {CascadeType.ALL})
    private List<MarkerModeration> markerModeration = new ArrayList<>();

	/*-------------------------------------------------------------------
     * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public Marker() {
        super();
    }

    /**
     * @param id
     */
    public Marker(Long id) {
        this.setId(id);
    }

    public Marker(Long id, Geometry location, MarkerStatus markerStatus, Boolean markerDeleted, User user) {
        this.setId(id);
        this.setLocation((Point) location);
        this.setStatus(markerStatus);
        this.setDeleted(markerDeleted);
        this.setUser(user);
    }

    /**
     * @param id
     * @param status
     */
    public Marker(Long id, MarkerStatus status) {
        this.setId(id);
        this.setStatus(status);
    }

    /**
     *
     */
    public Marker(Long id, MarkerStatus status, Geometry location, Layer layer) {
        this.setId(id);
        this.setStatus(status);
        this.setLocation((Point) location);
        this.setLayer(layer);
    }

    /**
     *
     */
    public Marker(Long id, MarkerStatus status, Calendar created, Layer layer,
                  User user) {
        this.setId(id);
        this.setStatus(status);
        user.setPassword("");
        //user.setEmail("");
        this.setUser(user);
        this.setLayer(layer);
        this.setCreated(created);
    }

    /**
     *
     */
    public Marker(Long id, MarkerStatus status, Geometry location,
                  Calendar created, Long layerId, String layerName,
                  String layerTitle, String layerIcon, Boolean layerPublished,
                  Boolean layerStartEnable, Boolean layerStartVisible, Boolean layerEnabled,
                  User user) {
        this.setId(id);
        this.setStatus(status);
        user.setPassword("");
        this.setUser(user);
        this.setCreated(created);
        this.setLocation((Point) location);

        Layer layer = new Layer(layerId, layerName, layerTitle, layerIcon,
                layerStartEnable, layerStartVisible, layerStartEnable,
                layerPublished, null, null, null, null);

        this.setLayer(layer);
    }

    /**
     * @param id
     * @param status
     * @param created
     * @param location
     */
    public Marker(Long id, MarkerStatus status, Calendar created, Geometry location) {
        this.setId(id);
        this.setStatus(status);
        user.setPassword("");
        user.setEmail("");
        layer.setMarkers(null);
        this.setLocation((Point) location);
        this.setCreated(created);
    }

    /**
     *
     */
    public Marker(Long id, MarkerStatus status, Calendar created,
                  Geometry location, Layer layer, User user) {
        this.setId(id);
        this.setStatus(status);
        user.setPassword("");
        //	user.setEmail("");
        this.setUser(user);
        layer.setMarkers(null);
        this.setLayer(layer);
        this.setLocation((Point) location);
        this.setCreated(created);
    }

    /**
     * Listagem de Markers
     *
     * @param id
     * @param status
     * @param created
     * @param location
     * @param layerId
     * @param layerName
     * @param user
     */
    public Marker(Long id, MarkerStatus status, Calendar created, Geometry location,
                  Long layerId, String layerName, User user, Long publishedLayerId) {
        this.setId(id);
        this.setStatus(status);
        user.setPassword("");
        this.setUser(user);

        Layer layer = new Layer(layerId, layerName, publishedLayerId);
        this.setLayer(layer);

        this.setLocation((Point) location);
        this.setCreated(created);
    }

    /**
     * @param point
     */
    public Marker(Point point) {
        this.setLocation(point);
    }
	
	/*-------------------------------------------------------------------
	 *								BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * Formata os attributos
     *
     * @return
     */
    public String formattedAttributes() {
        String formattedAttributes = new String();

        for (MarkerAttribute markerAttribute : this.getMarkerAttribute()) {

            if (markerAttribute.getAttribute().getName() != null &&
                    markerAttribute.getAttribute().getType() != AttributeType.PHOTO_ALBUM) {
                if (formattedAttributes.length() > 0) {
                    formattedAttributes += "," + markerAttribute.getAttribute().getName() + ":" +
                            markerAttribute.getAttribute().formmattedTypeAttributes();
                } else {
                    formattedAttributes += markerAttribute.getAttribute().getName() + ":" +
                            markerAttribute.getAttribute().formmattedTypeAttributes();
                }
                // O
                markerAttribute.getAttribute().formmatNameAttribute();
            }
        }

        return formattedAttributes;
    }

    /**
     * Diminui o tamanho de todos os atributos
     */
    public void formattedNameAttributes() {
        for (MarkerAttribute markerAttribute : this.getMarkerAttribute()) {
            if (markerAttribute.getAttribute().getName() != null &&
                    markerAttribute.getAttribute().getType() != AttributeType.PHOTO_ALBUM) {
                markerAttribute.getAttribute().formmatNameAttribute();
            }
        }
    }

    /**
     * Remove atributos com nomes duplicados
     * Geotools não permite atributos com nomes duplicados
     */
    public void handlerDuplicateAttributes() {
        for (int i = 0; i < this.getMarkerAttribute().size(); i++) {
            for (int j = 0; j < this.getMarkerAttribute().size(); j++) {
                if (this.getMarkerAttribute().get(i).getAttribute().getName().equals(this.getMarkerAttribute().get(j).getAttribute().getName()) && i != j) {
                    this.getMarkerAttribute().get(i).getAttribute().setName(this.getMarkerAttribute().get(j).getAttribute().getName() + (i - 1));
                }
            }
        }
    }
	
	
	
	/*-------------------------------------------------------------------
	 *						   SETTERS AND GETTERS
	 *-------------------------------------------------------------------*/


    /**
     * @return the status
     */
    public MarkerStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(MarkerStatus status) {
        this.status = status;
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
     * @return the markerAttributes
     */
    public List<MarkerAttribute> getMarkerAttribute() {
        return markerAttributes;
    }

    /**
     * @param markerAttributes the markerAttribute to set
     */
    public void setMarkerAttribute(List<MarkerAttribute> markerAttributes) {
        this.markerAttributes = markerAttributes;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the deleted
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the imageToDelete
     */
    public Boolean getImageToDelete() {
        return imageToDelete;
    }

    /**
     * @param imageToDelete the imageToDelete to set
     */
    public void setImageToDelete(Boolean imageToDelete) {
        this.imageToDelete = imageToDelete;
    }

    /**
     * @return the wktCoordenate
     */
    public String getWktCoordenate() {
        return wktCoordenate;
    }

    /**
     * @param wktCoordenate the wktCoordenate to set
     */
    public void setWktCoordenate(String wktCoordenate) {
        this.wktCoordenate = wktCoordenate;
    }

    /**
     * @return the location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * @return the markerModeration
     */
    public List<MarkerModeration> getMarkerModeration() {
        return markerModeration;
    }

    /**
     * @param markerModeration the markerModeration to set
     */
    public void setMarkerModeration(List<MarkerModeration> markerModeration) {
        this.markerModeration = markerModeration;
    }

}
