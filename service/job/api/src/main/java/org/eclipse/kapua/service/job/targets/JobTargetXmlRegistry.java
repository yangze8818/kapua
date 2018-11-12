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
package org.eclipse.kapua.service.job.targets;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link JobTarget} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class JobTargetXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public JobTarget newJobTarget() {
        return JOB_TARGET_FACTORY.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public JobTargetCreator newJobTargetCreator() {
        return JOB_TARGET_FACTORY.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public JobTargetListResult newJobTargetListResult() {
        return JOB_TARGET_FACTORY.newListResult();
    }

    public JobTargetQuery newQuery() {
        return JOB_TARGET_FACTORY.newQuery(null);
    }
}
