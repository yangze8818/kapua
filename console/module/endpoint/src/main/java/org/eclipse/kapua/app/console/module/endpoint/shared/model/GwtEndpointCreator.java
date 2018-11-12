/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;

public class GwtEndpointCreator extends GwtEntityCreator {

    private String schema;
    private String dns;
    private Number port;
    private boolean secure;

    public GwtEndpointCreator() {
        super();
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public Number getPort() {
        return port;
    }

    public void setPort(Number port) {
        this.port = port;
    }

    public boolean getSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }
}
