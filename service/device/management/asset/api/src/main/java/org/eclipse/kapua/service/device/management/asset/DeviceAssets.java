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
package org.eclipse.kapua.service.device.management.asset;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DeviceAsset}s list entity definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "deviceAssets")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceAssetXmlRegistry.class, factoryMethod = "newAssetListResult")
public interface DeviceAssets extends KapuaSerializable {

    /**
     * Get the {@link DeviceAsset} {@link List}
     *
     * @return The {@link DeviceAsset} {@link List}.
     * @since 1.0.0
     */
    @XmlElement(name = "deviceAsset")
    List<DeviceAsset> getAssets();

    /**
     * Sets the {@link DeviceAsset} {@link List}.
     *
     * @param assets The {@link DeviceAsset} {@link List}.
     * @since 1.0.0
     */
    void setAssets(List<DeviceAsset> assets);
}
