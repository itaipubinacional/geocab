/**
 *
 */
package br.gov.itaipu.geocab.domain.service.marker;

import br.gov.itaipu.geocab.application.security.ContextHolder;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.configuration.account.UserRole;
import br.gov.itaipu.geocab.domain.entity.marker.Marker;
import br.gov.itaipu.geocab.domain.entity.marker.MarkerAttribute;
import br.gov.itaipu.geocab.domain.entity.marker.MarkerStatus;
import br.gov.itaipu.geocab.domain.entity.marker.photo.Photo;
import br.gov.itaipu.geocab.domain.entity.marker.photo.PhotoAlbum;
import br.gov.itaipu.geocab.domain.entity.markermoderation.MarkerModeration;
import br.gov.itaipu.geocab.infrastructure.jcr.MetaFile;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.jcr.RepositoryException;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @since 30/09/2014
 */
@Service
@Transactional
public class MarkerService extends AbstractMarkerService {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/*-------------------------------------------------------------------
     *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param markers
     * @return
     */
    public List<Marker> insertMarker(List<Marker> markers) {
        for (Marker marker : markers) {
            this.insertMarker(marker);
        }
        return markers;
    }

    /**
     * Method to insert an {@link Marker}
     *
     * @return Marker
     * @throws RepositoryException
     * @throws IOException
     */
    public Marker insertMarker(Marker marker) {
        try {
            User user = ContextHolder.getAuthenticatedUser();

            marker.setLocation((Point) this.wktToGeometry(marker.getWktCoordenate()));

            marker.setUser(user);

            validateAttribute(marker.getMarkerAttribute());

            marker = this.markerRepository.save(marker);

            marker.setMarkerAttribute(this.insertMarkersAttributes(marker.getMarkerAttribute()));

            MarkerModeration markerModeration = new MarkerModeration();
            markerModeration.setMarker(marker);
            markerModeration.setStatus(marker.getStatus());
            this.markerModerationRepository.save(markerModeration);
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }
        return marker;
    }

    /**
     * @param marker
     * @return
     * @throws IOException
     * @throws RepositoryException
     */
    public Marker updateMarker(Marker marker) {
        try {
            Assert.isTrue(ContextHolder.getAuthenticatedUser().getId().equals(marker.getUser().getId()), messages.getMessage("Access-is-denied", null, null));

            if (marker.getLocation() == null) {
                marker.setLocation(this.markerRepository.findOne(marker.getId()).getLocation());
            } else {
                marker.setLocation((Point) this.wktToGeometry(marker.getWktCoordenate()));
            }

            validateAttribute(marker.getMarkerAttribute());

            // Não deixa repetir os atributos, previne erros do cascade
            List<MarkerAttribute> markersAttributes = marker.getMarkerAttribute();
            marker.setMarkerAttribute(null);
            this.markerRepository.save(marker);
            marker.setMarkerAttribute(markersAttributes);

            marker.setMarkerAttribute(this.insertMarkersAttributes(marker.getMarkerAttribute()));

            MarkerModeration markerModeration = new MarkerModeration();
            markerModeration.setMarker(marker);
            markerModeration.setStatus(marker.getStatus());
            this.markerModerationRepository.save(markerModeration);
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }
        return marker;
    }


    /**
     * Pega a ultima foto que foi adicionada na marker
     *
     * @param markerId
     * @return
     */
    public Page<Photo> lastPhotoByMarkerId(Long markerId, Pageable pageRequest) {

        Page<Photo> photo = this.photoRepository.findPhotoByMarkerId(markerId, pageRequest);

        Assert.isTrue(photo.getNumberOfElements() > 0);


        return photo;


    }

    public Photo lastPhotoByMarkerId(Long markerId) {

        Order order = new Order(Sort.Direction.DESC, "id");
        Sort sort = new Sort(order);
        Pageable pageRequest = new PageRequest(0, 1, sort);

        Page<Photo> photo = this.lastPhotoByMarkerId(markerId, pageRequest);
        return photo.getContent().get(0);

    }

    /**
     * @param photo
     * @return
     */
    public MetaFile getPhotoFile(Photo photo) throws RepositoryException {
        return this.metaFileRepository.findByPath(photo.getIdentifier(), true);
    }


