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
package org.eclipse.kapua.service.device.management.asset.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;

/**
 * Device assets list entity implementation.
 *
 * @since 1.0
 *
 */
public class DeviceAssetsImpl implements DeviceAssets {

    private static final long serialVersionUID = -6657213220333406876L;
    private List<DeviceAsset> assets;

    @Override
    public List<DeviceAsset> getAssets() {
        if (assets == null) {
            assets = new ArrayList<>();
        }

        return assets;
    }

    @Override
    public void setAssets(List<DeviceAsset> assets) {
        this.assets = assets;
    }
}
