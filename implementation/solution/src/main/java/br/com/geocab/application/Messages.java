package br.com.geocab.application;

import java.util.ResourceBundle;

/**
 *
 * @author rodrigofraga
 * @since Sep 25, 2013
 * @version 1.0
 * @category
 */
public class Messages
{
	/**
	 * Mensagens de exceções
	 */
	private static final ResourceBundle EXCEPTIONS = ResourceBundle.getBundle("messages.exception-messages");
	/**
	 * Mensagens de Permissões
	 */
	private static final ResourceBundle PERMISSIONS = ResourceBundle.getBundle("messages.permission-messages");
	/**
	 * Mensagens de Enuns
	 */
	private static final ResourceBundle ENUMS = ResourceBundle.getBundle("messages.enum-messages");
	
	/**
	 *
	 * @param key
	 */
	public static String getException( String key )
	{
		return EXCEPTIONS.getString(key);
	}
	/**
	 *
	 * @param key
	 */
	public static String getPermission( String key )
	{
		return PERMISSIONS.getString(key);
	}
	/**
	 *
	 * @param key
	 */
	public static String getEnum( String key )
	{
		return ENUMS.getString(key);
	}
}
