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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

/**
 * Xml event bus marshaller implementation
 *
 * @since 1.0
 */
public class XmlServiceEventMarshaler implements ServiceEventMarshaler {

    public static final String CONTENT_TYPE_XML = "application/xml";

    @Override
    public String getContentType() {
        return CONTENT_TYPE_XML;
    }

    @Override
    public ServiceEvent unmarshal(String message) throws KapuaException {
        try {
            return XmlUtil.unmarshal(message, ServiceEvent.class);
        } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
            throw new ServiceEventBusException(e);
        }
    }

    @Override
    public String marshal(ServiceEvent kapuaEvent) throws ServiceEventBusException {
        try {
            return XmlUtil.marshal(kapuaEvent);
        } catch (JAXBException e) {
            throw new ServiceEventBusException(e);
        }
    }

}
