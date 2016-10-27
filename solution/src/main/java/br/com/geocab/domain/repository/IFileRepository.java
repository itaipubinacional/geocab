/**
 * 
 */
package br.com.geocab.domain.repository;

import java.io.File;

/**
 * @author thiago
 *
 */
public interface IFileRepository
{
	/**
	 * 
	 * @return
	 */
	public File[] listFilePath( String path );

}
