package br.com.geocab.domain.entity;

import java.io.Serializable;

/**
 * @author Rodrigo P. Fraga 
 * @since 22/11/2012
 * @version 1.0
 * @category Entity
 */
public interface IEntity<ID extends Serializable> extends Serializable
{
	/**
	 * Core schema
	 */
	public static final String SCHEMA = "geocab";
	/**
	 * Audit schema
	 */
	public static final String AUDIT_SCHEMA = "geocab_auditoria";
	
	/*-------------------------------------------------------------------
	 * 		 				GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * @return
	 */
	public ID getId();
	/**
	 * 
	 * @param id
	 */
	public void setId(ID id);
}
