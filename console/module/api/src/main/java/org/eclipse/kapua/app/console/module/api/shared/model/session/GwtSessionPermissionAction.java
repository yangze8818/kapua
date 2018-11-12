/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.shared.model.session;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum GwtSessionPermissionAction implements IsSerializable {
    read, //
    write, //
    delete, //
    connect, //
    execute
}
