package br.com.geocab.infrastructure.jcr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.validation.Validator;

import org.modeshape.jcr.api.JcrTools;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.geocab.domain.entity.MetaFile;
import br.com.geocab.domain.repository.IMetaFileRepository;
import br.com.geocab.infrastructure.jcr.modeshape.MetadataNodeType;


/**
 * 
 * @author juciel.freitas
 * @since 28/02/2013
 * @version 1.0
 * @category
 */
public class MetaFileRepository implements IMetaFileRepository
{
	
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private Session session;
	/**
	 * 
	 */
	@Autowired
	private Validator validator;
	/**
	 * 
	 */
	private JcrTools manager = new JcrTools(true);
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/*
	 * 
	 *
	 * (non-Javadoc)
	 * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#insert(br.mil.fab.sisplaer.core.domain.entity.MetaFile)
	 */
	@Override
	public MetaFile insert( MetaFile metaFile ) throws RepositoryException, IOException
	{
		this.validator.validate(metaFile);
		
		//Buscamos a lista de arquivos já adicionados
		List<MetaFile> metaFiles = this.listByFolder( metaFile.getFolder());
			
		//Devemos garantir que os arquivos adicionados não fiquem com nomes iguais
		metaFile.setName( metaFile.rename(metaFiles, metaFile) ) ;	
		
		//Se não passar uma ID, geramos uma dinâmicamente com UUID.
		if ( metaFile.getId() == null )
		{
			metaFile.setId( UUID.randomUUID().toString() );
		}
		
		//Cria o nó do arquivo
		final Node fileNode = this.manager.uploadFile( session, metaFile.getPath(), metaFile.getInputStream() );
		
		final Node contentNode = fileNode.getNode( Node.JCR_CONTENT );
		contentNode.setProperty(Property.JCR_MIMETYPE, metaFile.getContentType());
		
		//Adiciona o metadado
		fileNode.addMixin(MetadataNodeType.NAME);
		fileNode.setProperty(MetadataNodeType.PROPERTY_ID, metaFile.getId());
		fileNode.setProperty(MetadataNodeType.PROPERTY_FILENAME, metaFile.getName());
		fileNode.setProperty(MetadataNodeType.PROPERTY_DESCRIPTION, metaFile.getDescription() );
		fileNode.setProperty(MetadataNodeType.PROPERTY_CREATED_BY, metaFile.getCreatedBy() );
		
		this.session.save();
		
		metaFile.setCreated( fileNode.getProperty(Property.JCR_CREATED).getDate() );
		
		return metaFile;
	}
	
	/*
	 *
	 * (non-Javadoc)
	 * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#remove(java.lang.String)
	 */
	@Override
	public void remove( String id ) throws RepositoryException
	{
		final String sql = "SELECT * FROM [nt:file] AS file " +
				  			"WHERE file.["+MetadataNodeType.PROPERTY_ID+"] = '" + id + "'";

		final QueryManager queryManager = this.session.getWorkspace().getQueryManager();
		final Query query = queryManager.createQuery(sql, Query.JCR_SQL2);
		final QueryResult result = query.execute();
		
		final NodeIterator i = result.getNodes();
		
		if ( !i.hasNext() ) throw new PathNotFoundException( "The file id '"+id+"' was not found." );
		
		this.session.removeItem( i.nextNode().getPath() );
		
		this.session.save();
	}
	
	/*
	 *
	 * (non-Javadoc)
	 * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#removeByPath(java.lang.String)
	 */
	@Override
	public void removeByPath( String path ) throws RepositoryException
	{
		if ( session.itemExists(path) )
		{
			this.session.removeItem( path );
		}
		else
		{
			throw new PathNotFoundException( "The path '"+path+"' was not found." );
		}
		
		this.session.save();
	}
	
	/**
	 * 
	 * @param folder
	 * @throws RepositoryException
	 */
	@Override
	public void removeByFolder( String folder ) throws RepositoryException
	{
		if ( session.itemExists(folder) )
		{
			this.session.removeItem( folder );
		}
		else
		{
			throw new PathNotFoundException( "The folder '"+folder+"' was not found." );
		}
		
		this.session.save();
	}
	
	/*
	 *
	 * (non-Javadoc)
	 * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#findById(java.lang.String)
	 */
	@Override
	public MetaFile findById( String id, boolean withStream ) throws RepositoryException
	{
		final String sql = "SELECT * FROM [nt:file] AS file " +
							"WHERE file.["+MetadataNodeType.PROPERTY_ID+"] = '" + id + "'";
		
		final QueryManager queryManager = this.session.getWorkspace().getQueryManager();
		final Query query = queryManager.createQuery(sql, Query.JCR_SQL2);
		final QueryResult result = query.execute();
		
		final NodeIterator i = result.getNodes();
		
		if ( !i.hasNext() ) throw new PathNotFoundException( "The file id '"+id+"' was not found." );
		
		final Node fileNode = i.nextNode();
		
		return MetadataNodeType.fromNodeToMetaFile(fileNode, new MetaFile(), withStream);
	}
	
	/*
	 *
	 * (non-Javadoc)
	 * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#findByPath(java.lang.String)
	 */
	@Override
	public MetaFile findByPath( String path, boolean withStream ) throws RepositoryException
	{
		final Node fileNode = this.session.getNode( path );
		
		return MetadataNodeType.fromNodeToMetaFile(fileNode, new MetaFile(), withStream);
	}

	/*
	 * 
	 * (non-Javadoc)
	 * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#listByFolder(java.lang.String)
	 */
	@Override
	public List<MetaFile> listByFolder( String folder ) throws RepositoryException
	{
		final List<MetaFile> metaFiles = new ArrayList<MetaFile>();
		
		final String sql = "SELECT * FROM [nt:folder] AS folder " +
						    "WHERE folder.[jcr:path] = '" + folder + "'";
		
		final QueryManager queryManager = this.session.getWorkspace().getQueryManager();
		final Query query = queryManager.createQuery(sql, Query.JCR_SQL2);
		
		final QueryResult result = query.execute();
		
		final NodeIterator i = result.getNodes();
		if ( !i.hasNext() ) return metaFiles;
		
		final Node folderNode = i.nextNode();
			
		final NodeIterator folderIterator = folderNode.getNodes();
		
		while ( folderIterator.hasNext() )
		{
			metaFiles.add( MetadataNodeType.fromNodeToMetaFile( folderIterator.nextNode(), new MetaFile(), false) );
		}		
					
		return metaFiles;
	}
}
