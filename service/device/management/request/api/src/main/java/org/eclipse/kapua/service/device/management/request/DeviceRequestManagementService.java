/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.request;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;

public interface DeviceRequestManagementService extends KapuaService {

    /**
     * Execute the given device request with the provided options
     *
     * @param requestInput request input
     * @param timeout      request timeout
     * @return response output
     * @throws KapuaException
     */
    GenericResponseMessage exec(GenericRequestMessage requestInput, Long timeout) throws KapuaException;
}
