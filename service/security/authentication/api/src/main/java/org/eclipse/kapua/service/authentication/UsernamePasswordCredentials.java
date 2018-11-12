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
package org.eclipse.kapua.service.authentication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Username and password {@link LoginCredentials} definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "usernamePasswordCredentials")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "username", "password" }, factoryClass = AuthenticationXmlRegistry.class, factoryMethod = "newUsernamePasswordCredentials")
public interface UsernamePasswordCredentials extends LoginCredentials {

    /**
     * return the username
     *
     * @return
     */
    String getUsername();

    /**
     * Set the username
     *
     * @param username
     */
    void setUsername(String username);

    /**
     * return the password
     *
     * @return
     */
    String getPassword();

    /**
     * Set the password
     *
     * @param password
     */
    void setPassword(String password);
}
