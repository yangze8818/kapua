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
package org.eclipse.kapua.service.device.management.request.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.request.GenericRequestFactory;
import org.eclipse.kapua.service.device.management.request.internal.message.request.GenericRequestChannelImpl;
import org.eclipse.kapua.service.device.management.request.internal.message.request.GenericRequestMessageImpl;
import org.eclipse.kapua.service.device.management.request.internal.message.request.GenericRequestPayloadImpl;
import org.eclipse.kapua.service.device.management.request.internal.message.response.GenericResponseChannelImpl;
import org.eclipse.kapua.service.device.management.request.internal.message.response.GenericResponseMessageImpl;
import org.eclipse.kapua.service.device.management.request.internal.message.response.GenericResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseChannel;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponsePayload;

@KapuaProvider
public class GenericRequestFactoryImpl implements GenericRequestFactory {

    @Override
    public GenericRequestChannel newRequestChannel() {
        return new GenericRequestChannelImpl();
    }

    @Override
    public GenericRequestPayload newRequestPayload() {
        return new GenericRequestPayloadImpl();
    }

    @Override
    public GenericRequestMessage newRequestMessage() {
        return new GenericRequestMessageImpl();
    }

    @Override
    public GenericResponseChannel newResponseChannel() {
        return new GenericResponseChannelImpl();
    }

    @Override
    public GenericResponsePayload newResponsePayload() {
        return new GenericResponsePayloadImpl();
    }

    @Override
    public GenericResponseMessage newResponseMessage() {
        return new GenericResponseMessageImpl();
    }
}
