/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.MessageListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;

/**
 * {@link DatastoreObjectFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DatastoreObjectFactoryImpl implements DatastoreObjectFactory {

    @Override
    public ChannelInfoQuery newChannelInfoQuery(KapuaId scopeId) {
        return new ChannelInfoQueryImpl(scopeId);
    }

    @Override
    public ChannelInfoListResult newChannelInfoListResult() {
        return new ChannelInfoListResultImpl();
    }

    @Override
    public ClientInfoQuery newClientInfoQuery(KapuaId scopeId) {
        return new ClientInfoQueryImpl(scopeId);
    }

    @Override
    public ClientInfoListResult newClientInfoListResult() {
        return new ClientInfoListResultImpl();
    }

    @Override
    public MessageQuery newDatastoreMessageQuery(KapuaId scopeId) {
        return new MessageQueryImpl(scopeId);
    }

    @Override
    public MessageListResult newDatastoreMessageListResult() {
        return new MessageListResultImpl();
    }

    @Override
    public MetricInfoQuery newMetricInfoQuery(KapuaId scopeId) {
        return new MetricInfoQueryImpl(scopeId);
    }

    @Override
    public MetricInfoListResult newMetricInfoListResult() {
        return new MetricInfoListResultImpl();
    }

}
