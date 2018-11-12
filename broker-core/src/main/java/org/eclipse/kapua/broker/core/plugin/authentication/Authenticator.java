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
package org.eclipse.kapua.broker.core.plugin.authentication;

import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.KapuaConnectionContext;

/**
 * Authenticator api
 * 
 * @since 1.0
 *
 */
public interface Authenticator {

    String ADDRESS_ADVISORY_PREFIX_KEY = "address_advisory_prefix";
    String ADDRESS_CLASSIFIER_KEY = "address_classifier";
    String ADDRESS_PREFIX_KEY = "address_prefix";
    String ADDRESS_CONNECT_PATTERN_KEY = "address_connect_pattern";

    /**
     * Execute the connect logic returning the authorization list (ACL)
     * 
     * @param kcc
     * @return
     * @throws KapuaException
     *             if any checks fails (credential not valid, profile missing, ...)
     */
    public abstract List<org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry> connect(KapuaConnectionContext kcc)
            throws KapuaException;

    /**
     * Execute the disconnect logic
     * 
     * @param kcc
     * @param error
     *            not null if the disconnection is due to an error not related to the client (network I/O error, server side error, ...)
     */
    public abstract void disconnect(KapuaConnectionContext kcc, Throwable error);

    /**
     * Send the connect message (this message is mainly for internal use to enforce the stealing link)
     * 
     * @param kcc
     */
    public abstract void sendConnectMessage(KapuaConnectionContext kcc);

}