/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.driver;

import com.google.common.collect.Lists;
import com.ibm.jbatch.container.jsl.ExecutionElement;
import com.ibm.jbatch.container.jsl.ModelSerializerFactory;
import com.ibm.jbatch.container.servicesmanager.ServicesManagerImpl;
import com.ibm.jbatch.jsl.model.JSLJob;
import com.ibm.jbatch.jsl.model.Step;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.CannotBuildJobDefDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.CannotCleanJobDefFileDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.CannotCreateTmpDirDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.CannotWriteJobDefFileDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.CleanJobDataDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.ExecutionNotFoundDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.ExecutionNotRunningDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.JbatchDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.JobExecutionIsRunningDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.JobStartingDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.utils.JbatchUtil;
import org.eclipse.kapua.job.engine.jbatch.driver.utils.JobDefinitionBuildUtils;
import org.eclipse.kapua.job.engine.jbatch.persistence.KapuaJDBCPersistenceManagerImpl;
import org.eclipse.kapua.job.engine.jbatch.setting.KapuaJobEngineSetting;
import org.eclipse.kapua.job.engine.jbatch.setting.KapuaJobEngineSettingKeys;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepAttributes;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.operations.JobExecutionNotRunningException;
import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.operations.NoSuchJobInstanceException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Driver class for Java Batch API
 *
 * @since 1.0.0
 */
public class JbatchDriver {

    private static final Logger LOG = LoggerFactory.getLogger(JbatchDriver.class);

    private static final KapuaJobEngineSetting JOB_ENGINE_SETTING = KapuaJobEngineSetting.getInstance();

    private static final JobOperator JOB_OPERATOR = BatchRuntime.getJobOperator();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    private static final JobStepDefinitionService STEP_DEFINITION_SERVICE = LOCATOR.getService(JobStepDefinitionService.class);

    private JbatchDriver() {
    }

    /**
     * Builds the jBatch job name from the {@link Job#getScopeId()} and the {@link Job#getId()}.
     * <p>
     * Format is: job-{scopeIdShort}-{jobIdShort}
     *
     * @param scopeId The scopeId of the {@link Job}
     * @param jobId   The id of the {@link Job}
     * @return The jBatch {@link Job} name
     * @since 1.0.0
     */
    public static String getJbatchJobName(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        return String.format("job-%s-%s", scopeId.toCompactId(), jobId.toCompactId());
    }

