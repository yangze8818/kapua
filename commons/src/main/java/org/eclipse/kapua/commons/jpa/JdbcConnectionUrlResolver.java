/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

/**
 * Jdbc url connection resolver service definition
 *
 * @since 1.0
 */
public interface JdbcConnectionUrlResolver {

    /**
     * Return the jdbc connection url
     *
     * @return
     */
    String connectionUrl();

}
