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

import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;

/**
 * POJO describing a validation rule.
 *
 * @author arichard
 */
public class ValidationRule {

    private final EClass eClass;

    private final Map<String, String> constraints;

    public ValidationRule(EClass eClass, Map<String, String> constraints) {
        this.eClass = Objects.requireNonNull(eClass);
        this.constraints = Objects.requireNonNull(constraints);
    }

    public EClass getEClass() {
        return this.eClass;
    }

    public Map<String, String> getConstraints() {
        return this.constraints;
    }
}
