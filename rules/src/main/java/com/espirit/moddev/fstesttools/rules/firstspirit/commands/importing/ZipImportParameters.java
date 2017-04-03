/*
 * ********************************************************************
 * Copyright (C) 2017 e-Spirit AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ********************************************************************
 */

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.importing;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import de.espirit.firstspirit.access.Connection;

import java.io.File;

import javax.inject.Inject;

/**
 * The type Zip import parameters.
 */
public final class ZipImportParameters implements FsConnRuleCmdParamBean {

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
