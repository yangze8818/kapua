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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import java.util.Set;

public class ServiceModuleConfiguration {

    public interface ConfigurationProvider {
        ServiceModuleProvider get() ;
    }

    private static ConfigurationProvider cofigurationProvider;

    private ServiceModuleConfiguration() {}

    public static void setConfigurationProvider(ConfigurationProvider aProvider) {
        cofigurationProvider = aProvider;
    }

    public static Set<ServiceModule> getServiceModules() {
        return cofigurationProvider.get().getModules();
    }

 }
