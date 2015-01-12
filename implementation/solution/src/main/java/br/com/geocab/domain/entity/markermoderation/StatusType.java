/**
 * 
 */
package br.com.geocab.domain.entity.markermoderation;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 09/01/2015
 * @version 1.0
 */
@DataTransferObject(type="enum")
public enum StatusType
{
	PENDING, ACCEPTED, REFUSED;
}
