package br.com.geocab.domain.repository.motive;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.accessgroup.AccessGroupLayer;
import br.com.geocab.domain.entity.markermoderation.MotiveMarkerModeration;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 12/01/2015
 * @version 1.0
 * @category Repository
 *
 */
public interface IMotiveMarkerModerationRepository extends IDataRepository<MotiveMarkerModeration, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 * @param groupId
	 * @return
	 */
	@Query(value="SELECT new MotiveMarkerModeration( motiveMarkerModeration.id, motiveMarkerModeration.description, motiveMarkerModeration.markerModeration, motiveMarkerModeration.motive ) " +
			"FROM MotiveMarkerModeration motiveMarkerModeration " +
			"WHERE motiveMarkerModeration.markerModeration.id = :markerModerationId " )
	public List<MotiveMarkerModeration> listByMarkerModerationId( @Param("markerModerationId") Long markerModerationId );
	
}
