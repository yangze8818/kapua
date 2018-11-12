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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaEntityUniquenessException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionValidator;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionAttributes;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link RolePermission} service implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class RolePermissionServiceImpl extends AbstractKapuaService implements RolePermissionService {

    private static final RoleService ROLE_SERVICE = KapuaLocator.getInstance().getService(RoleService.class);

    public RolePermissionServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance());
    }

    @Override
    public RolePermission create(RolePermissionCreator rolePermissionCreator)
            throws KapuaException {
        ArgumentValidator.notNull(rolePermissionCreator, "rolePermissionCreator");
        ArgumentValidator.notNull(rolePermissionCreator.getRoleId(), "rolePermissionCreator.roleId");
        ArgumentValidator.notNull(rolePermissionCreator.getPermission(), "rolePermissionCreator.permission");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.write, rolePermissionCreator.getScopeId()));

        //
        // Check role existence
        if (ROLE_SERVICE.find(rolePermissionCreator.getScopeId(), rolePermissionCreator.getRoleId()) == null) {
            throw new KapuaEntityNotFoundException(Role.TYPE, rolePermissionCreator.getRoleId());
        }

        //
        // Check that the given permission matches the definition of the Domains.
        PermissionValidator.validatePermission(rolePermissionCreator.getPermission());

        //
        // If permission are created out of the role permission scope, check that the current user has the permission on the external scopeId.
        Permission permission = rolePermissionCreator.getPermission();
        if (permission.getTargetScopeId() == null || !permission.getTargetScopeId().equals(rolePermissionCreator.getScopeId())) {
            authorizationService.checkPermission(permission);
        }

        //
        // Check duplicates
        RolePermissionQuery query = new RolePermissionQueryImpl(rolePermissionCreator.getScopeId());
        query.setPredicate(
                new AndPredicateImpl(
                        new AttributePredicateImpl<>(RolePermissionAttributes.SCOPE_ID, rolePermissionCreator.getScopeId()),
                        new AttributePredicateImpl<>(RolePermissionAttributes.ROLE_ID, rolePermissionCreator.getRoleId()),
                        new AttributePredicateImpl<>(RolePermissionAttributes.PERMISSION_DOMAIN, rolePermissionCreator.getPermission().getDomain()),
                        new AttributePredicateImpl<>(RolePermissionAttributes.PERMISSION_ACTION, rolePermissionCreator.getPermission().getAction()),
                        new AttributePredicateImpl<>(RolePermissionAttributes.PERMISSION_TARGET_SCOPE_ID, rolePermissionCreator.getPermission().getTargetScopeId()),
                        new AttributePredicateImpl<>(RolePermissionAttributes.PERMISSION_GROUP_ID, rolePermissionCreator.getPermission().getGroupId()),
                        new AttributePredicateImpl<>(RolePermissionAttributes.PERMISSION_FORWARDABLE, rolePermissionCreator.getPermission().getForwardable())
                )
        );
        if (count(query) > 0) {
            List<Map.Entry<String, Object>> uniquesFieldValues = new ArrayList<>();

            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(RolePermissionAttributes.SCOPE_ID, rolePermissionCreator.getScopeId()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(RolePermissionAttributes.ROLE_ID, rolePermissionCreator.getRoleId()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(RolePermissionAttributes.PERMISSION_DOMAIN, rolePermissionCreator.getPermission().getDomain()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(RolePermissionAttributes.PERMISSION_ACTION, rolePermissionCreator.getPermission().getAction()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(RolePermissionAttributes.PERMISSION_TARGET_SCOPE_ID, rolePermissionCreator.getPermission().getTargetScopeId()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(RolePermissionAttributes.PERMISSION_GROUP_ID, rolePermissionCreator.getPermission().getGroupId()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(RolePermissionAttributes.PERMISSION_FORWARDABLE, rolePermissionCreator.getPermission().getForwardable()));

            throw new KapuaEntityUniquenessException(RolePermission.TYPE, uniquesFieldValues);
        }

        return entityManagerSession.onTransactedInsert(em -> RolePermissionDAO.create(em, rolePermissionCreator));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId rolePermissionId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(rolePermissionId, "rolePermissionId");

        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.onTransactedAction(em -> {
            if (RolePermissionDAO.find(em, scopeId, rolePermissionId) == null) {
                throw new KapuaEntityNotFoundException(RolePermission.TYPE, rolePermissionId);
            }

            RolePermissionDAO.delete(em, scopeId, rolePermissionId);
        });
    }

    @Override
    public RolePermission find(KapuaId scopeId, KapuaId rolePermissionId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(rolePermissionId, "rolePermissionId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.onResult(em -> RolePermissionDAO.find(em, scopeId, rolePermissionId));
    }

    @Override
    public RolePermissionListResult findByRoleId(KapuaId scopeId, KapuaId roleId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(roleId, "roleId");

        //
        // Build query
        RolePermissionQuery query = new RolePermissionQueryImpl(scopeId);
        query.setPredicate(new AttributePredicateImpl<>(RolePermissionAttributes.ROLE_ID, roleId));

        return query(query);
    }

    @Override
    public RolePermissionListResult query(KapuaQuery<RolePermission> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> RolePermissionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<RolePermission> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> RolePermissionDAO.count(em, query));
    }
}
