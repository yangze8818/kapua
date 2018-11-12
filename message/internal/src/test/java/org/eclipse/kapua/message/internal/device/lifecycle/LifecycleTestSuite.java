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
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        KapuaAppsMessageTest.class,
        KapuaBirthMessageTest.class,
        KapuaDisconnectMessageTest.class,
        KapuaMissingMessageTest.class,
        KapuaNotifyMessageTest.class,
        KapuaUnmatchedMessageTest.class
})
public class LifecycleTestSuite {
}