    /**
     * Starts a jBatch job with data sourced from the Kapua {@link Job} definition.
     * <p>
     * It builds the XML jBatch job definition using the {@link JSLJob} model definition.
     * The generated XML is store in the {@link SystemUtils#getJavaIoTmpDir()} since the default configuration of jBatch requires a path name to start the jBatch job
     *
     * @param scopeId         The scopeId of the {@link Job}
     * @param jobId           The id of the {@link Job}
     * @param jobStartOptions The {@link JobStartOptions} for this start {@link org.eclipse.kapua.service.job.Job} request.
     * @throws CannotBuildJobDefDriverException     if the creation of the {@link JSLJob} fails
     * @throws CannotCreateTmpDirDriverException    if the temp directory for storing the XML job definition file cannot be created
     * @throws CannotCleanJobDefFileDriverException if the XML job definition file cannot be deleted, when existing
     * @throws CannotWriteJobDefFileDriverException if the XML job definition file cannot be created and written in the tmp directory
     * @throws JobExecutionIsRunningDriverException if the jBatch job has another {@link JobExecution} running
     * @throws JobStartingDriverException           if invoking {@link JobOperator#start(String, Properties)} throws an {@link Exception}
     */
    public static void startJob(@NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull JobStartOptions jobStartOptions)
            throws JbatchDriverException {

        String jobXmlDefinition;
        String jobName = JbatchDriver.getJbatchJobName(scopeId, jobId);
        try {
            JobStepQuery jobStepQuery = JOB_STEP_FACTORY.newQuery(scopeId);
            jobStepQuery.setPredicate(new AttributePredicateImpl<>(JobStepAttributes.JOB_ID, jobId));

            JobStepListResult jobSteps = JOB_STEP_SERVICE.query(jobStepQuery);
            jobSteps.sort(Comparator.comparing(JobStep::getStepIndex));

            List<ExecutionElement> jslExecutionElements = new ArrayList<>();
            Iterator<JobStep> jobStepIterator = jobSteps.getItems().iterator();
            while (jobStepIterator.hasNext()) {
                JobStep jobStep = jobStepIterator.next();

                Step jslStep = new Step();
                JobStepDefinition jobStepDefinition = STEP_DEFINITION_SERVICE.find(jobStep.getScopeId(), jobStep.getJobStepDefinitionId());
                switch (jobStepDefinition.getStepType()) {
                    case GENERIC:
                        jslStep.setBatchlet(JobDefinitionBuildUtils.buildGenericStep(jobStepDefinition));
                        break;
                    case TARGET:
                        jslStep.setChunk(JobDefinitionBuildUtils.buildChunkStep(jobStepDefinition));
                        break;
                    default:
                        throw new KapuaIllegalArgumentException(jobStepDefinition.getStepType().name(), "jobStepDefinition.stepType");
                }

                jslStep.setId("step-" + jobStep.getStepIndex());

                if (jobStepIterator.hasNext()) {
                    jslStep.setNextFromAttribute("step-" + (jobStep.getStepIndex() + 1));
                }

                jslStep.setProperties(JobDefinitionBuildUtils.buildStepProperties(jobStepDefinition, jobStep, jobStepIterator.hasNext()));

                jslExecutionElements.add(jslStep);
            }

            JSLJob jslJob = new JSLJob();
            jslJob.setRestartable("true");
            jslJob.setId(jobName);
            jslJob.setVersion("1.0");
            jslJob.setProperties(JobDefinitionBuildUtils.buildJobProperties(scopeId, jobId, jobStartOptions));
            jslJob.setListeners(JobDefinitionBuildUtils.buildListener());
            jslJob.getExecutionElements().addAll(jslExecutionElements);

            jobXmlDefinition = ModelSerializerFactory.createJobModelSerializer().serializeModel(jslJob);
        } catch (Exception e) {
            throw new CannotBuildJobDefDriverException(e, jobName);
        }

        //
        // Retrieve temporary directory for job XML definition
        String tmpDirectory = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
        File jobTempDirectory = new File(tmpDirectory, "kapua-job/" + scopeId.toCompactId());
        if (!jobTempDirectory.exists() && !jobTempDirectory.mkdirs()) {
            throw new CannotCreateTmpDirDriverException(jobName, jobTempDirectory.getAbsolutePath());
        }

        //
        // Retrieve job XML definition file. Delete it if exist
        File jobXmlDefinitionFile = new File(jobTempDirectory, jobId.toCompactId().concat(".xml"));
        if (jobXmlDefinitionFile.exists() && !jobXmlDefinitionFile.delete()) {
            throw new CannotCleanJobDefFileDriverException(jobName, jobXmlDefinitionFile.getAbsolutePath());
        }

        try (FileOutputStream tmpStream = new FileOutputStream(jobXmlDefinitionFile)) {
            IOUtils.write(jobXmlDefinition, tmpStream);
        } catch (IOException e) {
            throw new CannotWriteJobDefFileDriverException(e, jobName, jobXmlDefinitionFile.getAbsolutePath());
        }

        //
        // Check job running
        if (isRunningJob(scopeId, jobId)) {
            throw new JobExecutionIsRunningDriverException(JbatchDriver.getJbatchJobName(scopeId, jobId));
        }

        //
        // Start job
        try {
            JOB_OPERATOR.start(jobXmlDefinitionFile.getAbsolutePath().replaceAll("\\.xml$", ""), new Properties());
        } catch (NoSuchJobExecutionException | NoSuchJobException | JobSecurityException e) {
            throw new JobStartingDriverException(e, jobName);
        }
    }

