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
package org.eclipse.kapua.service.device.registry.common;

import org.eclipse.kapua.commons.util.ValidationRegex;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;

import java.util.regex.Pattern;

public enum DeviceValidationRegex implements ValidationRegex {

    QUERY_FETCH_ATTRIBUTES("(" + DeviceAttributes.CONNECTION + "|" + DeviceAttributes.LAST_EVENT + ")");

    private Pattern pattern;

    DeviceValidationRegex(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
