/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.layer;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category
 * @since 22/09/2014
 */
public enum TypeFieldLayer {

    /*-------------------------------------------------------------------
     *				 		     ENUMS
     *-------------------------------------------------------------------*/
    //Não mudar este ordem pois está sendo usado de forma hierarquica
    //A ordem tambem é utilizada para definir o nivel
    STRING,
    NUMBER,
    SHORT,
    INT,
    DECIMAL,
    DOUBLE,
    DATETIME;

    /**
     * @return o valor ordinal do enum
     */
    public int getOrdinal() {
        return this.ordinal();
    }
}