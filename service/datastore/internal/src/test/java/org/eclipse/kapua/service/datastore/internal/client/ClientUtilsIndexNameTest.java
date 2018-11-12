/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.client;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.AbstractMessageStoreServiceTest;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;

import org.junit.Assert;
import org.junit.Test;

public class ClientUtilsIndexNameTest extends AbstractMessageStoreServiceTest {

    private static final KapuaId ONE = new KapuaEid(BigInteger.ONE);
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final MessageStoreService MESSAGE_STORE_SERVICE = LOCATOR.getService(MessageStoreService.class);

    @Test
    public void test1() {
        final Instant instant = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        try {
            // Index by Week
            Assert.assertEquals("1-2017-01", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK));

            // Index by Day
            Assert.assertEquals("1-2017-01-01", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_DAY));

            // Index by Hour
            Assert.assertEquals("1-2017-01-01-00", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_HOUR));
        } catch (KapuaException kaex) {
            Assert.fail("Error while generating index name");
        }
    }

    @Test
    public void test2() {
        final Instant instant = ZonedDateTime.of(2017, 1, 8, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        try {
            // Index by Week
            Assert.assertEquals("1-2017-02", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK));

            // Index by Day
            Assert.assertEquals("1-2017-02-01", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_DAY));

            // Index by Hour
            Assert.assertEquals("1-2017-02-01-00", DatastoreUtils.getDataIndexName(ONE, instant.toEpochMilli(), DatastoreUtils.INDEXING_WINDOW_OPTION_HOUR));
        } catch (KapuaException kaex) {
            Assert.fail("Error while generating index name");
        }
    }
}
