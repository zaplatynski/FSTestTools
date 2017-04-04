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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import de.espirit.firstspirit.access.Connection;

import java.util.Objects;

import javax.inject.Inject;

/**
 * The type Modify store parameters.
 */
public final class ModifyStoreParameters implements FsConnRuleCmdParamBean {

    private final String projectName;
    private final String name;
    private String channel;
    private String content;

    @Inject
    private Connection connection;

    /**
     * Instantiates a new Modify store parameters.
     *
     * @param projectName the project name
     * @param name        the name
     */
    public ModifyStoreParameters(final String projectName, final String name) {
        this.projectName = Objects.requireNonNull(projectName, "projectName can not be null!");
        this.name = Objects.requireNonNull(name, "name can not be null!");
    }

    /**
     * Instantiates a new Modify store parameters.
     *
     * @param projectName the project name
     * @param uid         the uid
     * @param channel     the channel
     * @param content     the content
     */
    public ModifyStoreParameters(final String projectName, final String uid, final String channel, final String content) {
        this(projectName, uid);
        this.channel = channel;
        this.content = content;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
