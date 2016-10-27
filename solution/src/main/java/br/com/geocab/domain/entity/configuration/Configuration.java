package br.com.geocab.domain.entity.configuration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.envers.Audited;

import br.com.geocab.domain.entity.AbstractEntity;
import br.com.geocab.domain.entity.configuration.preferences.BackgroundMap;

/**
 * 
 * Classe responsável por definir o comportamento de uma {@link Configuration}
 * Define qual é o plano de fundo padrão para os novos usuários, e configura o
 * sistema para não enviar mais email
 * 
 * @author Emanuel Victor de Oliveira Fonseca
 * @since 29/06/2016
 * @version 1.0
 * @category Entity
 */
@Entity
@Audited
@DataTransferObject
public class Configuration extends AbstractEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3188928271717948294L;

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 * {@link BackgroundMap} padrão da {@link Configuration}
	 */
	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private BackgroundMap backgroundMap;

	/**
	 * {@link BackgroundMap} padrão da {@link Configuration}
	 */
	@NotNull
	@Column(nullable = false)
	private Boolean stopSendEmail;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public Configuration()
	{

	}

	/**
	 * 
	 *
	 * @param id
	 */
	public Configuration(Long id)
	{
		this.setId(id);
	}

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * @return the backgroundMap
	 */
	public BackgroundMap getBackgroundMap()
	{
		if (backgroundMap == null)
		{
			backgroundMap = BackgroundMap.OPEN_STREET_MAP;
		}
		return backgroundMap;
	}

	/**
	 * @param backgroundMap
	 *            the backgroundMap to set
	 */
	public void setBackgroundMap(BackgroundMap backgroundMap)
	{
		if (backgroundMap == null)
		{
			backgroundMap = BackgroundMap.OPEN_STREET_MAP;
		}
		this.backgroundMap = backgroundMap;
	}

	/**
	 * @return the stopSendEmail
	 */
	public Boolean getStopSendEmail()
	{
		if (stopSendEmail == null)
		{
			stopSendEmail = false;
		}
		return stopSendEmail;
	}

	/**
	 * @param stopSendEmail
	 *            the stopSendEmail to set
	 */
	public void setStopSendEmail(Boolean stopSendEmail)
	{
		if (stopSendEmail == null)
		{
			stopSendEmail = false;
		}
		this.stopSendEmail = stopSendEmail;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((backgroundMap == null) ? 0 : backgroundMap.hashCode());
		result = prime * result
				+ ((stopSendEmail == null) ? 0 : stopSendEmail.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Configuration other = (Configuration) obj;
		if (backgroundMap != other.backgroundMap) return false;
		if (stopSendEmail == null)
		{
			if (other.stopSendEmail != null) return false;
		}
		else if (!stopSendEmail.equals(other.stopSendEmail)) return false;
		return true;
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

}
