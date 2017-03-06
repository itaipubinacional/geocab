/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.markermoderation;

import br.gov.itaipu.geocab.domain.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @since 09/01/2015
 */
@Entity
public class Motive extends AbstractEntity implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -3226836196260423017L;

	/*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/


    /**
     * Name {@link Motive}
     */
    @NotNull
    @Column(nullable = true, length = 144)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "motive", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<MotiveMarkerModeration> motiveMarkerModerations = new ArrayList<MotiveMarkerModeration>();
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    public Motive() {

    }

    public Motive(Long id) {
        this.setId(id);
    }

    public Motive(Long id, String name) {
        this.setId(id);
        this.setName(name);
    }

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	
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
     * @return the motiveMarkerModerations
     */
    public List<MotiveMarkerModeration> getMotiveMarkerModerations() {
        return motiveMarkerModerations;
    }

    /**
     * @param motiveMarkerModerations the motiveMarkerModerations to set
     */
    public void setMotiveMarkerModerations(
            List<MotiveMarkerModeration> motiveMarkerModerations) {
        this.motiveMarkerModerations = motiveMarkerModerations;
    }


}
