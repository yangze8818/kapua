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
package org.eclipse.kapua.service.device.management.packages.model.install;

/**
 * Device package install request definition.
 *
 * @since 1.0
 */
public interface DevicePackageInstallRequest {

    /**
     * Get the package name
     *
     * @return
     */
    String getName();

    /**
     * Set the package name
     *
     * @param name
     */
    void setName(String name);

    /**
     * Get the package version
     *
     * @return
     */
    String getVersion();

    /**
     * Set the package version
     *
     * @param version
     */
    void setVersion(String version);

    /**
     * Get the device reboot flag
     *
     * @return
     */
    Boolean isReboot();

    /**
     * Set the device reboot flag
     *
     * @param reboot
     */
    void setReboot(Boolean reboot);

    /**
     * Get the reboot delay
     *
     * @return
     */
    Integer getRebootDelay();

    /**
     * Set the reboot delay
     *
     * @param rebootDelay
     */
    void setRebootDelay(Integer rebootDelay);
}
