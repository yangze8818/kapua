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
package org.eclipse.kapua.service.device.management.request.message.request;

import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.request.GenericRequestXmlRegistry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(factoryClass = GenericRequestXmlRegistry.class, factoryMethod = "newRequestChannel")
public interface GenericRequestChannel extends KapuaRequestChannel {

    /**
     * Get the resources
     *
     * @return resources
     */
    @XmlElementWrapper(name = "resources")
    @XmlElement(name = "resource")
    String[] getResources();

    /**
     * Set the resources
     *
     * @param resources
     */
    void setResources(String[] resources);
}
