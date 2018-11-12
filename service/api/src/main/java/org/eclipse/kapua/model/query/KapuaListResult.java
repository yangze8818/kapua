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
package org.eclipse.kapua.model.query;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.KapuaEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Query list result definition.
 *
 * @param <E> query entity domain
 * @since 1.0
 */
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "limitExceeded", "size", "items" })
public interface KapuaListResult<E extends KapuaEntity> extends KapuaSerializable {

    /**
     * Get the limit exceeded flag
     *
     * @return
     */
    @XmlElement(name = "limitExceeded")
    boolean isLimitExceeded();

    /**
     * Set the limit exceeded flag
     *
     * @param limitExceeded true if the query matching elements are more than {@link KapuaQuery#setLimit(Integer)} value
     */
    void setLimitExceeded(boolean limitExceeded);

    /**
     * Return the result list
     *
     * @return
     */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    List<E> getItems();

    /**
     * Return the element at i position (zero based)
     *
     * @param i
     * @return
     */
    E getItem(int i);

    /**
     * Returns the first element in the {@link KapuaListResult}.<br>
     * It returns {@code null} if first element does not exist.
     *
     * @return The first element in the {@link KapuaListResult} or {@code null} if not present.
     */
    E getFirstItem();

    /**
     * Return the result list size
     *
     * @return
     */
    @XmlElement(name = "size")
    int getSize();

    /**
     * Check if the result list is empty
     *
     * @return
     */
    boolean isEmpty();

    /**
     * Add items to the result list
     *
     * @param items
     */
    void addItems(Collection<? extends E> items);

    /**
     * Clear item result list
     */
    void clearItems();

    /**
     * Sorts the list result according to the given {@link Comparator}.
     *
     * @param comparator The {@link Comparator} used to compare items.
     */
    void sort(Comparator<E> comparator);
}