    /**
     * Remove todas as fotos no sistema de arquivos
     *
     * @param idPhotos
     */
    public void removePhotos(List<Long> idPhotos) {
        for (Long idPhoto : idPhotos) {
            Photo photo = this.photoRepository.findOne(idPhoto);

            try {
                this.metaFileRepository.removeByPath(photo.getIdentifier());
                this.photoRepository.delete(idPhoto);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param identifier
     * @return
     */
    public Page<Photo> listPhotosByPhotoAlbumIdentifier(final String identifier, final PageRequest pageRequest) {
        return this.photoRepository.findByIdentifierContaining(identifier, pageRequest);
    }

    /**
     * @param markerAttributeId
     * @param pageRequest
     * @return
     */
    public Page<Photo> findPhotoAlbumByAttributeMarkerId(Long markerAttributeId, final PageRequest pageRequest) {
        PhotoAlbum photoAlbum = this.photoAlbumRepository.findByMarkerAttributeId(markerAttributeId);
        return this.listPhotosByPhotoAlbumIdentifier(photoAlbum.getIdentifier(), pageRequest);
    }


    /**
     * @return
     */
    public Photo findPhotoById(String identifier) {
        return this.photoRepository.findByIdentifier(identifier);
    }

    /**
     * @param photoId
     * @return
     */
    public Photo findPhotoById(Long photoId) {
        return this.photoRepository.findOne(photoId);
    }

    /**
     * Method to remove an {@link Marker}
     *
     * @param id
     */
    // @PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','"+
    // UserRole.MODERATOR_VALUE + "')")
    public void removeMarker(Long id) {
        try {
            Marker marker = this.markerRepository.findOne(id);

            Assert.isTrue(ContextHolder.getAuthenticatedUser().getId().equals(marker.getUser().getId()), messages.getMessage("Access-is-denied", null, null));

            marker.setDeleted(true);
            this.markerRepository.save(marker);
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }

    }

    /**
     * Method to block an {@link Marker}
     */
    @PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','" + UserRole.MODERATOR_VALUE + "')")
    public void enableMarker(Long id) {
        try {
            Marker marker = this.markerRepository.findOne(id);
            marker.setStatus(MarkerStatus.ACCEPTED);
            marker = this.markerRepository.save(marker);
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Method to unblock an {@link Marker}
     */
    @PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','" + UserRole.MODERATOR_VALUE + "')")
    public void disableMarker(Long id) {
        try {
            Marker marker = this.markerRepository.findOne(id);
            marker.setStatus(MarkerStatus.REFUSED);
            marker = this.markerRepository.save(marker);
        } catch (DataIntegrityViolationException e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Method to find an {@link Marker} by id
     *
     * @param id
     * @return marker
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public Marker findMarkerById(Long id) {

        Marker marker = this.markerRepository.findOne(id);

        marker.setMarkerAttribute(this.markerAttributeRepository.listAttributeByMarker(marker.getId()));
        for (MarkerAttribute markerAttribute : marker.getMarkerAttribute()) {
            markerAttribute.setPhotoAlbum(this.photoAlbumRepository.findByMarkerAttributeId(markerAttribute.getId()));
        }

        return marker;
    }

    /**
     * @return
     */
    public User getUserMe() {
        return ContextHolder.getAuthenticatedUser();
    }

    /**
     * Method to find an {@link Marker} by layer
     *
     * @param layerId
     * @return marker List
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public List<Marker> listMarkerByLayer(Long layerId) {
        final User user = ContextHolder.getAuthenticatedUser();

        List<Marker> listMarker = null;

        if (!user.equals(User.ANONYMOUS)) {
            if (user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE) || user.getRole().name().equals(UserRole.MODERATOR_VALUE)) {
                listMarker = this.markerRepository.listMarkerByLayerAll(layerId);
            } else {
                listMarker = this.markerRepository.listMarkerByLayer(layerId, user.getId());
            }
        } else {
            listMarker = this.markerRepository.listMarkerByLayerPublic(layerId);
        }

        return listMarker;
    }

    @Transactional(readOnly = true)
    public List<Marker> listMarkersToExport(List<Long> layersId, MarkerStatus status, String dateStart, String dateEnd, String user) {

        List<Marker> markers = new ArrayList<>();

        try {

            for (Long layerId : layersId) {


                Page<Marker> markersPage = this.listMarkerByFilters(layerId, status, dateStart, dateEnd, user, null);

                markers.addAll(markersPage.getContent());

            }

            return markers;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }


    }


    /**
     * @param wktPoint
     * @return
     */
    private Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException(messages.getMessage("admin.marker.Invalid-coordinates", null, null));
        }

        Assert.isTrue(geom.isValid(), messages.getMessage("admin.marker.Invalid-coordinates", null, null));
        return geom;
    }


    /**
     * Method to list all {@link Marker}
     *
     * @return marker
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public List<Marker> listAll() {
        return this.markerRepository.listAll();
    }

    /**
     * Method to find attribute by marker
     *
     * @param id
     */
    public List<MarkerAttribute> listAttributeByMarker(Long id) {
        return this.markerAttributeRepository.listAttributeByMarker(id);
    }

    /**
     * @param layer
     * @param status
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Marker> listMarkerByFiltersByUser(Long layer, MarkerStatus status, String dateStart, String dateEnd, PageRequest pageable) {
        String user = ContextHolder.getAuthenticatedUser().getEmail();
        return this.listMarkerByFilters(layer, status, dateStart, dateEnd, user, pageable);
    }

    /**
     * Method to list {@link Marker} pageable with filter options
     *
     * @param pageable
     * @return
     * @throws java.text.ParseException
     */
    @Transactional(readOnly = true)
    public Page<Marker> listMarkerByFilters(Long layer, MarkerStatus status, String dateStart, String dateEnd, String user, PageRequest pageable) {
        if (this.getUserMe().getRole() != UserRole.ADMINISTRATOR) {
            user = this.getUserMe().getEmail();
        }
        return this.markerRepository.listByFilters(layer, status, this.formattDates(dateStart, dateEnd)[0], this.formattDates(dateStart, dateEnd)[1], user, pageable);
    }

    /**
     * @param layer
     * @param status
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     * @throws java.text.ParseException
     */
    @Transactional(readOnly = true)
    public List<Marker> listMarkerByFiltersMapByUser(Long layer, MarkerStatus status, String dateStart, String dateEnd, PageRequest pageable) throws java.text.ParseException {
        String user = ContextHolder.getAuthenticatedUser().getEmail();
        return this.listMarkerByFiltersMap(layer, status, dateStart, dateEnd, user, pageable);
    }

    /**
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private Calendar[] formattDates(String dateStart, String dateEnd) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar dEnd = null;
        Calendar dStart = null;

        try {
            if (dateStart != null) {
                dStart = Calendar.getInstance();
                dStart.setTime((Date) formatter.parse(dateStart));
            }

            if (dateEnd != null) {
                dEnd = Calendar.getInstance();
                dEnd.setTime((Date) formatter.parse(dateEnd));
                dEnd.add(Calendar.DAY_OF_YEAR, 1);
                System.out.println(dEnd);
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }
        return new Calendar[]{dStart, dEnd};
    }

    /**
     * Method to list {@link Marker} pageable with filter options
     *
     * @param layer
     * @param status
     * @param dateStart
     * @param dateEnd
     * @param user
     * @param pageable
     * @return
     * @throws java.text.ParseException
     */
    @Transactional(readOnly = true)
    public List<Marker> listMarkerByFiltersMap(Long layer, MarkerStatus status, String dateStart, String dateEnd, String user, PageRequest pageable) throws java.text.ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar dEnd = null;
        Calendar dStart = null;

        if (dateStart != null) {
            dStart = Calendar.getInstance();
            dStart.setTime((Date) formatter.parse(dateStart));
        }

        if (dateEnd != null) {
            dEnd = Calendar.getInstance();
            dEnd.setTime((Date) formatter.parse(dateEnd));
            dEnd.add(Calendar.DAY_OF_MONTH, 1);
            dEnd.setTime(dEnd.getTime());
        }

        if (this.getUserMe().getRole() != UserRole.ADMINISTRATOR) {
            user = this.getUserMe().getEmail();
        }


        return this.markerRepository.listByFiltersMap(layer, status, this.formattDates(dateStart, dateEnd)[0], this.formattDates(dateStart, dateEnd)[1], user);
    }

    /**
     * Method to list {@link Marker} pageable with filter options
     *
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Marker> listMarkerByMarkers(List<Long> ids, PageRequest pageable) {
        return this.markerRepository.listByMarkers(ids, pageable);
    }

    public void saveImg(MetaFile file) throws IOException, RepositoryException {
        this.metaFileRepository.insert(file);
    }

}
