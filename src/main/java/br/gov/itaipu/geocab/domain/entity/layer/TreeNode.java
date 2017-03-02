/**
 *
 */
package br.gov.itaipu.geocab.domain.entity.layer;

import java.util.List;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @since 19/09/2014
 */
public interface TreeNode {
    /**
     * @return
     */
    List<? extends TreeNode> getNodes();

    String getTitle();
}
