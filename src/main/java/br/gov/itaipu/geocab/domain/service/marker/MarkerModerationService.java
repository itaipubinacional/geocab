/**
 *
 */
package br.gov.itaipu.geocab.domain.service.marker;

import br.gov.itaipu.geocab.application.security.ContextHolder;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.configuration.account.UserRole;
import br.gov.itaipu.geocab.domain.entity.marker.Marker;
import br.gov.itaipu.geocab.domain.entity.marker.MarkerStatus;
import br.gov.itaipu.geocab.domain.entity.markermoderation.MarkerModeration;
import br.gov.itaipu.geocab.domain.entity.markermoderation.Motive;
import br.gov.itaipu.geocab.domain.entity.markermoderation.MotiveMarkerModeration;
import br.gov.itaipu.geocab.domain.repository.configuration.account.UserRepository;
import br.gov.itaipu.geocab.domain.repository.marker.MarkerRepository;
import br.gov.itaipu.geocab.domain.repository.markermoderation.MarkerModerationRepository;
import br.gov.itaipu.geocab.domain.repository.markermoderation.MotiveMarkerModerationRepository;
import br.gov.itaipu.geocab.infrastructure.mail.AccountMailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category Service
 * @since 12/01/2015
 */

@Service
@Transactional
public class MarkerModerationService {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * Log
     */
    private static final Logger LOG = Logger.getLogger(MarkerModeration.class.getName());

    /**
     * I18n
     */
    @Autowired
    private MessageSource messages;

    /**
     *
     */
    @Autowired
    private MarkerModerationRepository markerModerationRepository;

    /**
     * User Repository
     */
    @Autowired
    private UserRepository userRepository;

    /**
     *
     */
    @Autowired
    private MarkerRepository markerRepository;

    /**
     *
     */
    @Autowired
    private MotiveMarkerModerationRepository motiveMarkerModerationRepository;

    /**
     * AccountMail Repository
     */
    @Autowired
    private AccountMailRepository accountMailRepository;
	
	
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param filter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public Page<MarkerModeration> listMarkerModerationByFilters(String filter, PageRequest pageable) {
        return this.markerModerationRepository.listByFilters(filter, pageable);
    }


