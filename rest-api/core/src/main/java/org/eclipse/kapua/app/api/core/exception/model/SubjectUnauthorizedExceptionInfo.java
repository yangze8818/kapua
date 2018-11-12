/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.core.exception.model;

import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.shiro.exception.SubjectUnauthorizedException;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subjectUnauthorizedExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectUnauthorizedExceptionInfo extends KapuaExceptionInfo {

    @XmlElement(name = "permission")
    private Permission permission;

    protected SubjectUnauthorizedExceptionInfo() {
        super();
    }

    public SubjectUnauthorizedExceptionInfo(Status httpStatus, SubjectUnauthorizedException kapuaException) {
        super(httpStatus, kapuaException.getCode(), kapuaException);

        setPermission(kapuaException.getPermission());
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
