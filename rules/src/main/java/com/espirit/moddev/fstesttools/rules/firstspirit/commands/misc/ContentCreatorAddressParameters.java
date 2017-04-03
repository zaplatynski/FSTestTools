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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.misc;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import de.espirit.firstspirit.access.Connection;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

/**
 * The type Content creator adress parameters.
 */
public class ContentCreatorAddressParameters implements FsConnRuleCmdParamBean {

    private final String projectName;
    private Locale locale;

    @Inject
    private Connection connection;

    /**
     * Instantiates a new Content creator adress parameters.
     *
     * @param projectName the project name
     */
    public ContentCreatorAddressParameters(String projectName) {
        this.projectName = Objects.requireNonNull(projectName, "projectName can not be null!");
    }

    /**
     * Instantiates a new Content creator adress parameters.
     *
     * @param projectName the project name
     * @param locale      the locale
     */
    public ContentCreatorAddressParameters(String projectName, Locale locale) {
        this(projectName);
        this.locale = locale;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets locale.
     *
     * @param locale the locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
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
