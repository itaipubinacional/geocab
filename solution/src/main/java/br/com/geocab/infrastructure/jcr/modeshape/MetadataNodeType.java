package br.com.geocab.infrastructure.jcr.modeshape;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import br.com.geocab.domain.entity.MetaFile;

/**
 *
 * @author rodrigofraga
 * @since Sep 12, 2013
 * @version 1.0
 * @category
 */
public class MetadataNodeType
{
	/**
	 * Define a propriedade de nome
	 */
	public static final String NAME = "metadata";
	/**
	 * Define a propriedade de id
	 */
	public static final String PROPERTY_ID = "id"; 
	/**
	 * Define a propriedade de caminho do arquivo	
	 */
	public static final String PROPERTY_FILENAME = "filename"; 
	/**
	 * Define a propriedade de autor
	 */
	public static final String PROPERTY_CREATED_BY = "createdBy"; 
	/**
	 * Define a propriedade de descrição
	 */
	public static final String PROPERTY_DESCRIPTION = "description"; 
	
	/**
	 * 
	 *
	 * @param fileNode
	 * @param metaFile
	 * @param withStream
	 * @return
	 * @throws ValueFormatException
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 */
	public static MetaFile fromNodeToMetaFile( Node fileNode, MetaFile metaFile, boolean withStream ) throws ValueFormatException, PathNotFoundException, RepositoryException
	{
		metaFile.setPath( fileNode.getPath() );
		metaFile.setCreated( fileNode.getProperty(Property.JCR_CREATED).getDate() );
		metaFile.setName( fileNode.getProperty(MetadataNodeType.PROPERTY_FILENAME).getString() );
		
		if ( fileNode.hasProperty(MetadataNodeType.PROPERTY_DESCRIPTION) )
		{
			metaFile.setDescription( fileNode.getProperty(MetadataNodeType.PROPERTY_DESCRIPTION).getString() );
		}
		
		if ( fileNode.hasProperty(MetadataNodeType.PROPERTY_CREATED_BY) )
		{
			metaFile.setCreatedBy( fileNode.getProperty(MetadataNodeType.PROPERTY_CREATED_BY).getString() );
		}
		
		final Node contentNode = fileNode.getNode( Node.JCR_CONTENT );
		metaFile.setContentType( contentNode.getProperty(Property.JCR_MIMETYPE).getString() );
		
		if ( withStream )
		{
			metaFile.setInputStream( contentNode.getProperty( Property.JCR_DATA ).getBinary().getStream() );
		}
		
		return metaFile;
	}
}
