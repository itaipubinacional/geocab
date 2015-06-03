package br.com.geocab.entity;

import java.io.Serializable;

/**
 * Created by Joaz Soares on 25/09/2014.
 */
public class Motive implements Serializable
{

    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    private Long id;
    /**
     * Name of {@link br.com.geocab.entity.Motive}
     */
    private String name;

    /*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/


    /*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
