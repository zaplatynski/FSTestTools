package com.espirit.moddev.fstesttools.rules.firstspirit;

import de.espirit.firstspirit.access.schedule.DeployTask;


/**
 * The type Scheduler configuration.
 * @deprecated
 */
@Deprecated
public class SchedulerConfiguration {

    private boolean generateDeleteDirectory;
    private DeployTask.Type deployTaskType;
    private String generateUrlPrefix;

    /**
     * Instantiates a new Scheduler configuration.
     */
    public SchedulerConfiguration() {
        generateDeleteDirectory = true;
        deployTaskType = DeployTask.Type.Full;
        generateUrlPrefix = "http://$address$";
    }

    /**
     * Is generate delete directory boolean.
     *
     * @return the boolean
     */
    public boolean isGenerateDeleteDirectory() {
        return generateDeleteDirectory;
    }

    /**
     * Sets generate delete directory.
     *
     * @param generateDeleteDirectory the generate delete directory
     */
    public void setGenerateDeleteDirectory(final boolean generateDeleteDirectory) {
        this.generateDeleteDirectory = generateDeleteDirectory;
    }

    /**
     * Gets deploy task type.
     *
     * @return the deploy task type
     */
    public DeployTask.Type getDeployTaskType() {
        return deployTaskType;
    }

    /**
     * Sets deploy task type.
     *
     * @param deployTaskType the deploy task type
     */
    public void setDeployTaskType(final DeployTask.Type deployTaskType) {
        this.deployTaskType = deployTaskType;
    }

    /**
     * Gets generate url prefix.
     *
     * @return the generate url prefix
     */
    public String getGenerateUrlPrefix() {
        return generateUrlPrefix;
    }

    /**
     * Sets generate url prefix.
     *
     * @param generateUrlPrefix the generate url prefix
     */
    public void setGenerateUrlPrefix(final String generateUrlPrefix) {
        this.generateUrlPrefix = generateUrlPrefix;
    }
}
