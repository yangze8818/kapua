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
package org.eclipse.kapua.service.device.management.snapshot;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceSnapshot} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceSnapshotXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceSnapshotFactory DEVICE_SNAPSHOT_FACTORY = LOCATOR.getFactory(DeviceSnapshotFactory.class);

    /**
     * Creates a new device snapshots list
     *
     * @return
     */
    public DeviceSnapshots newDeviceSnapshots() {
        return DEVICE_SNAPSHOT_FACTORY.newDeviceSnapshots();
    }

    /**
     * Creates a new device snapshot
     *
     * @return
     */
    public DeviceSnapshot newDeviceSnapshot() {
        return DEVICE_SNAPSHOT_FACTORY.newDeviceSnapshot();
    }
}
