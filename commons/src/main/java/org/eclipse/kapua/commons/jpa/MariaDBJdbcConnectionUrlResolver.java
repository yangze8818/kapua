/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

/**
 * MariaDB Jdbc url connection resolver implementation
 *
 * @since 1.0
 */
public class MariaDBJdbcConnectionUrlResolver implements JdbcConnectionUrlResolver {

    @Override
    public String connectionUrl() {
        SystemSetting config = SystemSetting.getInstance();

        // Mandatory connection parameters
        String dbName = config.getString(SystemSettingKey.DB_NAME);
        String dbConnectionScheme = config.getString(SystemSettingKey.DB_CONNECTION_SCHEME);
        String dbConnectionHost = config.getString(SystemSettingKey.DB_CONNECTION_HOST);
        String dbConnectionPort = config.getString(SystemSettingKey.DB_CONNECTION_PORT);

        StringBuilder dbConnectionString = new StringBuilder().append(dbConnectionScheme)
                .append("://")
                .append(dbConnectionHost)
                .append(":")
                .append(dbConnectionPort)
                .append("/")
                .append(dbName)
                .append("?");

        // Optional connection parameters
        String useTimezone = config.getString(SystemSettingKey.DB_USE_TIMEZONE);
        if (useTimezone != null) {
            dbConnectionString.append("useTimezone=")
                    .append(useTimezone)
                    .append("&");
        }

        String useLegacyDatetimeCode = config.getString(SystemSettingKey.DB_USE_LEGACY_DATETIME_CODE);
        if (useLegacyDatetimeCode != null) {
            dbConnectionString.append("useLegacyDatetimeCode=")
                    .append(useLegacyDatetimeCode)
                    .append("&");
        }

        String serverTimezone = config.getString(SystemSettingKey.DB_SERVER_TIMEZONE);
        if (serverTimezone != null) {
            dbConnectionString.append("serverTimezone=")
                    .append(serverTimezone)
                    .append("&");
        }

        String characterEncoding = config.getString(SystemSettingKey.DB_CHAR_ENCODING);
        if (characterEncoding != null) {
            dbConnectionString.append("characterEncoding=")
                    .append(characterEncoding)
                    .append("&");
        }

        // Unmodifiable parameters
        dbConnectionString.append("allowMultiQueries=true");

        return dbConnectionString.toString();
    }

}
