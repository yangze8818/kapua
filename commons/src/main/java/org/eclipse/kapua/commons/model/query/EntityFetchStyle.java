/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.query;

import org.eclipse.kapua.model.query.KapuaFetchStyle;

/**
 * Query fetch style behavior.
 * 
 * @since 1.0
 *
 */
public enum EntityFetchStyle implements KapuaFetchStyle {
    /**
     * Partial entity fields retrieving
     */
    BASIC,
    /**
     * Full entity retrieving
     */
    FULL
}
