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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageInfo;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;

/**
 * Define a datastore configuration provider.<br>
 * These service is responsible to get the configuration parameters (profiled by account) such as the data ttl.
 *
 * @since 1.0
 */
public interface ConfigurationProvider {

    /**
     * Get the configuration for the given scope
     *
     * @param scopeId
     * @return
     * @throws ConfigurationException
     */
    MessageStoreConfiguration getConfiguration(KapuaId scopeId) throws ConfigurationException;

    /**
     * Get the message information for the given scope
     *
     * @param scopeId
     * @return
     * @throws ConfigurationException
     */
    MessageInfo getInfo(KapuaId scopeId) throws ConfigurationException;
}
