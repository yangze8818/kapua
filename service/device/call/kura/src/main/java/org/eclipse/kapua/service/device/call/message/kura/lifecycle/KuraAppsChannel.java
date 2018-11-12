/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleChannel;

/**
 * {@link DeviceLifecycleChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraAppsChannel extends AbstractKuraAppsBirthChannel implements DeviceLifecycleChannel {

    /**
     * Constructor
     *
     * @param messageClassification
     * @param scopeNamespace
     * @param clientId
     */
    public KuraAppsChannel(String messageClassification, String scopeNamespace, String clientId) {
        super(messageClassification, scopeNamespace, clientId);
    }

}
