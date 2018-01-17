/**
 * 
 */
package br.com.geocab.domain.entity.accessgroup;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.directwebremoting.annotations.DataTransferObject;

import br.com.geocab.domain.entity.configuration.account.User;

/**
 * @author Emanuel Victor
 *
 *         Entidade criada para resolução do problema de mapeamento da coluna
 *         "username" para "id" do usuário, na tabela "access_group_ser". Dessa
 *         forma é possível a realização HQL's para o resgate de tais valores
 *
 */
@Entity
@Table(name = "ACCESS_GROUP_USER"/*, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "user_username", "access_group_id" }) }*/)
@DataTransferObject
public class AccessGroupUser /*extends AbstractEntity */implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5657810493718440426L;

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "access_group_id", nullable = true)
	private AccessGroup accessGroup;

	/**
	 * Possibilita a realização HQL's para o resgate de tais valores
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "user_username", nullable = true/* , referencedColumnName = "id" */)
	private User user;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	public AccessGroupUser() {
		super();
	}

	/**
	 * @param accessGroup
	 * @param user
	 */
	public AccessGroupUser(AccessGroup accessGroup, User user) {
		super();
		this.accessGroup = accessGroup;
		this.user = user;
	}

	/*-------------------------------------------------------------------
	 *				 	     SETTERS AND GETTERS
	 *-------------------------------------------------------------------*/
	/**
	 * @return the accessGroup
	 */
	public AccessGroup getAccessGroup() {
		return accessGroup;
	}

	/**
	 * @param accessGroup
	 *            the accessGroup to set
	 */
	public void setAccessGroup(AccessGroup accessGroup) {
		this.accessGroup = accessGroup;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/*-------------------------------------------------------------------
	 *				 	     BEHAVIORS
	 *-------------------------------------------------------------------*/

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessGroup == null) ? 0 : accessGroup.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessGroupUser other = (AccessGroupUser) obj;
		if (accessGroup == null) {
			if (other.accessGroup != null)
				return false;
		} else if (!accessGroup.equals(other.accessGroup))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	

}
