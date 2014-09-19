package br.com.geocab.infrastructure.jpa2.hibernate;

import org.hibernate.AssertionFailure;
import org.hibernate.cfg.DefaultComponentSafeNamingStrategy;
import org.hibernate.internal.util.StringHelper;

/**
 * Uma estraégia de nomeação onde nomes mistos são separados por sublinhado
 * 
 * @author rodrigo
 * @since 29/07/2013
 * @version 1.0
 * @category
 
 
 */
public class NamingStrategy extends DefaultComponentSafeNamingStrategy
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8102411374924176656L;

	/**
	 * A instância singleton conveniente
	 */
	public static final org.hibernate.cfg.NamingStrategy INSTANCE = new NamingStrategy();

	/**
	 * Retornar o nome da classe não qualificada, caso misto convertidos em
	 * sublinhados
	 */
	@Override
	public String classToTableName(String className)
	{
		return addUnderscores(StringHelper.unqualify(className));
	}

	/**
	 * Converte os casos misto para sublinhado
	 */
	@Override
	public String tableName(String tableName)
	{
		return addUnderscores(tableName);
	}

	/**
	 * Converte os casos misto para sublinhado
	 */
	@Override
	public String columnName(String columnName)
	{
		return addUnderscores(columnName);
	}

	/**
	 * Retorna o caminho da propriedade plena com separadores sublinados,
	 * maiúsculas e minúsculas convertidas para sublinhado
	 */
	@Override
	public String propertyToColumnName(String propertyName)
	{
		return addUnderscores(StringHelper.unqualify(propertyName));
	}

	/**
	 * 
	 */
	@Override
	public String collectionTableName(String ownerEntity,
			String ownerEntityTable, String associatedEntity,
			String associatedEntityTable, String propertyName)
	{
		return tableName(new StringBuilder(ownerEntityTable)
				.append("_")
				.append(associatedEntityTable != null ? associatedEntityTable
						: addUnderscores(propertyName)).toString());
	}

	/**
	 * 
	 */
	@Override
	public String foreignKeyColumnName(String propertyName,
			String propertyEntityName, String propertyTableName,
			String referencedColumnName)
	{
		String header = propertyName != null ? addUnderscores(propertyName)
				: propertyTableName;
		if (header == null) throw new AssertionFailure(
				"NamingStrategy not properly filled");
		return columnName(header + "_" + referencedColumnName);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected static String addUnderscores(String name)
	{
		StringBuilder buf = new StringBuilder(name.replace('.', '_'));
		for (int i = 1; i < buf.length() - 1; i++)
		{
			if (Character.isLowerCase(buf.charAt(i - 1))
					&& Character.isUpperCase(buf.charAt(i))
					&& Character.isLowerCase(buf.charAt(i + 1)))
			{
				buf.insert(i++, '_');
			}
		}
		return buf.toString().toLowerCase();
	}
}
