/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.app.response;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppChannel;

/**
 * {@link DeviceResponseChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation
 */
public class KuraResponseChannel extends KuraAppChannel implements DeviceResponseChannel {

    private String replyPart;
    private String requestId;

    /**
     * Constructor
     *
     * @param scopeNamespace
     * @param clientId
     */
    public KuraResponseChannel(String scopeNamespace, String clientId) {
        this(null, scopeNamespace, clientId);
    }

    /**
     * Constructor
     *
     * @param controlDestinationPrefix
     * @param scopeNamespace
     * @param clientId
     */
    public KuraResponseChannel(String controlDestinationPrefix, String scopeNamespace, String clientId) {
        super(controlDestinationPrefix, scopeNamespace, clientId);
    }

    @Override
    public String getReplyPart() {
        return replyPart;
    }

    @Override
    public void setReplyPart(String replyPart) {
        this.replyPart = replyPart;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
