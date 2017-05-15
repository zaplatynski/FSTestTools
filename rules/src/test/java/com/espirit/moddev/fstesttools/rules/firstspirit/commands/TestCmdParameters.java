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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.agency.SpecialistsBroker;

import javax.inject.Inject;

/**
 * Created by e-Spirit AG.
 */
public class TestCmdParameters implements FsConnRuleCmdParamBean {

    @Inject
    private Connection connection;

    @Inject
    private SpecialistsBroker broker;

    @Inject
    private BaseContext context;

    @Override
    public String getProjectName() {
        return "Test";
    }

    public Connection getConnection() {
        return connection;
    }

    public SpecialistsBroker getBroker() {
        return broker;
    }

    public BaseContext getContext() {
        return context;
    }
}
