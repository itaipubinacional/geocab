package br.com.geocab.application.restful;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.repository.IMetaFileRepository;
import br.com.geocab.domain.service.marker.MarkerService;


/**
 * @author Vinicius Ramos Kawamoto
 * @since 22/10/2014
 * @version 1.0
 * @category Controller
 */
@Controller
@RequestMapping("/files")
public class MetaFileRESTFul
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private IMetaFileRepository metaFileRepository;
	
	/**
	 * 
	 */
	@Autowired
	private MarkerService markerService;	
	
	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 * @param file
	 * @param markerId
	 * @return
	 */
    @RequestMapping(value="/marker/{markerId}/upload", headers="content-type=multipart/*", method=RequestMethod.POST)
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
	 * @param path
	 * @param download
	 * @param display
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws RepositoryException 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/marker/{markerId}/download")
	public @ResponseBody MetaFile downloadProfilePicture( @PathVariable Long markerId, @RequestParam(required=false) boolean download, 
			 @RequestParam(required=false) boolean display,
			 HttpServletResponse response ) throws IOException, InvocationTargetException, RepositoryException
	{
		
		final String path = String.format( Marker.PICTURE_PATH, markerId, markerId);
		
		//First of all, we create the MetaFile to look for the media in the respository.
		MetaFile metaFile = new MetaFile();
		
		metaFile = this.metaFileRepository.findByPath( path, true );
		
		response.setHeader("Cache-Control", "max-age=864000");
		
		//Configure the response header
		if ( display )
		{
			//If we will display the media content
			response.setHeader("Content-Disposition", "inline; filename="+URLEncoder.encode(metaFile.getName(),"UTF-8"));
		}
		else if ( download )
		{
			//The name of file encoded to prevent files with blank fields or strange chars.
			response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(metaFile.getName(),"UTF-8") );			
		}
		
		//Set the content type.
		if ( metaFile.getContentType() != null && !metaFile.getContentType().isEmpty() )
		{
			response.setContentType( metaFile.getContentType() );
		}
		else
		{
			response.setContentType("product/x-download"); //product/octet-stream
		}
		
		//Try to set the content length
		if ( metaFile.getContentLength() > 0 )
		{
			response.setContentLength( (int) metaFile.getContentLength() );
		}
		
		//Write the media stream in the response stream
		//While is possible to read() the stream...
		int i;
		while ( (i = metaFile.getInputStream().read()) != -1 )
		{
			response.getOutputStream().write(i);
		}

		//Now we close all streams.
		metaFile.getInputStream().close();
		response.getOutputStream().flush();
		response.getOutputStream().close();
		
		return metaFile;
	}
	
	/**
	 * Get all the Files based on an Product and a Folder
	 *
	 * @see MetaFileService
	 * @param folder The folder
	 * @return The Files of the product and folder
	 * @throws RepositoryException 
	 */
	@RequestMapping(value = "/{folder}", method = RequestMethod.GET)
	public @ResponseBody List<MetaFile> listByFolder( @PathVariable String folder ) throws RepositoryException
	{
		return this.metaFileRepository.listByFolder( folder );
	}
}