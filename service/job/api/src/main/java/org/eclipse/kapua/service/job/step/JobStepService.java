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
package org.eclipse.kapua.service.job.step;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * {@link JobStepService} exposes APIs to manage JobStep objects.<br>
 * It includes APIs to create, update, find, list and delete Jobs.<br>
 * Instances of the JobStepService can be acquired through the ServiceLocator object.
 *
 * @since 1.0
 */
public interface JobStepService extends KapuaEntityService<JobStep, JobStepCreator>,
        KapuaUpdatableEntityService<JobStep>,
        KapuaConfigurableService {

    /**
     * Returns the {@link JobStepListResult} with elements matching the provided query.
     *
     * @param query The {@link JobStepQuery} used to filter results.
     * @return The {@link JobStepListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    JobStepListResult query(KapuaQuery<JobStep> query)
            throws KapuaException;
}
