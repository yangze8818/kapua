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
package org.eclipse.kapua.service.device.management.asset.job;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.operation.AbstractTargetProcessor;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetManagementService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.job.definition.DeviceAssetWritePropertyKeys;
import org.eclipse.kapua.service.job.operation.TargetOperation;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

public class DeviceAssetWriteTargetProcessor extends AbstractTargetProcessor implements TargetOperation {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceAssetManagementService ASSET_MANAGEMENT_SERVICE = LOCATOR.getService(DeviceAssetManagementService.class);

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    public void processTarget(JobTarget jobTarget) throws KapuaException {
        setContext(jobContext, stepContext);

        DeviceAssets assets = stepContextWrapper.getStepProperty(DeviceAssetWritePropertyKeys.ASSETS, DeviceAssets.class);
        Long timeout = stepContextWrapper.getStepProperty(DeviceAssetWritePropertyKeys.TIMEOUT, Long.class);

        KapuaSecurityUtils.doPrivileged(() -> ASSET_MANAGEMENT_SERVICE.write(jobTarget.getScopeId(), jobTarget.getJobTargetId(), assets, timeout));
    }
}
