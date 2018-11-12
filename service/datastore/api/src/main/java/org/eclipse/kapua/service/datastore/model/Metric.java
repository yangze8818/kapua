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
package org.eclipse.kapua.service.datastore.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Metric definition
 *
 * @param <T>
 * @since 1.0
 */
@XmlRootElement(name = "metric")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name",
        "type",
        "value" })
public interface Metric<T> extends Comparable<T> {

    /**
     * Get the name
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Set the name
     *
     * @param name
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Get the type
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(MetricInfoTypeAdapter.class)
    Class<T> getType();

    /**
     * Set the type
     *
     * @param type
     * @since 1.0.0
     */
    void setType(Class<T> type);

    /**
     * Get the metric value
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "value")
    @XmlPath(".")
    T getValue();

    /**
     * Set the metric value
     *
     * @param value
     * @since 1.0.0
     */
    void setValue(T value);
}