    /**
     * Stops completely the jBatch job.
     * <p>
     * First invokes the {@link JobOperator#stop(long)} on the running execution, which stop the running execution.
     * Secondly, according to the {@link KapuaJobEngineSettingKeys#JOB_ENGINE_STOP_WAIT_CHECK} value, it waits asynchronously the complete stop of the job
     * to be able to invoke {@link JobOperator#abandon(long)} which terminate the jBatch Job.
     * <p>
     * A jBatch job cannot be resumed after this method is invoked on it.
     *
     * @param scopeId The scopeId of the {@link Job}
     * @param jobId   The id of the {@link Job}
     * @throws ExecutionNotFoundDriverException   when there isn't a corresponding job execution in jBatch tables
     * @throws ExecutionNotRunningDriverException when the corresponding job execution is not running.
     */
    public static void stopJob(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) throws JbatchDriverException {

        String jobName = getJbatchJobName(scopeId, jobId);

        //
        // Check running
        JobExecution runningExecution = getRunningJobExecution(scopeId, jobId);
        if (runningExecution == null) {
            throw new ExecutionNotRunningDriverException(jobName);
        }

        //
        // Do stop
        try {
            JOB_OPERATOR.stop(runningExecution.getExecutionId());

            if (JOB_ENGINE_SETTING.getBoolean(KapuaJobEngineSettingKeys.JOB_ENGINE_STOP_WAIT_CHECK)) {
                JbatchUtil.waitForStop(runningExecution, () -> JOB_OPERATOR.abandon(runningExecution.getExecutionId()));
            }
        } catch (NoSuchJobExecutionException e) {
            throw new ExecutionNotFoundDriverException(e, jobName);
        } catch (JobExecutionNotRunningException e) {
            throw new ExecutionNotRunningDriverException(e, jobName);
        }
    }

    /**
     * Checks whether or not the {@link Job} identified by the parametersis in a running status.
     * <p>
     * jBatch {@link Job} running statuses are listed in {@link JbatchJobRunningStatuses}
     *
     * @param scopeId The scopeId of the {@link Job}
     * @param jobId   The id of the {@link Job}
     * @return {@code true} if the jBatch {@link Job} is running, {@code false} otherwise,
     */
    public static boolean isRunningJob(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        return getRunningJobExecution(scopeId, jobId) != null;
    }

    public static void cleanJobData(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) throws CleanJobDataDriverException {
        String jobName = getJbatchJobName(scopeId, jobId);
        try {
            ((KapuaJDBCPersistenceManagerImpl) ServicesManagerImpl.getInstance().getPersistenceManagerService()).purgeByName(jobName);
        } catch (Exception ex) {
            throw new CleanJobDataDriverException(ex, jobName);
        }
    }

    //
    // Private methods
    //
    private static JobExecution getRunningJobExecution(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        return getJobExecutions(scopeId, jobId).stream().filter(je -> JbatchJobRunningStatuses.getStatuses().contains(je.getBatchStatus())).findFirst().orElse(null);
    }

    private static List<JobExecution> getJobExecutions(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        List<JobExecution> jobExecutions = Lists.newArrayList();

        String jobName = getJbatchJobName(scopeId, jobId);
        try {
            int jobInstanceCount = JOB_OPERATOR.getJobInstanceCount(jobName);
            List<JobInstance> jobInstances = JOB_OPERATOR.getJobInstances(jobName, 0, jobInstanceCount);
            jobInstances.forEach(ji -> jobExecutions.addAll(getJbatchJobExecutions(ji)));
        } catch (NoSuchJobException e) {
            LOG.debug("Error while getting Job: " + jobName, e);
            // This exception is thrown when there is no job, this means that the job never run before
            // So we can ignore it and return `null`
        } catch (NoSuchJobExecutionException e) {
            LOG.debug("Error while getting execution status for Job: " + jobName, e);
            // This exception is thrown when there is no execution is running.
            // So we can ignore it and return `null`
        }

        return jobExecutions;
    }

    private static List<JobExecution> getJbatchJobExecutions(@NotNull JobInstance jobInstance) {
        try {
            return JOB_OPERATOR.getJobExecutions(jobInstance);
        } catch (NoSuchJobInstanceException e) {
            LOG.debug("Error while getting Job Instance: " + jobInstance.getInstanceId(), e);
            // This exception is thrown when there is no job instance, this means that the job never run before
            // So we can ignore it and return `null`
        }

        return Collections.emptyList();
    }
}
