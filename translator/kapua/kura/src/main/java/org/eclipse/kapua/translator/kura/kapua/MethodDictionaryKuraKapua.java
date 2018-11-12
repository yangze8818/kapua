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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.service.device.call.kura.KuraMethod;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Dictionary class to define actions translations between Kura domain to Kapua domain.<br>
 * For detail about action please refer to {@link KapuaMethod} and {@link KuraMethod}
 *
 * @since 1.0
 */
public class MethodDictionaryKuraKapua {

    /**
     * Translations dictionary map
     */
    private static final Map<KuraMethod, KapuaMethod> DICTIONARY;

    static {
        DICTIONARY = new HashMap<>(5);

        DICTIONARY.put(KuraMethod.GET, KapuaMethod.READ);
        DICTIONARY.put(KuraMethod.POST, KapuaMethod.CREATE);
        DICTIONARY.put(KuraMethod.PUT, KapuaMethod.WRITE);
        DICTIONARY.put(KuraMethod.DEL, KapuaMethod.DELETE);
        DICTIONARY.put(KuraMethod.EXEC, KapuaMethod.EXECUTE);
    }

    private MethodDictionaryKuraKapua() {
    }

    /**
     * Returns the action translation from Kura domain to Kapua domain
     *
     * @param kuraMethod
     * @return
     */
    public static KapuaMethod get(KuraMethod kuraMethod) {
        return DICTIONARY.get(kuraMethod);
    }
}
