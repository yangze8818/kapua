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
package org.eclipse.kapua.service.device.call.message.kura.app.request;

import org.eclipse.kapua.service.device.call.DeviceMethod;
import org.eclipse.kapua.service.device.call.kura.KuraMethod;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppChannel;

/**
 * {@link DeviceRequestChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraRequestChannel extends KuraAppChannel implements DeviceRequestChannel {

    private KuraMethod method;
    private String[] resources;
    private String requestId;
    private String requesterClientId;

    /**
     * Constructor
     */
    public KuraRequestChannel() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeNamespace
     * @param clientId
     */
    public KuraRequestChannel(String scopeNamespace, String clientId) {
        this(null, scopeNamespace, clientId);
    }

    /**
     * Constructor
     *
     * @param controlDestinationPrefix
     * @param scopeNamespace
     * @param clientId
     */
    public KuraRequestChannel(String controlDestinationPrefix, String scopeNamespace, String clientId) {
        super(controlDestinationPrefix, scopeNamespace, clientId);
    }

    @Override
    public KuraMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(DeviceMethod method) {
        this.method = (KuraMethod) method;
    }

    @Override
    public String[] getResources() {
        if (resources == null) {
            resources = new String[0];
        }

        return resources;
    }

    @Override
    public void setResources(String[] resources) {
        this.resources = resources;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getRequesterClientId() {
        return requesterClientId;
    }

    @Override
    public void setRequesterClientId(String requesterClientId) {
        this.requesterClientId = requesterClientId;
    }

}
