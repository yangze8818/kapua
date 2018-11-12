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
package org.eclipse.kapua.commons.service.event.store.api;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * KapuaEvent factory service definition.
 * 
 * @since 1.0
 * 
 */
public interface EventStoreFactory extends KapuaEntityFactory<EventStoreRecord, EventStoreRecordCreator, EventStoreRecordQuery, EventStoreRecordListResult> {

}