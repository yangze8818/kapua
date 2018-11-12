/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial APInd implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.id;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Generates random identifier
 *
 * @since 1.0
 */
public class IdGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int ID_SIZE = SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_KEY_SIZE);

    private IdGenerator() {
    }

    /**
     * Generate a {@link BigInteger} random value.<br>
     * For more detail refer to: {@link SystemSettingKey#KAPUA_KEY_SIZE}
     *
     * @return
     */
    public static BigInteger generate() {
        return new BigInteger(ID_SIZE, SECURE_RANDOM);
    }

}
