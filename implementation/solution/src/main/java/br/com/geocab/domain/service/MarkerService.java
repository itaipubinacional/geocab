/**
 * 
 */
package br.com.geocab.domain.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.xml.bind.JAXBException;

import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.domain.repository.IMetaFileRepository;
import br.com.geocab.domain.repository.marker.IMarkerAttributeRepository;
import br.com.geocab.domain.repository.marker.IMarkerRepository;
import br.com.geocab.domain.repository.markermoderation.IMarkerModerationRepository;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * @author Thiago Rossetto Afonso
 * @since 30/09/2014
 * @version 1.0
 */
@Service
@Transactional
@RemoteProxy(name = "markerService")
public class MarkerService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Log
	 */
	private static final Logger LOG = Logger.getLogger(DataSourceService.class
			.getName());

	/**
	 * Repository of {@link DataSource}
	 */
	@Autowired
	private IMarkerRepository markerRepository;

	/**
	 * 
	 */
	@Autowired
	private IMarkerAttributeRepository markerAttributeRepository;

	/**
	 * 
	 */
	@Autowired
	private IMarkerModerationRepository markerModerationRepository;

	/**
	 * I18n
	 */
	@Autowired
	private MessageSource messages;

	@Autowired
	private IMetaFileRepository metaFileRepository;

	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Method to insert an {@link Marker}
	 * 
	 * @param Marker
	 * @return Marker
	 * @throws RepositoryException
	 * @throws IOException
	 */
	public Marker insertMarker(Marker marker) throws IOException,
			RepositoryException
	{

		List<MarkerModeration> list = this.markerModerationRepository
				.listByFilters(null, null).getContent();
		System.out.println(list);
		try
		{
			User user = ContextHolder.getAuthenticatedUser();

			marker.setLocation((Point) this.wktToGeometry(marker
					.getWktCoordenate()));

			marker.setStatus(MarkerStatus.PENDING);
			marker.setUser(user);
			marker = this.markerRepository.save(marker);
			if (marker.getImage() != null)
			{
				this.uploadImg(marker.getImage(), marker.getId());
			}

			MarkerModeration markerModeration = new MarkerModeration();
			markerModeration.setMarker(marker);
			markerModeration.setStatus(MarkerStatus.PENDING);
			this.markerModerationRepository.save(markerModeration);

		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
			final String error = e.getCause().getCause().getMessage();

			this.dataIntegrityViolationException(error);
		}
		return marker;
	}

	/**
	 * Method to update an {@link Marker}
	 * 
	 * @param Marker
	 * @return Marker
	 * @throws RepositoryException
	 * @throws IOException
	 */
	// @PreAuthorize("hasAnyRole('"+UserRole.ADMINISTRATOR_VALUE+"','"+UserRole.MODERATOR_VALUE+"')")
	public Marker updateMarker(Marker marker) throws IOException,
			RepositoryException
	{
		try
		{
			Marker markerTemporary = this.markerRepository.findOne(marker
					.getId());

			if (markerTemporary.getLayer().getId() != marker.getLayer().getId())
			{
				List<MarkerAttribute> markerAttributes = this.markerAttributeRepository
						.listAttributeByMarker(marker.getId());

				if (markerAttributes != null)
				{
					this.markerAttributeRepository
							.deleteInBatch(markerAttributes);
				}
			}

			FileTransfer file = this.findImgByMarker(marker.getId());

			if (file != null && marker.getImage() != null)
			{
				this.removeImg(String.valueOf(marker.getId()));
			}

			if (marker.getImage() != null)
			{
				this.uploadImg(marker.getImage(), marker.getId());
			}

			marker.setLocation(markerTemporary.getLocation());

			marker = this.markerRepository.save(marker);
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
			final String error = e.getCause().getCause().getMessage();

			this.dataIntegrityViolationException(error);
		}
		return marker;
	}

	/**
	 * Method to remove an {@link Marker}
	 * 
	 * @param id
	 */
	@PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','"
			+ UserRole.MODERATOR_VALUE + "')")
	public void removeMarker(Long id)
	{
		Marker marker = this.findMarkerById(id);
		marker.setDeleted(true);
		this.markerRepository.save(marker);
	}

	/**
	 * Method to block an {@link Marker}
	 * 
	 * @param Marker
	 *            marker
	 */
	@PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','"
			+ UserRole.MODERATOR_VALUE + "')")
	public void enableMarker(Long id)
	{
		try
		{
			Marker marker = this.findMarkerById(id);
			marker.setStatus(MarkerStatus.ACCEPTED);
			marker = this.markerRepository.save(marker);
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
			final String error = e.getCause().getCause().getMessage();

			this.dataIntegrityViolationException(error);
		}
	}

	/**
	 * Method to unblock an {@link Marker}
	 * 
	 * @param Marker
	 *            marker
	 */
	@PreAuthorize("hasAnyRole('" + UserRole.ADMINISTRATOR_VALUE + "','"
			+ UserRole.MODERATOR_VALUE + "')")
	public void disableMarker(Long id)
	{
		try
		{
			Marker marker = this.findMarkerById(id);
			marker.setStatus(MarkerStatus.REFUSED);
			marker = this.markerRepository.save(marker);
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.info(e.getMessage());
			final String error = e.getCause().getCause().getMessage();

			this.dataIntegrityViolationException(error);
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
	public Marker findMarkerById(Long id)
	{
		return this.markerRepository.findOne(id);
	}

	public User getUserMe()
	{
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
	public List<Marker> listMarkerByLayerFilters(Long layerId)
	{
		final User user = ContextHolder.getAuthenticatedUser();

		List<Marker> listMarker = null;

		if (!user.equals(User.ANONYMOUS))
		{

			if (user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE)
					|| user.getRole().name().equals(UserRole.MODERATOR_VALUE))
			{
				listMarker = this.markerRepository
						.listMarkerByLayerAll(layerId);
			}
			else
			{
				listMarker = this.markerRepository.listMarkerByLayer(layerId,
						user.getId());
			}

		}
		else
		{
			listMarker = this.markerRepository.listMarkerByLayerPublic(layerId);
		}

		for (Marker marker : listMarker)
		{
			marker.setMarkerAttribute(listAttributeByMarker(marker.getId()));
		}

		return listMarker;
	}

	/**
	 * Method to find an {@link Marker} by layer
	 * 
	 * @param layerId
	 * @return marker List
	 * @throws JAXBException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listMarkerByLayer(Long layerId)
	{
		final User user = ContextHolder.getAuthenticatedUser();

		List<Marker> listMarker = null;

		if (!user.equals(User.ANONYMOUS))
		{

			if (user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE)
					|| user.getRole().name().equals(UserRole.MODERATOR_VALUE))
			{
				listMarker = this.markerRepository
						.listMarkerByLayerAll(layerId);
			}
			else
			{
				listMarker = this.markerRepository.listMarkerByLayer(layerId,
						user.getId());
			}

		}
		else
		{
			listMarker = this.markerRepository.listMarkerByLayerPublic(layerId);
		}

		return listMarker;
	}

	/**
	 * 
	 * @param wktPoint
	 * @return
	 */
	private Geometry wktToGeometry(String wktPoint)
	{
		WKTReader fromText = new WKTReader();
		Geometry geom = null;
		try
		{
			geom = fromText.read(wktPoint);
		}
		catch (ParseException e)
		{
			throw new RuntimeException("Not a WKT string:" + wktPoint);
		}
		return geom;
	}

	/**
	 * 
	 * @param geometry
	 * @return
	 */
	private String geometryToWkt(Geometry geometry)
	{
		WKTWriter geom = new WKTWriter();
		return geom.write(geometry);
	}

	/**
	 * Method to list all {@link Marker}
	 * 
	 * @param id
	 * @return marker
	 * @throws JAXBException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listAll()
	{
		return this.markerRepository.listAll();
	}

	/**
	 * Method to find attribute by marker
	 * 
	 * @param id
	 */
	public List<MarkerAttribute> listAttributeByMarker(Long id)
	{
		return this.markerAttributeRepository.listAttributeByMarker(id);
	}

	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 * @throws java.text.ParseException
	 */
	@Transactional(readOnly = true)
	public Page<Marker> listMarkerByFilters(String layer, MarkerStatus status,
			String dateStart, String dateEnd, String user, PageRequest pageable)
			throws java.text.ParseException
	{

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dEnd = null;
		Calendar dStart = null;

		if (dateStart != null)
		{
			dStart = Calendar.getInstance();
			dStart.setTime((Date) formatter.parse(dateStart));
		}

		if (dateEnd != null)
		{
			dEnd = Calendar.getInstance();
			dEnd.setTime((Date) formatter.parse(dateEnd));
		}

		// return this.markerRepository.listByFilters(layer, status, dStart,
		// dEnd, user, pageable);
		return this.markerRepository.listByFilters(layer, status, dStart, dEnd,
				user, pageable);
	}

	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 * @throws java.text.ParseException
	 */
	@Transactional(readOnly = true)
	public List<Marker> listMarkerByFiltersMap(String layer,
			MarkerStatus status, String dateStart, String dateEnd, String user,
			PageRequest pageable) throws java.text.ParseException
	{

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dEnd = null;
		Calendar dStart = null;

		if (dateStart != null)
		{
			dStart = Calendar.getInstance();
			dStart.setTime((Date) formatter.parse(dateStart));
		}

		if (dateEnd != null)
		{
			dEnd = Calendar.getInstance();
			dEnd.setTime((Date) formatter.parse(dateEnd));
		}

		return this.markerRepository.listByFiltersMap(layer, status, dStart,
				dEnd, user);
	}

	/**
	 * Method to list {@link FonteDados} pageable with filter options
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Marker> listMarkerByMarkers(List<Long> ids, PageRequest pageable)
	{
		return this.markerRepository.listByMarkers(ids, pageable);
	}

	/**
	 * Method to verify DataIntegrityViolations and throw
	 * IllegalArgumentException with the field name
	 * 
	 * @param error
	 * @throws IllegalArgumentException
	 * @return void
	 */
	private void dataIntegrityViolationException(String error)
	{
		/*
		 * String fieldError = ""; if(error.contains("uk_data_source_name")) {
		 * fieldError = this.messages.getMessage("Name", new Object [] {}, null
		 * ); } else if(error.contains("uk_data_source_url")) { fieldError =
		 * this.messages.getMessage("Address", new Object [] {}, null ); }
		 * if(!fieldError.isEmpty()){ throw new IllegalArgumentException(
		 * this.messages
		 * .getMessage("The-field-entered-already-exists,-change-and-try-again",
		 * new Object [] {fieldError}, null) ); }
		 */
	}

	/**
	 * 
	 * @param metaFileId
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public void removeImg(String metaFileId) throws IOException,
			RepositoryException
	{

		this.metaFileRepository.remove(metaFileId);
	}

	/**
	 * 
	 * @param fileTransfer
	 * @param markerId
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public void uploadImg(FileTransfer fileTransfer, Long markerId)
			throws IOException, RepositoryException
	{

		final String mimeType = fileTransfer.getMimeType();

		final List<String> validMimeTypes = new ArrayList<String>();
		validMimeTypes.add("image/gif");
		validMimeTypes.add("image/jpeg");
		validMimeTypes.add("image/bmp");
		validMimeTypes.add("image/png");

		if (mimeType == null || !validMimeTypes.contains(mimeType))
		{
			throw new IllegalArgumentException("Formato inválido!");
		}

		InputStream is = new BufferedInputStream(fileTransfer.getInputStream());
		final BufferedImage bufferedImage = new BufferedImage(640, 480,
				BufferedImage.TYPE_INT_RGB);
		Image image = ImageIO.read(is);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, 640, 480, null);
		g.dispose();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", os);
		InputStream isteam = new ByteArrayInputStream(os.toByteArray());

		MetaFile metaFile = new MetaFile();
		metaFile.setId(String.valueOf(markerId));
		metaFile.setContentType(fileTransfer.getMimeType());
		metaFile.setContentLength(fileTransfer.getSize());
		metaFile.setFolder("/marker/" + markerId);
		metaFile.setInputStream(isteam);
		metaFile.setName(fileTransfer.getFilename());

		this.metaFileRepository.insert(metaFile);
	}

	/**
	 * 
	 * @param markerId
	 * @return
	 * @throws RepositoryException
	 */
	public FileTransfer findImgByMarker(Long markerId)
			throws RepositoryException
	{
		try
		{
			final MetaFile metaFile = this.metaFileRepository.findByPath(
					"/marker/" + markerId + "/" + markerId, true);
			return new FileTransfer(metaFile.getName(),
					metaFile.getContentType(), metaFile.getInputStream());
		}
		catch (PathNotFoundException e)
		{
			return null;
		}
	}

}
