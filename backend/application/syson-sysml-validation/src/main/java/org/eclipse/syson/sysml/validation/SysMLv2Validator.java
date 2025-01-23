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
import java.util.Map.Entry;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * The validator for SysMLv2 elements.
 *
 * @author arichard
 */
public class SysMLv2Validator implements EValidator {

    private final AQLInterpreter aqlInterpreter;

    public SysMLv2Validator() {
        this.aqlInterpreter = new AQLInterpreter(List.of(), List.of(SysmlPackage.eINSTANCE));
    }

    @Override
    public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    @Override
    public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    @Override
    public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean isValid = true;
        if (eObject instanceof Element element) {
            isValid = this.checkAllConstraints(element, eClass, diagnostics);
        }
        return isValid;
    }

    /**
     * Check all constraints related to the given {@link Element}.
     *
     * @param element
     *            the {@link Element} on which the constraints have to be checked.
     * @param eClass
     *            the {@link EClass} of the {@link Element}.
     * @param diagnostics
     *            the {@link DiagnosticChain} to fill with new diagnostic if constraints are not respected.
     * @return <code>true</code> if all constraints are respected, <code>false</code> otherwise.
     */
    private boolean checkAllConstraints(Element element, EClass eClass, DiagnosticChain diagnostics) {
        boolean isValid = true;

        List<ValidationRule> rules = SysMLv2ValidationRules.getValidationRules(eClass);
        for (ValidationRule validationRule : rules) {
            for (Entry<String, String> constraint : validationRule.getConstraints().entrySet()) {
                boolean validConstraint = this.executeContraint(element, constraint.getValue());
                if (!validConstraint) {
                    this.addDiagnostic(element, validationRule.getEClass(), diagnostics, constraint.getKey());
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    /**
     * Execute the AQL expression.
     *
     * @param element
     *            the {@link Element} on which the constraints have to be checked.
     * @param aqlExpression
     *            the expression to execute.
     * @return <code>true</code> if the AQL expression returns true, <code>false</code> otherwise.
     */
    private boolean executeContraint(Element element, String aqlExpression) {
        Map<String, Object> variables = Map.ofEntries(Map.entry("self", element));
        return this.aqlInterpreter.evaluateExpression(variables, aqlExpression).asBoolean().orElse(Boolean.FALSE).booleanValue();
    }

    /**
     * Add a {@link BasicDiagnostic} to the given diagnostics for the given element.
     *
     * @param element
     *            the {@link Element} on which the diagnostic has to be added.
     * @param eClass
     *            the {@link EClass} associated to the constraint.
     * @param diagnostics
     *            the {@link DiagnosticChain} on which the diagnostic has to be added.
     * @param constraintName
     *            the name of the constraint that is not respected.
     * @return
     */
    private void addDiagnostic(Element element, EClass eClass, DiagnosticChain diagnostics, String constraintName) {
        BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.WARNING,
                "",
                0,
                new StringBuilder().append(constraintName + " on " + eClass.getName() + " is not respected for " + element.getQualifiedName()).toString(),
                new Object[] {
                        element.getQualifiedName(),
                });

        diagnostics.add(basicDiagnostic);
    }
}
