/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration.internal.exception;

import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementResponseErrorCodes;

public enum ConfigurationManagementResponseErrorCodes implements DeviceManagementResponseErrorCodes {

    /**
     * The device has returned an error when getting configuration
     */
    CONFIGURATION_GET_ERROR,

    /**
     * The device has returned an error when putting configuration
     */
    CONFIGURATION_PUT_ERROR,
}
