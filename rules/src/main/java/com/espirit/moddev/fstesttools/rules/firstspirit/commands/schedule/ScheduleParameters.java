package com.espirit.moddev.fstesttools.rules.firstspirit.commands.schedule;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.schedule.DeployTask;

import java.io.File;

import javax.inject.Inject;


/**
 * The type Schedule parameters.
 */
public class ScheduleParameters implements FsConnRuleCmdParamBean {

    private final String projectName;
    private final String entryName;
    private boolean generateDeleteDirectory;
    private DeployTask.Type deployTaskType;
    private String generateUrlPrefix;
    private File deployDir;

    @Inject
    private Connection connection;

    /**
     * Instantiates a new Schedule parameters.
     *
     * @param projectName the project name
     * @param entryName   the entry name
     */
    public ScheduleParameters(final String projectName, final String entryName) {
        this.projectName = projectName;
        this.entryName = entryName;
        generateDeleteDirectory = true;
        deployTaskType = DeployTask.Type.Full;
        generateUrlPrefix = "http://$address$";
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets entry name.
     *
     * @return the entry name
     */
    public String getEntryName() {
        return entryName;
    }

    /**
     * Gets deploy dir.
     *
     * @return the deploy dir
     */
    public File getDeployDir() {
        return deployDir;
    }

    /**
     * Sets deploy dir.
     *
     * @param deployDir the deploy dir
     */
    public void setDeployDir(final File deployDir) {
        this.deployDir = deployDir;
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
