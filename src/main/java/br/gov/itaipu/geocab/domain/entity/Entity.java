package br.gov.itaipu.geocab.domain.entity;

import java.io.Serializable;

/**
 * @author Rodrigo P. Fraga
 * @version 1.0
 * @category Entity
 * @since 22/11/2012
 */
public interface Entity<ID extends Serializable> extends Serializable {
    /*-------------------------------------------------------------------
	 * 		 				GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    /**
     * @return
     */
    ID getId();

    /**
     * @param id
     */
    void setId(ID id);
}
