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
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

/**
 * {@link Account} {@link org.eclipse.kapua.model.KapuaEntity}.
 *
 * @since 1.0
 */
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = AccountXmlRegistry.class, factoryMethod = "newAccount")
public interface Account extends KapuaNamedEntity {

    String TYPE = "account";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Get the account's organization
     *
     * @return
     */
    @XmlElement(name = "organization")
    Organization getOrganization();

    /**
     * Set the account's organization
     *
     * @param organization
     */
    void setOrganization(Organization organization);

    /**
     * Return the parent account path.<br>
     * The account path is a '/' separated list of the parents account identifiers in reverse order (so it should be read from right to left).<br>
     * e.g. The parent account path 7/14/15 mens that the current account has 15 as parent, then 15 has 14 as parent and 14 has 7 as parent.
     *
     * @return
     */
    @XmlElement(name = "parentAccountPath")
    String getParentAccountPath();

    /**
     * Set the parent account path.
     *
     * @param parentAccountPath
     */
    void setParentAccountPath(String parentAccountPath);

    List<Account> getChildAccounts();

    /**
     * Gets the current Account expiration date
     *
     * @return the current Account expiration date
     */
    @XmlElement(name = "expirationDate")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getExpirationDate();

    /**
     * Sets the current Account expiration date
     *
     * @param expirationDate the current Account expiration date
     */
    void setExpirationDate(Date expirationDate);

}
