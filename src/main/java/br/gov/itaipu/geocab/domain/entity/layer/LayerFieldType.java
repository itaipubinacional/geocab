/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.layer;

/**
 * Classe responsável por definir o comportamento de uma {@link LayerFieldType}
 *
 * @author Vagner B.C
 * @version 1.0
 * @category
 * @since 06/08/2014
 */
public enum LayerFieldType {

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
    DATETIME,
    DATE,
    BOOLEAN;

    /**
     * @return o valor ordinal do enum
     */
    public int getOrdinal() {
        return this.ordinal();
    }
}