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
package org.eclipse.kapua.service.device.registry.connection.option;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceConnectionOptionService} XML factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceConnectionOptionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceConnectionOptionFactory DEVICE_CONNECTION_OPTION_FACTORY = LOCATOR.getFactory(DeviceConnectionOptionFactory.class);

    /**
     * Creates a new {@link DeviceConnectionOption}
     *
     * @return
     */
    public DeviceConnectionOption newDeviceConnectionOption() {
        return DEVICE_CONNECTION_OPTION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new device connection options list result
     *
     * @return
     */
    public DeviceConnectionOptionListResult newDeviceConnectionOptionListResult() {
        return DEVICE_CONNECTION_OPTION_FACTORY.newListResult();
    }

    public DeviceConnectionOptionQuery newQuery() {
        return DEVICE_CONNECTION_OPTION_FACTORY.newQuery(null);
    }
}
