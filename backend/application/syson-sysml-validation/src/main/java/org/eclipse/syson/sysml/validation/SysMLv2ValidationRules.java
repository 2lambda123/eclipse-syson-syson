/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.sysml.validation;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Contains all SysMLv2 validation rules from the specification.
 *
 * @author arichard
 */
public class SysMLv2ValidationRules {

    // @formatter:off

    public static final List<ValidationRule> VALIDATION_RULES = List.of(
            new ValidationRule(SysmlPackage.eINSTANCE.getPartUsage(), Map.ofEntries(
                    Map.entry("checkPartUsageSpecialization", "aql:self.specializesFromLibrary('Parts::parts')")))
    );

    // @formatter:on

    public static final List<ValidationRule> getValidationRules(EClass eClass) {
        return VALIDATION_RULES.stream()
                .filter(rule -> rule.getEClass().equals(eClass) || rule.getEClass().isSuperTypeOf(eClass))
                .toList();
    }
}
