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
package org.eclipse.kapua.commons.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class used to handle the mapping between services and address used to republish events.
 *
 * @since 1.0
 */
public class ServiceMap {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMap.class);

    //no need to have a concurrent map since:
    //if a service is not available (due to register process in progress) no event is sent
    //if a service is going to be deregistered may some event is still fired and will remain in the queue until the service consuming these events will come available later
    private static final Map<String, String> AVAILABLE_SERVICES = new HashMap<>();

    private ServiceMap() {
    }

    /**
     * Register the list of services to the provided address
     *
     * @param serviceAddress
     * @param servicesNames
     */
    public static synchronized void registerServices(String serviceAddress, List<String> servicesNames) {
        for (String serviceName : servicesNames) {
            //register service name
            String tmpServiceName = AVAILABLE_SERVICES.get(serviceName);
            if (tmpServiceName == null) {
                AVAILABLE_SERVICES.put(serviceName, serviceAddress);
                LOG.info("Bound service '{}' to address '{}'", serviceName, serviceAddress);
            } else if (!serviceAddress.equals(tmpServiceName)) {
                LOG.warn("The service '{}' is already registered with a different address (old '{}' - new '{}'). No change will be made", serviceName, tmpServiceName, serviceAddress);
            } else {
                LOG.info("The service '{}' is already registered with address '{}'", serviceName, serviceAddress);
            }
        }
    }

    /**
     * Unregister the provided services from the addess map
     *
     * @param servicesNames
     */
    public static synchronized void unregisterServices(List<String> servicesNames) {
        if (servicesNames != null) {
            for (String serviceName : servicesNames) {
                String tmpServiceName = AVAILABLE_SERVICES.remove(serviceName);
                if (tmpServiceName == null) {
                    LOG.warn("Cannot deregister service '{}'. The service wasn't registered!", serviceName);
                } else {
                    LOG.info("Deregistered service '{}' from address '{}'", serviceName, tmpServiceName);
                }
            }
        }
    }

    /**
     * Get the address associated to the specific service
     *
     * @param serviceName
     * @return
     */
    public static String getAddress(String serviceName) {
        return AVAILABLE_SERVICES.get(serviceName);
    }

}