/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.accessgroup;

import br.gov.itaipu.geocab.domain.entity.AbstractEntity;
import br.gov.itaipu.geocab.domain.entity.layer.CustomSearch;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;


/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @category Entity
 * @since 03/12/2014
 */
@Entity
public class AccessGroupCustomSearch extends AbstractEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6496162028366468252L;

	/*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    @ManyToOne
    @JoinColumn(name = "access_group_id")
    private AccessGroup accessGroup;

    /*
     *
     */
    @ManyToOne
    @JoinColumn(name = "custom_search_id")
    private CustomSearch customSearch;
	
	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public AccessGroupCustomSearch() {

    }

    /**
     * @param id
     */
    public AccessGroupCustomSearch(Long id) {
        this.id = id;
    }

    /**
     * @param id
     * @param accessGroupId
     * @param customSearchId
     */
    public AccessGroupCustomSearch(Long id, Long accessGroupId, Long customSearchId) {
        this.setId(id);
        this.accessGroup = new AccessGroup(accessGroupId);
        this.customSearch = new CustomSearch(customSearchId);
    }

    /**
     * @param id
     * @param accessGroup
     * @param customSearch
     */
    public AccessGroupCustomSearch(Long id, AccessGroup accessGroup, CustomSearch customSearch) {
        this.setId(id);
        this.accessGroup = accessGroup;
        this.customSearch = customSearch;
    }


    public AccessGroupCustomSearch(Long id, Long accessGroupId, String accessGroupName, String accessGroupDescription, Long customSearchId) {
        this.setId(id);

        AccessGroup accessGroup = new AccessGroup(accessGroupId);
        accessGroup.setName(accessGroupName);
        accessGroup.setDescription(accessGroupDescription);
        this.setAccessGroup(accessGroup);

        CustomSearch customSearch = new CustomSearch(customSearchId);
        this.setCustomSearch(customSearch);
    }
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((customSearch == null) ? 0 : customSearch.hashCode());
        result = prime * result
                + ((accessGroup == null) ? 0 : accessGroup.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        AccessGroupCustomSearch other = (AccessGroupCustomSearch) obj;
        if (customSearch == null) {
            if (other.customSearch != null) return false;
        } else if (!customSearch.equals(other.customSearch)) return false;
        if (accessGroup == null) {
            if (other.accessGroup != null) return false;
        } else if (!accessGroup.equals(other.accessGroup)) return false;
        return true;
    }

    /**
     * @return the accessGroup
     */
    public AccessGroup getAccessGroup() {
        return accessGroup;
    }

    /**
     * @param accessGroup the accessGroup to set
     */
    public void setAccessGroup(AccessGroup accessGroup) {
        this.accessGroup = accessGroup;
    }

    /**
     * @return the customSearch
     */
    public CustomSearch getCustomSearch() {
        return customSearch;
    }

    /**
     * @param customSearch the customSearch to set
     */
    public void setCustomSearch(CustomSearch customSearch) {
        this.customSearch = customSearch;
    }

}
