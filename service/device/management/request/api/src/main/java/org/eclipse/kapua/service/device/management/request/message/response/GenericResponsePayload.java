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
package org.eclipse.kapua.service.device.management.request.message.response;

import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.request.GenericRequestXmlRegistry;

import javax.xml.bind.annotation.XmlType;

@XmlType(factoryClass = GenericRequestXmlRegistry.class, factoryMethod = "newResponsePayload")
public interface GenericResponsePayload extends KapuaResponsePayload {

}
