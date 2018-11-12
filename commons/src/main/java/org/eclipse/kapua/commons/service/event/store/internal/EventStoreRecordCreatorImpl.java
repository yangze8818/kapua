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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * KapuaEvent creator service implementation.
 * 
 * @since 1.0
 * 
 */
public class EventStoreRecordCreatorImpl extends AbstractKapuaEntityCreator<EventStoreRecord> implements EventStoreRecordCreator {

    private static final long serialVersionUID = 1048699703033893534L;

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public EventStoreRecordCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

}