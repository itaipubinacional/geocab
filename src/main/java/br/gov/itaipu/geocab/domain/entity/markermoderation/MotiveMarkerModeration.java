/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.markermoderation;

import br.gov.itaipu.geocab.domain.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @since 09/01/2015
 */

@Entity
public class MotiveMarkerModeration extends AbstractEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7579901947534822117L;

	/*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    @NotNull
    private String description;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "markerModeration_id")
    @JsonBackReference
    private MarkerModeration markerModeration;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "motive_id")
    private Motive motive;
	
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public MotiveMarkerModeration() {

    }

    /**
     * @param id
     */
    public MotiveMarkerModeration(Long id) {
        this.setId(id);
    }
	
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public MotiveMarkerModeration(Long id, String description, MarkerModeration markerModeration, Motive motive) {
        this.setId(id);
        this.description = description;
        this.markerModeration = markerModeration;
        this.motive = motive;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the markerModeration
     */
    public MarkerModeration getMarkerModeration() {
        return markerModeration;
    }

    /**
     * @param markerModeration the markerModeration to set
     */
    public void setMarkerModeration(MarkerModeration markerModeration) {
        this.markerModeration = markerModeration;
    }

    /**
     * @return the motive
     */
    public Motive getMotive() {
        return motive;
    }

    /**
     * @param motive the motive to set
     */
    public void setMotive(Motive motive) {
        this.motive = motive;
    }


}
