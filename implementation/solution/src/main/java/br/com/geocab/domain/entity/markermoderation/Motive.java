/**
 * 
 */
package br.com.geocab.domain.entity.markermoderation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.IEntity;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 09/01/2015
 * @version 1.0
 */
@Entity
@Audited
@DataTransferObject(javascript="Motive")
@Table(schema=IEntity.SCHEMA)
public class Motive extends AbstractEntity implements Serializable
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3226836196260423017L;
	
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	

	/**
	 * Name {@link Motive}
	 * */
	@NotNull
	@Column(nullable=true, length=144)
	private String name;
	
	
	@OneToMany(mappedBy="motive", fetch=FetchType.EAGER, cascade={CascadeType.REMOVE})
	private List<MotiveMarkerModeration> motiveMarkerModerations = new ArrayList<MotiveMarkerModeration>();
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	public Motive()
	{
		
	}
	
	public Motive( Long id )
	{
		super(id);
	}
	
	public Motive(Long id, String name){
		super(id);
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
	 * @return the motiveMarkerModerations
	 */
	public List<MotiveMarkerModeration> getMotiveMarkerModerations()
	{
		return motiveMarkerModerations;
	}

	/**
	 * @param motiveMarkerModerations the motiveMarkerModerations to set
	 */
	public void setMotiveMarkerModerations(
			List<MotiveMarkerModeration> motiveMarkerModerations)
	{
		this.motiveMarkerModerations = motiveMarkerModerations;
	}
	
	
	
	
}
