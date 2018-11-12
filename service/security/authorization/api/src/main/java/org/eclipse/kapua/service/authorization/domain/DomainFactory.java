/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.domain;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link Domain} object factory.
 *
 * @since 1.0.0
 */
public interface DomainFactory extends KapuaEntityFactory<Domain, DomainCreator, DomainQuery, DomainListResult> {

    /**
     * Instantiate a new {@link DomainCreator} implementing object with the provided parameters.
     *
     * @param name        The {@link Domain} name to set.
     * @return A instance of the implementing class of {@link Domain}.
     * @since 1.0.0
     */
    DomainCreator newCreator(String name);

}
