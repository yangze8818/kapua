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
package org.eclipse.kapua.message;

/**
 * Device position definition.
 *
 * @since 1.0
 */

import org.eclipse.kapua.message.xml.MessageXmlRegistry;

import javax.xml.bind.annotation.XmlType;

@XmlType(factoryClass = MessageXmlRegistry.class, factoryMethod = "newPosition")
        public interface Position {

}
