/*******************************************************************************
 * Copyright (c) 2024 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.application.configuration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerTreeItemAlteredContentProvider;
import org.eclipse.syson.sysml.Membership;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link IExplorerTreeItemAlteredContentProvider} allowing to hide memberships tree items from
 * Explorer tree without hide their children.
 *
 * @author arichard
 */
@Service
public class HideMembershipsTreeItemAlteredContentProvider implements IExplorerTreeItemAlteredContentProvider {

    @Override
    public boolean canHandle(Object object, List<String> activeFilterIds) {
        return activeFilterIds.contains(SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID);
    }

    @Override
    public List<Object> apply(List<Object> computedChildren, VariableManager variableManager) {
        List<Object> alteredChildren = new ArrayList<>();
        computedChildren.forEach(child -> {
            if (child instanceof Membership membership) {
                alteredChildren.addAll(membership.getOwnedRelatedElement());
            } else {
                alteredChildren.add(child);
            }
        });
        return alteredChildren;
    }

}
