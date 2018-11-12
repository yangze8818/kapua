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
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class AccessPermissionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AccessPermissionFactory ACCESS_PERMISSION_FACTORY = LOCATOR.getFactory(AccessPermissionFactory.class);

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermission newAccessPermission() {
        return ACCESS_PERMISSION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermissionCreator newCreator() {
        return ACCESS_PERMISSION_FACTORY.newCreator(null);
    }

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermissionListResult newAccessPermissionListResult() {
        return ACCESS_PERMISSION_FACTORY.newListResult();
    }

    public AccessPermissionQuery newQuery() {
        return ACCESS_PERMISSION_FACTORY.newQuery(null);
    }
}
