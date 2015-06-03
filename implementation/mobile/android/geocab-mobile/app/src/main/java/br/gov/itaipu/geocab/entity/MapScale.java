package br.gov.itaipu.geocab.entity;

/**
 * Created by Vinicius on 25/09/2014.
 */
public enum MapScale {

        /*-------------------------------------------------------------------
         *				 		     ENUMS
         *-------------------------------------------------------------------*/
        //Do not change this order
        UM70M, UM35M, UM18M, UM9M, UM4M, UM2M, UM1M, UM528K, UM274K, UM137K, UM68K, UM34K, UM17K, UM8561, UM4280, UM2140;

        /**
         *
         * @return value of enum
         */
        public int getOrdinal()
        {
            return this.ordinal();
        }
}
