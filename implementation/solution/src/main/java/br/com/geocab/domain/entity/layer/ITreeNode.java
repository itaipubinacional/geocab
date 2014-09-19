/**
 * 
 */
package br.com.geocab.domain.entity.layer;

import java.util.List;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 19/09/2014
 * @version 1.0
 *
 */
public interface ITreeNode
{
	/**
	 * 
	 * @return
	 */
	public List<? extends ITreeNode> getNodes();
}
