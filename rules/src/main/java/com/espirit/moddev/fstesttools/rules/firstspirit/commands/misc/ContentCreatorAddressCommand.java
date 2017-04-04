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

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;

import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.project.Project;

import java.util.Locale;
import java.util.Optional;

/**
 * The type Content creator address command.
 */
public class ContentCreatorAddressCommand implements FsConnRuleCommand<ContentCreatorAddressParameters, ContentCreatorAddressResult> {

    @Override
    public String name() {
        return "ContentCreatorAddress";
    }

    @Override
    public ContentCreatorAddressResult execute(ContentCreatorAddressParameters parameters) {
        final Connection connection = parameters.getConnection();
        final Project project = connection.getProjectByName(parameters.getProjectName());
        final long projectId = project.getId();
        final Locale locale = Optional.ofNullable(parameters.getLocale()).orElse(project.getMasterLanguage().getLocale());
        final String language = locale.getLanguage();
        final String contentCreatorUrl =
            connection.getServerConfiguration().getUrl() + "fs5webedit_" + projectId + "/?locale=" + language + "&project=" + projectId;
        return new ContentCreatorAddressResult(contentCreatorUrl);
    }
}
