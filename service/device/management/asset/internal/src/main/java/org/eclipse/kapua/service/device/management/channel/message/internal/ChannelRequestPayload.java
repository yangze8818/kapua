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
package org.eclipse.kapua.service.device.management.channel.message.internal;

import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Device bundle information request payload.
 *
 * @since 1.0
 */
public class ChannelRequestPayload extends KapuaPayloadImpl implements KapuaRequestPayload {

    private static final String CHANNEL_NAME_PREFIX = "names";
    private static final String CHANNEL_NAME_PREFIX_DOT = CHANNEL_NAME_PREFIX + ".";

    public void setChannelNames(List<String> channelNames) {
        int i = 0;
        for (String channelName : channelNames) {
            if (channelName != null) {
                getMetrics().put(CHANNEL_NAME_PREFIX_DOT + i++, channelName);
            }
        }
    }

    public List<String> getChannelNames() {
        int i = 0;
        List<String> names = new ArrayList<>();
        String name;
        do {
            name = (String) getMetrics().get(CHANNEL_NAME_PREFIX_DOT + i++);
            if (name != null) {
                names.add(name);
            }
        } while (name != null);

        return names;
    }
}
