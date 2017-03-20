package com.espirit.moddev.fstesttools.rules.firstspirit.commands.importing;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import de.espirit.firstspirit.access.Connection;

import java.io.File;

import javax.inject.Inject;

/**
 * The type Zip import parameters.
 */
public class ZipImportParameters implements FsConnRuleCmdParamBean {

    private final String projectName;
    private final File zip;

    @Inject
    private Connection connection;

    /**
     * Instantiates a new Zip import parameters.
     *
     * @param projectName the project name
     * @param zip         the zip
     */
    public ZipImportParameters(final String projectName, final File zip) {
        this.projectName = projectName;
        this.zip = zip;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets zip.
     *
     * @return the zip
     */
    public File getZip() {
        return zip;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
}
