package br.gov.itaipu.geocab.domain.repository.markermoderation;

import br.gov.itaipu.geocab.domain.entity.markermoderation.MotiveMarkerModeration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category Repository
 * @since 12/01/2015
 */
public interface MotiveMarkerModerationRepository extends JpaRepository<MotiveMarkerModeration, Long> {
    /*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @return
     */
    @Query(value = "SELECT new MotiveMarkerModeration( motiveMarkerModeration.id, motiveMarkerModeration.description, motiveMarkerModeration.markerModeration, motiveMarkerModeration.motive ) " +
            "FROM MotiveMarkerModeration motiveMarkerModeration " +
            "WHERE motiveMarkerModeration.markerModeration.id = :markerModerationId ")
    List<MotiveMarkerModeration> listByMarkerModerationId(@Param("markerModerationId") Long markerModerationId);

}
