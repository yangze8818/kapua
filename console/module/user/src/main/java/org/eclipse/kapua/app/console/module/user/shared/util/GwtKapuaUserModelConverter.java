/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.shared.util;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser.GwtUserStatus;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUserQuery;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserStatus;

import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.service.user.UserAttributes;

/**
 * Utility class for convertKapuaId {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 */
public class GwtKapuaUserModelConverter {

    private GwtKapuaUserModelConverter() {
    }

    /**
     * Converts a {@link GwtUserQuery} into a {@link UserQuery} object for backend usage
     *
     * @param loadConfig   the load configuration
     * @param gwtUserQuery the {@link GwtUserQuery} to convertKapuaId
     * @return the converted {@link UserQuery}
     */
    public static UserQuery convertUserQuery(PagingLoadConfig loadConfig, GwtUserQuery gwtUserQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        UserFactory userFactory = locator.getFactory(UserFactory.class);

        AndPredicateImpl predicate = new AndPredicateImpl();
        // Convert query
        UserQuery userQuery = userFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtUserQuery.getScopeId()));
        if (gwtUserQuery.getName() != null && !gwtUserQuery.getName().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(UserAttributes.NAME, gwtUserQuery.getName(), Operator.LIKE));
        }
        if (gwtUserQuery.getUserStatus() != null && !gwtUserQuery.getUserStatus().equals(GwtUserStatus.ANY.toString())) {
            predicate.and(new AttributePredicateImpl<UserStatus>(UserAttributes.STATUS, convertUserStatus(gwtUserQuery.getUserStatus()), Operator.EQUAL));
        }
        if (gwtUserQuery.getPhoneNumber() != null && !gwtUserQuery.getPhoneNumber().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(UserAttributes.PHONE_NUMBER, gwtUserQuery.getPhoneNumber(), Operator.LIKE));
        }
        if (gwtUserQuery.getExpirationDate() != null) {
            predicate.and(new AttributePredicateImpl<Date>(UserAttributes.EXPIRATION_DATE, gwtUserQuery.getExpirationDate(), Operator.EQUAL));
        }
        if (gwtUserQuery.getEmail() != null && !gwtUserQuery.getEmail().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(UserAttributes.EMAIL, gwtUserQuery.getEmail(), Operator.LIKE));
        }
        if (gwtUserQuery.getDisplayName() != null && !gwtUserQuery.getDisplayName().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(UserAttributes.DISPLAY_NAME, gwtUserQuery.getDisplayName(), Operator.LIKE));
        }
        userQuery.setOffset(loadConfig.getOffset());
        userQuery.setLimit(loadConfig.getLimit());
        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? UserAttributes.NAME : loadConfig.getSortField();
        if (sortField.equals("username")) {
            sortField = UserAttributes.NAME;
        } else if (sortField.equals("modifiedByName")) {
            sortField = UserAttributes.MODIFIED_BY;
        } else if (sortField.equals("expirationDateFormatted")) {
            sortField = UserAttributes.EXPIRATION_DATE;
        } else if (sortField.equals("modifiedOnFormatted")) {
            sortField = UserAttributes.MODIFIED_ON;
        } else if (sortField.equals("createdOnFormatted")) {
            sortField = UserAttributes.CREATED_ON;
        }
        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);
        userQuery.setSortCriteria(sortCriteria);
        userQuery.setPredicate(predicate);
        //
        // Return converted
        return userQuery;
    }

    private static UserStatus convertUserStatus(String userStatus) {
        return UserStatus.valueOf(userStatus);
    }

    public static UserStatus convertUserStatus(GwtUserStatus gwtUserStatus) {
        return UserStatus.valueOf(gwtUserStatus.toString());
    }

}
