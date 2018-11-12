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
package org.eclipse.kapua;

public class KapuaExceptionWithoutBundle extends KapuaException {
    public KapuaExceptionWithoutBundle(KapuaErrorCode code) {
        super(code);
    }

    public KapuaExceptionWithoutBundle(KapuaErrorCode code, Object... arguments) {
        super(code, arguments);
    }

    public KapuaExceptionWithoutBundle(KapuaErrorCode code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return "non-existing-file.properties";
    }
}