    /**
     * Method to accept a {@link Marker}
     *
     * @return
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public MarkerModeration acceptMarker(Long id) {
        try {
            final MarkerModeration lastMarkerModeration = this.listMarkerModerationByMarker(id).get(0);

            MarkerModeration markerModeration = new MarkerModeration();

            if (lastMarkerModeration.getStatus().equals(MarkerStatus.ACCEPTED)) {
                throw new IllegalArgumentException("The marker moderation already accepted");
            } else {
                Marker marker = markerRepository.findOne(id);
                User user = this.userRepository.findOne(marker.getUser().getId());

                marker.setStatus(MarkerStatus.ACCEPTED);

                markerModeration.setMarker(marker);
                markerModeration.setStatus(MarkerStatus.ACCEPTED);

                markerModeration = this.markerModerationRepository.save(markerModeration);

                this.accountMailRepository.sendMarkerAccepted(user, marker);
            }

            return markerModeration;
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }

        return null;
    }

    /**
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void associateMotive(List<Motive> motives, Long markerModerationId) {
        try {
            MarkerModeration markerModeration = new MarkerModeration(markerModerationId);

            for (Motive motive : motives) {
                MotiveMarkerModeration motiveMarkerModeration = new MotiveMarkerModeration();
                motiveMarkerModeration.setMarkerModeration(markerModeration);
                motiveMarkerModeration.setMotive(motive);

                this.motiveMarkerModerationRepository.save(motiveMarkerModeration);
            }
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }
    }


    /**
     * Method to update an {@link Marker}
     *
     * @return Marker
     * @throws RepositoryException
     * @throws IOException
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public Marker cancelMarkerModeration(Long id) throws IOException, RepositoryException {
        try {
            final MarkerModeration lastMarkerModeration = this.listMarkerModerationByMarker(id).get(0);

            MarkerModeration markerModeration = new MarkerModeration();

            if (lastMarkerModeration.getStatus().equals(MarkerStatus.CANCELED)) {
                throw new IllegalArgumentException("The marker moderation already canceled");

            } else {
                Marker marker = markerRepository.findOne(id);
                User user = this.userRepository.findOne(marker.getUser().getId());

                marker.setStatus(MarkerStatus.CANCELED);

                markerModeration.setMarker(marker);
                markerModeration.setStatus(MarkerStatus.CANCELED);

                markerModeration = this.markerModerationRepository.save(markerModeration);

                this.accountMailRepository.sendMarkerCanceled(user, marker);

                return marker;
            }


        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }

        return null;
    }


    /**
     * Method to refuse a {@link Marker}
     *
     * @return
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public MarkerModeration refuseMarker(Long markerId, Motive motive, String description) {

        try {
            PageRequest pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "created"));

            final MarkerModeration lastMarkerModeration = this.markerModerationRepository.listMarkerModerationByMarker(markerId, pageable).getContent().get(0);

            MarkerModeration markerModeration = new MarkerModeration();

            if (lastMarkerModeration.getStatus().equals(MarkerStatus.REFUSED)) {
                throw new IllegalArgumentException("The marker moderation already refused");
            } else {
                Marker marker = markerRepository.findOne(markerId);
                User user = this.userRepository.findOne(marker.getUser().getId());

                marker.setStatus(MarkerStatus.REFUSED);

                markerModeration.setMarker(marker);
                markerModeration.setStatus(MarkerStatus.REFUSED);

                markerModeration = this.markerModerationRepository.save(markerModeration);

                MotiveMarkerModeration motiveMarkerModeration = new MotiveMarkerModeration();
                motiveMarkerModeration.setMarkerModeration(markerModeration);
                motiveMarkerModeration.setMotive(motive);
                motiveMarkerModeration.setDescription(description);

                this.motiveMarkerModerationRepository.save(motiveMarkerModeration);

                this.accountMailRepository.sendMarkerRefused(user, marker, motiveMarkerModeration);

            }

            return markerModeration;
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }

        return null;
    }

    /**
     * @param markerModerationId
     * @return
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public List<MotiveMarkerModeration> listMotivesByMarkerModerationId(Long markerModerationId) {
        List<MotiveMarkerModeration> motivesMarkerModeration = this.motiveMarkerModerationRepository.listByMarkerModerationId(markerModerationId);

        return motivesMarkerModeration;
    }

    /**
     * @param markerId
     * @return
     */
    @Transactional(readOnly = true)
    public List<MarkerModeration> listMarkerModerationByMarker(Long markerId) {
        List<MarkerModeration> markersModerations = this.markerModerationRepository.listMarkerModerationByMarker(markerId);
        for (MarkerModeration markerModeration : markersModerations) {
            if (!markerModeration.getMarker().getUser().getId().equals(ContextHolder.getAuthenticatedUser().getId()) && !ContextHolder.getAuthenticatedUser().getRole().equals(UserRole.ADMINISTRATOR)) {
                throw new RuntimeException(this.messages.getMessage("admin.Access-denied", new Object[]{}, null));
            }
        }
        return markersModerations;
    }


    /**
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<MarkerModeration> listMarkerModerationByMarker(Long markerId, PageRequest pageable) {
        //pageable.setSort(new Sort(Direction.ASC, "id"));

        Page<MarkerModeration> markersModerations = this.markerModerationRepository.listMarkerModerationByMarker(markerId, pageable);
        for (MarkerModeration markerModeration : markersModerations) {
            if (!markerModeration.getMarker().getUser().getId().equals(ContextHolder.getAuthenticatedUser().getId()) && !ContextHolder.getAuthenticatedUser().getRole().equals(UserRole.ADMINISTRATOR)) {
                throw new RuntimeException(this.messages.getMessage("admin.Access-denied", new Object[]{}, null));
            }
        }

        return markersModerations;
    }

}
