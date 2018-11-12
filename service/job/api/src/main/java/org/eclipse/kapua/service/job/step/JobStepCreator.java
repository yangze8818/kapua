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

import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link JobStepCreator} encapsulates all the information needed to create a new JobStep in the system.<br>
 * The data provided will be used to seed the new JobStep.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "jobStepCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobStepXmlRegistry.class, factoryMethod = "newJobStepCreator")
public interface JobStepCreator extends KapuaNamedEntityCreator<JobStep> {

    String getDescription();

    void setDescription(String description);

    KapuaId getJobId();

    void setJobId(KapuaId jobId);

    Integer getStepIndex();

    void setStepIndex(Integer stepIndex);

    KapuaId getJobStepDefinitionId();

    void setJobStepDefinitionId(KapuaId jobStepDefinitionId);

    <P extends JobStepProperty> List<P> getStepProperties();

    void setJobStepProperties(List<JobStepProperty> jobStepProperties);
}
