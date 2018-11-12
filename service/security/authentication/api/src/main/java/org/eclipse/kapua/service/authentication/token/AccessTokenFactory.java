/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token;

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.Date;

/**
 * Access token factory service definition.
 *
 * @since 1.0
 */
public interface AccessTokenFactory extends KapuaEntityFactory<AccessToken, AccessTokenCreator, AccessTokenQuery, AccessTokenListResult> {

    /**
     * Create a new {@link AccessTokenCreator} for the specific access credential type
     *
     * @param scopeId      The scopeId of the new {@link AccessToken}.
     * @param userId       The userId owner of the new {@link AccessToken}.
     * @param tokenId      The tokenId of the new {@link AccessToken}.
     * @param expiresOn    The expiration date after which the token is no longer valid.
     * @param refreshToken The refresh token to obtain a new {@link AccessToken} after expiration.
     * @return A new instance of {@link AccessTokenCreator}.
     * @since 1.0
     */
    AccessTokenCreator newCreator(KapuaId scopeId, KapuaId userId, String tokenId, Date expiresOn, String refreshToken, Date refreshExpiresOn);

}
