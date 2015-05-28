package br.com.geocab.application.restful;

import java.io.IOException;
import java.util.List;

import javax.jcr.RepositoryException;

import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.domain.entity.markermoderation.Motive;
import br.com.geocab.domain.entity.markermoderation.MotiveMarkerModeration;
import br.com.geocab.domain.service.MarkerModerationService;
import br.com.geocab.domain.service.MarkerService;
import br.com.geocab.domain.service.MotiveService;

import com.vividsolutions.jts.io.WKTWriter;


/**
 * @author Vinicius Ramos Kawamoto
 * @since 15/10/2014
 * @version 1.0
 * @category Controller
 */

@Controller
@RequestMapping("marker")
public class MarkerRESTful
{
	
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private MarkerService markerService;
	
	/**
	 * 
	 */
	@Autowired
	private MotiveService motiveService;
	
	/**
	 * 
	 */
	@Autowired
	private MarkerModerationService markerModerationService;

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 * @param layerId
	 * @return
	 */
	@RequestMapping(value="/{layerId}/markers", method = RequestMethod.GET)
	public @ResponseBody List<Marker> listMarkerByLayer(@PathVariable long layerId)
	{
		List<Marker> markers = this.markerService.listMarkerByLayer(layerId);
		final WKTWriter writer = new WKTWriter();
		
		// Pega a localização do objeto e converte para o formato WKT
		for (Marker marker : markers)
		{
		    marker.setWktCoordenate(writer.write(marker.getLocation()));
		}
		
		return markers;
	}
	
	/**
	 * 
	 * @param layerId
	 * @return
	 */
	@RequestMapping(value="/{markerId}/markerattributes", method = RequestMethod.GET)
	public @ResponseBody List<MarkerAttribute> listAttributeByMarker(@PathVariable long markerId)
	{
		return this.markerService.listAttributeByMarker(markerId);
	}
	
	/**
	 * 
	 * @param marker
	 * @return
	 * @throws RepositoryException 
	 * @throws IOException 
	 */
	@RequestMapping(value="/", method = RequestMethod.POST)
	public @ResponseBody Marker insertMarker(@RequestBody Marker marker) throws IOException, RepositoryException
	{
		return this.markerService.insertMarker(marker);
	}
	
	/**
	 * 
	 * @param marker
	 * @return
	 * @throws RepositoryException 
	 * @throws IOException 
	 */
	@RequestMapping(value="/", method = RequestMethod.PUT)
	public @ResponseBody Marker updateMarker(@RequestBody Marker marker) throws IOException, RepositoryException
	{
		if ( marker.getImageToDelete() == true )
		{
			FileTransfer currentFile = this.markerService.findImgByMarker(marker.getId());

			if (currentFile != null)
			{
				this.markerService.removeImg(String.valueOf(marker.getId()));
			}			
		}
		
		return this.markerService.updateMarker(marker);
	}	
	
	/**
	 * @param markerId
	 */
	@RequestMapping(value="/{markerId}", method = RequestMethod.DELETE)
	public @ResponseBody void removeMarker(@PathVariable long markerId)
	{
		this.markerService.removeMarker(markerId);
	}		
	
	/**
	 * 
	 * @param markerId
	 */
	@RequestMapping(value="/{markerId}/approve", method = RequestMethod.POST)
	public @ResponseBody MarkerModeration approveMarker(@PathVariable long markerId)
	{
		return this.markerModerationService.acceptMarker(markerId);
	}	
	
	/**
	 * @param markerId
	 */
	@RequestMapping(value="/{markerId}/refuse", method = RequestMethod.POST)
	public @ResponseBody MarkerModeration refuseMarker(@RequestBody MotiveMarkerModeration motiveMarkerModeration, @PathVariable long markerId)
	{
		return this.markerModerationService.refuseMarker(markerId, motiveMarkerModeration.getMotive(), motiveMarkerModeration.getDescription());
	}
	
	/**
	 * 
	 * @param file
	 * @param markerId
	 * @return
	 */
    @RequestMapping(value="/{markerId}/uploadphoto", headers="content-type=multipart/*", method=RequestMethod.POST)
    public @ResponseBody String markerUploadPhoto(MultipartFile file, @PathVariable long markerId){
        try 
        {
			FileTransfer currentFile = this.markerService.findImgByMarker(markerId);

			if (currentFile != null)
			{
				this.markerService.removeImg(String.valueOf(markerId));
			}
			
            FileTransfer fileTransfer = new FileTransfer(file.getOriginalFilename(), "image/jpeg", file.getBytes());
            
            this.markerService.uploadImg(fileTransfer, markerId);
            
            return "Uploaded";
        } 
        catch ( Exception e )
        {
        	return e.getMessage();
        }
    }	
	
	/**
	 * 
	 * @param layerId
	 * @return
	 */
	@RequestMapping(value="/motives", method = RequestMethod.GET)
	public @ResponseBody List<Motive> listMotives()
	{
		return this.motiveService.listMotives();
	}	
	
}