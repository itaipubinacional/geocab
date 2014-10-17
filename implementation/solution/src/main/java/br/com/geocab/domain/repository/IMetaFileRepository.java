package br.com.geocab.domain.repository;

import java.io.IOException;
import java.util.List;

import javax.jcr.RepositoryException;

import br.com.geocab.domain.entity.MetaFile;

/**
 * Classe pertinente ao acesso dos dados da classe {@link MetaFile}
 * 
 * @author Rodrigo P. Fraga
 * @since 03/08/2012
 * @version 1.0
 * @category Repository
 */
public interface IMetaFileRepository
{
	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 *
	 * @param metaFile
	 * @return
	 * @throws RepositoryException
	 * @throws IOException
	 */
	public MetaFile insert( MetaFile metaFile ) throws RepositoryException, IOException;
	
	/**
	 * 
	 * @param id A UUID string
	 * @throws RepositoryException
	 */
	public void remove( String id ) throws RepositoryException;
	
	/**
	 * 
	 *
	 * @param path A full file path like: /myfolder/myfolder2/myfile
	 * @throws RepositoryException
	 */
	public void removeByPath( String path ) throws RepositoryException;
	
	/**
	 * 
	 *
	 * @param folder A full file path like: /myfolder/myfolder2
	 * @throws RepositoryException
	 */
	public void removeByFolder( String folder ) throws RepositoryException;
	
	/**
	 *
	 * @param id
	 * @param withStream Data stream
	 * @throws RepositoryException
	 */
	public MetaFile findById( String id, boolean withStream ) throws RepositoryException;
	
	/**
	 *
	 * @param path A full file path like: /myfolder/myfolder2/myfile
	 * @param withStream Data stream
	 * @throws RepositoryException
	 */
	public MetaFile findByPath( String path, boolean withStream ) throws RepositoryException;
	
	/**
	 * 
	 *
	 * @param folder A folder path like /myfolder/myanotherfolder
	 * @return
	 */
	public List<MetaFile> listByFolder( String folder ) throws RepositoryException;
}
