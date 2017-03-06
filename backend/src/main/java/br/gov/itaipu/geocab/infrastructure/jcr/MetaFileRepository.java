package br.gov.itaipu.geocab.infrastructure.jcr;

import br.gov.itaipu.geocab.infrastructure.jcr.modeshape.MetadataNodeType;
import org.modeshape.jcr.api.JcrTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author juciel.freitas
 * @version 1.0
 * @category
 * @since 28/02/2013
 */
@Component
public class MetaFileRepository {

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
    public MetaFile insert(MetaFile metaFile) throws RepositoryException, IOException {
        this.validator.validate(metaFile);

        //Buscamos a lista de arquivos já adicionados
        List<MetaFile> metaFiles = this.listByFolder(metaFile.getFolder());

        //Devemos garantir que os arquivos adicionados não fiquem com nomes iguais
        metaFile.setName(metaFile.rename(metaFiles, metaFile));

        //Se não passar uma ID, geramos uma dinâmicamente com UUID.
        if (metaFile.getId() == null) {
            metaFile.setId(UUID.randomUUID().toString());
        }

        //Cria o nó do arquivo
        final Node fileNode = this.manager.uploadFile(session, metaFile.getPath(), metaFile.getInputStream());

        final Node contentNode = fileNode.getNode(Node.JCR_CONTENT);
        contentNode.setProperty(Property.JCR_MIMETYPE, metaFile.getContentType());

        //Adiciona o metadado
        fileNode.addMixin(MetadataNodeType.NAME);
        fileNode.setProperty(MetadataNodeType.PROPERTY_ID, metaFile.getId());
        fileNode.setProperty(MetadataNodeType.PROPERTY_FILENAME, metaFile.getName());
        fileNode.setProperty(MetadataNodeType.PROPERTY_DESCRIPTION, metaFile.getDescription());
        fileNode.setProperty(MetadataNodeType.PROPERTY_CREATED_BY, metaFile.getCreatedBy());

        this.session.save();

        metaFile.setCreated(fileNode.getProperty(Property.JCR_CREATED).getDate());

        return metaFile;
    }

    /*
     *
     * (non-Javadoc)
     * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#remove(java.lang.String)
     */
    public void remove(String id) throws RepositoryException {
        final String sql = "SELECT * FROM [nt:file] AS file " +
                "WHERE file.[" + MetadataNodeType.PROPERTY_ID + "] = '" + id + "'";

        final QueryManager queryManager = this.session.getWorkspace().getQueryManager();
        final Query query = queryManager.createQuery(sql, Query.JCR_SQL2);
        final QueryResult result = query.execute();

        final NodeIterator i = result.getNodes();

        if (!i.hasNext()) throw new PathNotFoundException("The file id '" + id + "' was not found.");

        this.session.removeItem(i.nextNode().getPath());

        this.session.save();
    }

    /*
     *
     * (non-Javadoc)
     * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#removeByPath(java.lang.String)
     */
    public void removeByPath(String path) throws RepositoryException {
        if (session.itemExists(path)) {
            this.session.removeItem(path);
        } else {
            throw new PathNotFoundException("The path '" + path + "' was not found.");
        }

        this.session.save();
    }

    /**
     * @param folder
     * @throws RepositoryException
     */
    public void removeByFolder(String folder) throws RepositoryException {
        if (session.itemExists(folder)) {
            this.session.removeItem(folder);
        } else {
            throw new PathNotFoundException("The folder '" + folder + "' was not found.");
        }

        this.session.save();
    }

    /*
     *
     * (non-Javadoc)
     * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#findById(java.lang.String)
     */
    public MetaFile findById(String id, boolean withStream) throws RepositoryException {
        final String sql = "SELECT * FROM [nt:file] AS file " +
                "WHERE file.[" + MetadataNodeType.PROPERTY_ID + "] = '" + id + "'";

        final QueryManager queryManager = this.session.getWorkspace().getQueryManager();
        final Query query = queryManager.createQuery(sql, Query.JCR_SQL2);
        final QueryResult result = query.execute();

        final NodeIterator i = result.getNodes();

        if (!i.hasNext()) throw new PathNotFoundException("The file id '" + id + "' was not found.");

        final Node fileNode = i.nextNode();

        return MetadataNodeType.fromNodeToMetaFile(fileNode, new MetaFile(), withStream);
    }

    /*
     *
     * (non-Javadoc)
     * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#findByPath(java.lang.String)
     */
    public MetaFile findByPath(String path, boolean withStream) throws RepositoryException {
        final Node fileNode = this.session.getNode(path);

        return MetadataNodeType.fromNodeToMetaFile(fileNode, new MetaFile(), withStream);
    }

    /*
     *
     * (non-Javadoc)
     * @see br.mil.fab.sisplaer.core.domain.repository.IMetaFileRepository#listByFolder(java.lang.String)
     */
    public List<MetaFile> listByFolder(String folder) throws RepositoryException {
        final List<MetaFile> metaFiles = new ArrayList<MetaFile>();

        final String sql = "SELECT * FROM [nt:folder] AS folder " +
                "WHERE folder.[jcr:path] = '" + folder + "'";

        final QueryManager queryManager = this.session.getWorkspace().getQueryManager();
        final Query query = queryManager.createQuery(sql, Query.JCR_SQL2);

        final QueryResult result = query.execute();

        final NodeIterator i = result.getNodes();
        if (!i.hasNext()) return metaFiles;

        final Node folderNode = i.nextNode();

        final NodeIterator folderIterator = folderNode.getNodes();

        while (folderIterator.hasNext()) {
            metaFiles.add(MetadataNodeType.fromNodeToMetaFile(folderIterator.nextNode(), new MetaFile(), false));
        }

        return metaFiles;
    }

}
