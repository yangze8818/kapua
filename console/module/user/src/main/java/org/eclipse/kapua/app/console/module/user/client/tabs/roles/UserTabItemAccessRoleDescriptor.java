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
package org.eclipse.kapua.app.console.module.user.client.tabs.roles;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.AccessInfoSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.RoleSessionPermission;
import org.eclipse.kapua.app.console.module.user.client.UserView;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class UserTabItemAccessRoleDescriptor extends AbstractEntityTabDescriptor<GwtUser, UserTabItemAccessRole, UserView> {

    @Override
    public UserTabItemAccessRole getTabViewInstance(UserView view, GwtSession currentSession) {
        return new UserTabItemAccessRole(currentSession);
    }

    @Override
    public String getViewId() {
        return "user.role";
    }

    @Override
    public Integer getOrder() {
        return 300;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(RoleSessionPermission.read()) && currentSession.hasPermission(AccessInfoSessionPermission.read());
    }
}
