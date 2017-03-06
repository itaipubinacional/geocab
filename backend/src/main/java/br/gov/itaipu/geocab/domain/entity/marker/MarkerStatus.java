/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.marker;

/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @since 30/09/2014
 */
public enum MarkerStatus {

    /*-------------------------------------------------------------------
     *				 		     ENUMS
     *-------------------------------------------------------------------*/
    //Do not change this order
    PENDING,
    ACCEPTED,
    REFUSED,
    SAVED,
    CANCELED;
//	YELLOW    GREEN    ORANGE    GRAY   RED

    /**
     * @return value of enum
     */
    public int getOrdinal() {
        return this.ordinal();
    }
}