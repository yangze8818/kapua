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
package org.eclipse.kapua.service.device.management.asset.message.internal;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;

/**
 * Device bundle information request message.
 *
 * @since 1.0
 */
public class AssetRequestMessage extends KapuaMessageImpl<AssetRequestChannel, AssetRequestPayload>
        implements KapuaRequestMessage<AssetRequestChannel, AssetRequestPayload> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public Class<AssetRequestMessage> getRequestClass() {
        return AssetRequestMessage.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<AssetResponseMessage> getResponseClass() {
        return AssetResponseMessage.class;
    }

}
