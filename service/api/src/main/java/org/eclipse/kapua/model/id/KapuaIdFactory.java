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
package org.eclipse.kapua.model.id;

import org.eclipse.kapua.model.KapuaObjectFactory;

import java.math.BigInteger;

/**
 * {@link KapuaId} factory definition.
 *
 * @since 1.0.0
 */
public interface KapuaIdFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link KapuaId} starting the provided short identifier.<br>
     * <p>
     * <b>Note:</b> This operation must be the inverse function of {@link KapuaId#toCompactId()} so, in other word, this code should't fail:
     * </p>
     *
     * <pre>{@code
     * KapuaIdFactory kapuaIdFactory = KapuaLocator.getInstance().getService(KapuaIdFactory.class);
     * String shortId = "some well formed encoded short id";
     * KapuaId id = kapuaIdFactory.newKapuaId(shortId);
     * String shortIdConverted = id.getShortId();
     * AssertTrue(shortId.equals(shortIdConverted));
     * }</pre>
     *
     * @param shortId The {@link KapuaId} short id to parse.
     * @return The {@link KapuaId} parsed from its short representation.
     * @since 1.0.0
     */
    KapuaId newKapuaId(String shortId);

    /**
     * Creates a new {@link KapuaId} form the {@link BigInteger} parameter.
     *
     * @param bigInteger The {@link BigInteger} from which create the {@link KapuaId}.
     * @return The new {@link KapuaId}
     * @since 1.0.0
     */
    KapuaId newKapuaId(BigInteger bigInteger);
}
