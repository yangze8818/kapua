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
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.model.ClientInfoCreator;
import org.eclipse.kapua.service.datastore.model.StorableId;

import java.util.Date;

/**
 * Client information schema creator implementation
 *
 * @since 1.0.0
 */
public class ClientInfoCreatorImpl implements ClientInfoCreator {

    private KapuaId scopeId;
    private String clientId;
    private StorableId messageId;
    private Date messageTimestamp;

    /**
     * Construct a client information creator for the given account
     *
     * @param scopeId
     */
    public ClientInfoCreatorImpl(KapuaId scopeId) {
        setScopeId(scopeId);
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    protected void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public StorableId getMessageId() {
        return messageId;
    }

    @Override
    public void setMessageId(StorableId messageId) {
        this.messageId = messageId;
    }

    @Override
    public Date getMessageTimestamp() {
        return messageTimestamp;
    }

    @Override
    public void setMessageTimestamp(Date messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }
}
