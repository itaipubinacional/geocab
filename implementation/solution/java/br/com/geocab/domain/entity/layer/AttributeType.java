/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * @author Thiago Rossetto Afonso
 * @since 02/10/2014
 * @version 1.0
 */
@DataTransferObject(type="enum")
public enum AttributeType
{
	TEXT, NUMBER, DATE, BOOLEAN;
}
