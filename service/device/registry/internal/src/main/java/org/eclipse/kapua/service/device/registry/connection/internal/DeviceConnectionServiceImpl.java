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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.DeviceDomains;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionAttributes;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DeviceConnectionService exposes APIs to retrieve Device connections under a scope.
 * It includes APIs to find, list, and update devices connections associated with a scope.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceConnectionServiceImpl extends
        //        AbstractKapuaConfigurableResourceLimitedService<DeviceConnection, DeviceConnectionCreator, DeviceConnectionService, DeviceConnectionListResult, DeviceConnectionQuery, DeviceConnectionFactory>
        AbstractKapuaConfigurableService
        implements DeviceConnectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceConnectionServiceImpl.class);

    public DeviceConnectionServiceImpl() {
        this(DeviceEntityManagerFactory.instance());
    }

    public DeviceConnectionServiceImpl(DeviceEntityManagerFactory deviceEntityManagerFactory) {
        //        super(DeviceConnectionService.class.getName(), DEVICE_CONNECTION_DOMAIN, deviceEntityManagerFactory, DeviceConnectionService.class, DeviceConnectionFactory.class);
        super(DeviceConnectionService.class.getName(), DeviceDomains.DEVICE_CONNECTION_DOMAIN, deviceEntityManagerFactory);
    }

    @Override
    public DeviceConnection create(DeviceConnectionCreator deviceConnectionCreator)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnectionCreator, "deviceConnectionCreator");
        ArgumentValidator.notNull(deviceConnectionCreator.getScopeId(), "deviceConnectionCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(deviceConnectionCreator.getClientId(), "deviceConnectionCreator.clientId");
        ArgumentValidator.notNull(deviceConnectionCreator.getUserId(), "deviceConnectionCreator.userId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.write, null));

        return entityManagerSession.onTransactedInsert(em -> DeviceConnectionDAO.create(em, deviceConnectionCreator));
    }

    @Override
    public DeviceConnection update(DeviceConnection deviceConnection)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnection, "deviceConnection");
        ArgumentValidator.notNull(deviceConnection.getId(), "deviceConnection.id");
        ArgumentValidator.notNull(deviceConnection.getScopeId(), "deviceConnection.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.write, null));

        return entityManagerSession.onTransactedResult(em -> {
            if (DeviceConnectionDAO.find(em, deviceConnection.getScopeId(), deviceConnection.getId()) == null) {
                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnection.getId());
            }
            return DeviceConnectionDAO.update(em, deviceConnection);
        });
    }

    @Override
    public DeviceConnection find(KapuaId scopeId, KapuaId entityId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.onResult(em -> DeviceConnectionDAO.find(em, scopeId, entityId));
    }

    @Override
    public DeviceConnection findByClientId(KapuaId scopeId, String clientId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notEmptyOrNull(clientId, "clientId");

        //
        // Build query
        DeviceConnectionQueryImpl query = new DeviceConnectionQueryImpl(scopeId);
        QueryPredicate predicate = new AttributePredicateImpl<>(DeviceConnectionAttributes.CLIENT_ID, clientId);
        query.setPredicate(predicate);

        //
        // Query and parse result
        DeviceConnectionListResult result = query(query);
        return result.getFirstItem();
    }

    @Override
    public DeviceConnectionListResult query(KapuaQuery<DeviceConnection> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> DeviceConnectionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<DeviceConnection> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> DeviceConnectionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceConnectionId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnectionId, "deviceConnection.id");
        ArgumentValidator.notNull(scopeId, "deviceConnection.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.write, null));

        entityManagerSession.onTransactedAction(em -> {
            if (DeviceConnectionDAO.find(em, scopeId, deviceConnectionId) == null) {
                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnectionId);
            }
            DeviceConnectionDAO.delete(em, scopeId, deviceConnectionId);
        });
    }

    @Override
    public void connect(DeviceConnectionCreator creator)
            throws KapuaException {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnect(KapuaId scopeId, String clientId)
            throws KapuaException {
        // TODO Auto-generated method stub

    }

    //@ListenServiceEvent(fromAddress="account")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOGGER.info("DeviceConnectionService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteConnectionByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    private void deleteConnectionByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionFactory deviceConnectionFactory = locator.getFactory(DeviceConnectionFactory.class);

        DeviceConnectionQuery query = deviceConnectionFactory.newQuery(accountId);

        DeviceConnectionListResult deviceConnectionsToDelete = query(query);

        for (DeviceConnection dc : deviceConnectionsToDelete.getItems()) {
            delete(dc.getScopeId(), dc.getId());
        }
    }

}
