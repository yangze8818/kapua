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
package org.eclipse.kapua.app.console.module.device.server;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceConnectionService;
import org.eclipse.kapua.app.console.module.device.shared.util.GwtKapuaDeviceModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.util.KapuaGwtDeviceModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * The server side implementation of the RPC service.
 */
public class GwtDeviceConnectionServiceImpl extends KapuaRemoteServiceServlet implements GwtDeviceConnectionService {

    private static final long serialVersionUID = 3314502846487119577L;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceConnectionService DEVICE_CONNECTION_SERVICE = LOCATOR.getService(DeviceConnectionService.class);

    private static final UserService USER_SERVICE = LOCATOR.getService(UserService.class);
    private static final UserFactory USER_FACTORY = LOCATOR.getFactory(UserFactory.class);

    @Override
    public PagingLoadResult<GwtDeviceConnection> query(PagingLoadConfig loadConfig, GwtDeviceConnectionQuery gwtDeviceConnectionQuery) throws GwtKapuaException {

        int totalLength = 0;
        List<GwtDeviceConnection> gwtDeviceConnections = new ArrayList<GwtDeviceConnection>();
        try {
            DeviceConnectionQuery query = GwtKapuaDeviceModelConverter.convertConnectionQuery(loadConfig, gwtDeviceConnectionQuery);

            KapuaListResult<DeviceConnection> deviceConnections = DEVICE_CONNECTION_SERVICE.query(query);
            totalLength = (int) DEVICE_CONNECTION_SERVICE.count(query);

            if (!deviceConnections.isEmpty()) {
                Map<String, String> users = new HashMap<String, String>();
                final UserQuery userQuery = USER_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtDeviceConnectionQuery.getScopeId()));

                UserListResult userList = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                    @Override
                    public UserListResult call() throws Exception {
                        return USER_SERVICE.query(userQuery);
                    }
                });
                for (User user : userList.getItems()) {
                    users.put(user.getId().toCompactId(), user.getName());
                }

                for (DeviceConnection dc : deviceConnections.getItems()) {
                    GwtDeviceConnection gwtDeviceConnection = KapuaGwtDeviceModelConverter.convertDeviceConnection(dc);
                    if (dc.getUserId() != null) {
                        gwtDeviceConnection.setUserName(users.get(dc.getUserId().toCompactId()));
                    }

                    if (dc.getReservedUserId() != null) {
                        gwtDeviceConnection.setReservedUserName(users.get(dc.getReservedUserId().toCompactId()));
                    }

                    gwtDeviceConnections.add(gwtDeviceConnection);
                }
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtDeviceConnection>(gwtDeviceConnections, loadConfig.getOffset(), totalLength);
    }

    @Override
    public GwtDeviceConnection find(String scopeIdString, String deviceConnectionIdString) throws GwtKapuaException {
        KapuaId deviceConnectionId = KapuaEid.parseCompactId(deviceConnectionIdString);
        KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);

        GwtDeviceConnection gwtDeviceConnection = null;
        try {

            gwtDeviceConnection = KapuaGwtDeviceModelConverter.convertDeviceConnection(DEVICE_CONNECTION_SERVICE.find(scopeId, deviceConnectionId));
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtDeviceConnection;
    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getConnectionInfo(String scopeIdString, String gwtDeviceConnectionId) throws GwtKapuaException {
        KapuaId deviceConnectionId = KapuaEid.parseCompactId(gwtDeviceConnectionId);
        final KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);

        List<GwtGroupedNVPair> deviceConnectionPropertiesPairs = new ArrayList<GwtGroupedNVPair>();
        try {
            final DeviceConnection deviceConnection = DEVICE_CONNECTION_SERVICE.find(scopeId, deviceConnectionId);
            User connectionUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return USER_SERVICE.find(scopeId, deviceConnection.getUserId());
                }
            });
            User createdUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return USER_SERVICE.find(scopeId, deviceConnection.getCreatedBy());
                }
            });
            User modifiedUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return USER_SERVICE.find(scopeId, deviceConnection.getModifiedBy());
                }
            });

            User reservedUser;
            if (deviceConnection.getReservedUserId() != null) {
                reservedUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                    @Override
                    public User call() throws Exception {
                        return USER_SERVICE.find(scopeId, deviceConnection.getReservedUserId());
                    }
                });
            } else {
                reservedUser = null;
            }

            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionStatus", deviceConnection.getStatus().toString()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionModifiedOn", deviceConnection.getModifiedOn()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionModifiedBy", modifiedUser != null ? modifiedUser.getName() : null));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionProtocol", deviceConnection.getProtocol()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionClientId", deviceConnection.getClientId()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionUser", connectionUser != null ? connectionUser.getName() : null));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionClientIp", deviceConnection.getClientIp()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionServerIp", deviceConnection.getServerIp()));
            GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = null;
            if (deviceConnection.getUserCouplingMode() != null) {
                gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.valueOf(deviceConnection.getUserCouplingMode().name());
            }
            deviceConnectionPropertiesPairs
                    .add(new GwtGroupedNVPair("connectionUserCouplingModeInfo", "connectionUserCouplingMode", gwtConnectionUserCouplingMode != null ? gwtConnectionUserCouplingMode.getLabel() : null));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionUserCouplingModeInfo", "connectionReservedUser", reservedUser != null ? reservedUser.getName() : null));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionUserCouplingModeInfo", "allowUserChange", deviceConnection.getAllowUserChange()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionFirstEstablishedOn", deviceConnection.getCreatedOn()));
            deviceConnectionPropertiesPairs.add(new GwtGroupedNVPair("connectionInfo", "connectionFirstEstablishedBy", createdUser != null ? createdUser.getName() : null));

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(deviceConnectionPropertiesPairs);
    }

}
