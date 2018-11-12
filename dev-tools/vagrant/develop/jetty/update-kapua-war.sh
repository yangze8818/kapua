#!/usr/bin/env bash
#*******************************************************************************
# Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
#*******************************************************************************
echo 'remove old symbolic link to console web application'
rm /var/lib/jetty/webapps/admin.war

echo 'remove old symbolic link to api web application'
rm /var/lib/jetty/webapps/api.war

echo 'create symbolic link to console web application'
ln -s /kapua/console/web/target/admin.war /var/lib/jetty/webapps/admin.war

echo 'create symbolic link to api web application'
ln -s /kapua/rest-api/web/target/api.war /var/lib/jetty/webapps/api.war
