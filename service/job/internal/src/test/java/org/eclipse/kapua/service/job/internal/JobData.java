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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;

import javax.inject.Singleton;

@Singleton
public class JobData {

    // Step scratchpad data
    public Job job;
    public JobCreator jobCreator;
    public KapuaId currentJobId;

    public JobData() {
        cleanup();
    }

    public void cleanup() {
        jobCreator = null;
        job = null;
        currentJobId = null;
    }
}
