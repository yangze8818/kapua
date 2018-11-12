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

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * {@link KapuaQuery} definition.
 *
 * @param <E> query entity domain
 */
public interface KapuaQuery<E extends KapuaEntity> {

    /**
     * Gets the fetch attribute names list.
     *
     * @return The fetch attribute names list.
     */
    @XmlElementWrapper(name = "fetchAttributeName")
    @XmlElement(name = "fetchAttributeName")
    List<String> getFetchAttributes();

    /**
     * Adds an attribute to the fetch attribute names list
     *
     * @param fetchAttribute The fetch attribute to add to the list.
     * @since 1.0.0
     */
    void addFetchAttributes(String fetchAttribute);

    /**
     * Sets the fetch attribute names list.<br>
     * This list is a list of optional attributes of a {@link KapuaEntity} that can be fetched when querying.
     *
     * @param fetchAttributeNames The fetch attribute names list.
     * @since 1.0.0
     */
    void setFetchAttributes(List<String> fetchAttributeNames);

    /**
     * Get the scope {@link KapuaId} in which to query.
     *
     * @return The scope {@link KapuaId} in which to query.
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getScopeId();

    /**
     * Set the scope {@link KapuaId} in which to query.
     *
     * @param scopeId The scope {@link KapuaId} in which to query.
     * @since 1.0.0
     */
    void setScopeId(KapuaId scopeId);

    /**
     * Gets the {@link KapuaQuery} {@link QueryPredicate}s.
     *
     * @return The {@link KapuaQuery} {@link QueryPredicate}s.
     * @since 1.0.0
     */
    @XmlTransient
    // @XmlElement(name = "predicate")
    QueryPredicate getPredicate();

    /**
     * Sets the {@link KapuaQuery} {@link QueryPredicate}s.<br>
     * The {@link QueryPredicate} can be a simple {@link AttributePredicate} or a combination
     * of them by using the {@link AndPredicate}
     *
     * @param queryPredicate The {@link KapuaQuery} {@link QueryPredicate}s.
     * @since 1.0.0
     */
    void setPredicate(QueryPredicate queryPredicate);

    /**
     * Gets the {@link KapuaQuery} {@link KapuaSortCriteria}
     *
     * @return The {@link KapuaQuery} {@link KapuaSortCriteria}
     * @since 1.0.0
     */
    @XmlTransient
    // @XmlElement(name = "sortCriteria")
    KapuaSortCriteria getSortCriteria();

    /**
     * Sets the {@link KapuaQuery} {@link KapuaSortCriteria}.
     *
     * @param sortCriteria The {@link KapuaQuery} {@link KapuaSortCriteria}.
     * @since 1.0.0
     */
    void setSortCriteria(KapuaSortCriteria sortCriteria);

    /**
     * Gets the {@link KapuaQuery} offset.
     *
     * @return The {@link KapuaQuery} offset.
     * @since 1.0.0
     */
    @XmlElement(name = "offset")
    Integer getOffset();

    /**
     * Set the {@link KapuaQuery} offset in the result set from which start query.<br>
     * If set to {@code null} the {@link KapuaQuery} will start from the first result found.
     * This also mean that {@link #setOffset(Integer)} with {@code 0} or {@code null} will produce the same result.<br>
     * This method and {@link #setLimit(Integer)} are meant to be used to paginate through the result set.
     *
     * @param offset The {@link KapuaQuery} offset.
     * @since 1.0.0
     */
    void setOffset(Integer offset);

    /**
     * Gets the {@link KapuaQuery} limit.
     *
     * @return The {@link KapuaQuery} limit.
     * @since 1.0.0
     */
    @XmlElement(name = "limit")
    Integer getLimit();

    /**
     * Sets max number of result that will be fetched by this {@link KapuaEntity}.<br>
     * If set to {@code null} the {@link KapuaQuery} will be unlimited.<br>
     * This method and {@link #setOffset(Integer)} are meant to be used to paginate through the result set.
     *
     * @param limit The max number of result that will be fetched by this {@link KapuaEntity}.
     * @since 1.0.0
     */
    void setLimit(Integer limit);
}
