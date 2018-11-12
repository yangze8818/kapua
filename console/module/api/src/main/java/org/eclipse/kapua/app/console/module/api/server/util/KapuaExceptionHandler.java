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
package org.eclipse.kapua.app.console.module.api.server.util;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaEntityUniquenessException;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaMaxNumberOfItemsReachedException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurationErrorCodes;
import org.eclipse.kapua.commons.configuration.KapuaConfigurationException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;
import org.eclipse.kapua.service.authorization.shiro.exception.SubjectUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KapuaExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(KapuaExceptionHandler.class);

    private KapuaExceptionHandler() {
    }

    public static void handle(Throwable t) throws GwtKapuaException {
        if (t instanceof KapuaUnauthenticatedException) {

            // sessions has expired
            throw new GwtKapuaException(GwtKapuaErrorCode.UNAUTHENTICATED, t);
        } else if (t instanceof KapuaAuthenticationException) {

            KapuaAuthenticationException ke = (KapuaAuthenticationException) t;
            String cause = ke.getCode().name();

            // INVALID_USERNAME_PASSWORD

            if (cause.equals(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS.name())) {
                throw new GwtKapuaException(GwtKapuaErrorCode.INVALID_USERNAME_PASSWORD, t);
            }
            if (cause.equals(KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL.name())) {
                throw new GwtKapuaException(GwtKapuaErrorCode.INVALID_USERNAME_PASSWORD, t);
            }

            // LOCKED_USER

            if (cause.equals(KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL.name())) {
                throw new GwtKapuaException(GwtKapuaErrorCode.LOCKED_USER, t);
            }
            if (cause.equals(KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL.name())) {
                throw new GwtKapuaException(GwtKapuaErrorCode.LOCKED_USER, t);
            }
            if (cause.equals(KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS.name())) {
                throw new GwtKapuaException(GwtKapuaErrorCode.LOCKED_USER, t);
            }

            // default
            throw new GwtKapuaException(GwtKapuaErrorCode.INVALID_USERNAME_PASSWORD, t);
        } else if (t instanceof KapuaRuntimeException && ((KapuaRuntimeException) t).getCode().equals(KapuaErrorCodes.ENTITY_ALREADY_EXISTS) ||
                   t.getCause() instanceof KapuaRuntimeException && ((KapuaRuntimeException) t.getCause()).getCode().equals(KapuaErrorCodes.ENTITY_ALREADY_EXISTS)) {
            logger.error("entity already exists", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.ENTITY_ALREADY_EXISTS, t, t.getLocalizedMessage());
        } else if (t instanceof KapuaException && ((KapuaException) t).getCode().equals(KapuaErrorCodes.INTERNAL_ERROR)) {
            logger.error("internal service error", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.INTERNAL_ERROR, t, t.getLocalizedMessage());
        } else if(t instanceof KapuaException && ((KapuaException) t).getCode().name().equals(KapuaErrorCodes.PARENT_LIMIT_EXCEEDED_IN_CONFIG.name())) {
            logger.warn("Child accounts limitation error", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.PARENT_LIMIT_EXCEEDED_IN_CONFIG, t, t.getLocalizedMessage());
        } else if(t instanceof KapuaException && ((KapuaException) t).getCode().name().equals(KapuaErrorCodes.SUBJECT_UNAUTHORIZED.name())) {
            logger.warn("User unauthorize", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.SUBJECT_UNAUTHORIZED, t, ((SubjectUnauthorizedException)t).getPermission().toString());
        } else if (t instanceof KapuaException && ((KapuaException) t).getCode().name().equals(KapuaErrorCodes.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT.name())) {
            logger.warn("Entity already exist in another account", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, t, t.getLocalizedMessage());
        } else if (t instanceof KapuaException && ((KapuaException) t).getCode().name().equals(KapuaErrorCodes.DUPLICATE_NAME.name())) {
            logger.warn("Entity already exist with the same name", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.DUPLICATE_NAME, t, t.getLocalizedMessage());
        } else if(t instanceof KapuaConfigurationException && ((KapuaConfigurationException) t).getCode().name().equals(KapuaConfigurationErrorCodes.SELF_LIMIT_EXCEEDED_IN_CONFIG.name())) {
            logger.warn("Parent account limitation error", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.SELF_LIMIT_EXCEEDED_IN_CONFIG, t, t.getLocalizedMessage());
        } else if(t instanceof KapuaEntityNotFoundException) {
            logger.warn("Entity not found", t);
            KapuaEntityNotFoundException kapuaEntityNotFoundException = (KapuaEntityNotFoundException)t;
            throw new GwtKapuaException(GwtKapuaErrorCode.ENTITY_NOT_FOUND, t, kapuaEntityNotFoundException.getEntityType(), kapuaEntityNotFoundException.getEntityName());
        } else if (t instanceof KapuaEntityUniquenessException) {
            logger.warn("Entity uniqueness error", t);
            KapuaEntityUniquenessException kapuaEntityUniquenessException = (KapuaEntityUniquenessException) t;
            StringBuilder errorFieldsSb = new StringBuilder("(");
            for (Map.Entry<String, Object> entry : kapuaEntityUniquenessException.getUniquesFieldValues()) {
                errorFieldsSb.append(entry.getKey()).append(", ");
            }
            errorFieldsSb.delete(errorFieldsSb.length() - 2, errorFieldsSb.length()).append(") - (");
            for (Map.Entry<String, Object> entry : kapuaEntityUniquenessException.getUniquesFieldValues()) {
                errorFieldsSb.append(entry.getValue()).append(", ");
            }
            errorFieldsSb.delete(errorFieldsSb.length() - 2, errorFieldsSb.length()).append(")");

            throw new GwtKapuaException(GwtKapuaErrorCode.ENTITY_UNIQUENESS, t, errorFieldsSb.toString());
        } else if (t instanceof KapuaIllegalArgumentException) {
            KapuaIllegalArgumentException kiae = (KapuaIllegalArgumentException)t;
            if(kiae.getArgumentName().equals("name") && kiae.getArgumentValue().equals(SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME))) {
                throw new GwtKapuaException(GwtKapuaErrorCode.OPERATION_NOT_ALLOWED_ON_ADMIN_USER, t);
            } else {
                throw new GwtKapuaException(GwtKapuaErrorCode.ILLEGAL_ARGUMENT, t, ((KapuaIllegalArgumentException) t).getArgumentName(), ((KapuaIllegalArgumentException) t).getArgumentValue());
            }
        } else if (t instanceof KapuaException && ((KapuaException) t).getCode().name().equals(KapuaErrorCodes.BUNDLE_START_ERROR)){
            logger.warn("Bundle could not be started", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.BUNDLE_START_ERROR, t, t.getLocalizedMessage());
        } else if (t instanceof KapuaException && ((KapuaException) t).getCode().name().equals(KapuaErrorCodes.BUNDLE_STOP_ERROR)){
            logger.warn("Bundle could not be stoped", t);
            throw new GwtKapuaException(GwtKapuaErrorCode.BUNDLE_STOP_ERROR, t, t.getLocalizedMessage());
        } else if (t instanceof KapuaMaxNumberOfItemsReachedException){
            throw new GwtKapuaException(GwtKapuaErrorCode.MAX_NUMBER_OF_ITEMS_REACHED, t, ((KapuaMaxNumberOfItemsReachedException) t).getArgValue());
        } else {
            // all others => log and throw internal error code
            logger.warn("RPC service non-application error", t);
            throw GwtKapuaException.internalError(t, t.getLocalizedMessage());
        }
    }
}
