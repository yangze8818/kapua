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
package org.eclipse.kapua.client.gateway.spi.util;

import org.junit.Test;

public class StringsTest {

    @Test(expected = IllegalArgumentException.class)
    public void test1() {
        Strings.nonEmptyText(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test2() {
        Strings.nonEmptyText("", "foo");
    }

    @Test
    public void test3() {
        Strings.nonEmptyText("foo", "foo");
    }
}
