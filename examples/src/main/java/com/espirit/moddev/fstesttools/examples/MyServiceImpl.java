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

package com.espirit.moddev.fstesttools.examples;

import de.espirit.common.base.Logging;
import de.espirit.firstspirit.agency.ServerInformationAgent;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.module.ServerEnvironment;
import de.espirit.firstspirit.module.Service;
import de.espirit.firstspirit.module.ServiceProxy;
import de.espirit.firstspirit.module.descriptor.ServiceDescriptor;

public class MyServiceImpl implements Service<MyService>, MyService {

    private boolean ruuning;
    private ServiceDescriptor serviceDescriptor;
    private ServerEnvironment serverEnvironment;

    @Override
    public void start() {
        ruuning = true;
    }

    @Override
    public void stop() {
        ruuning = false;
    }

    @Override
    public boolean isRunning() {
        return ruuning;
    }

    @Override
    public Class<? extends MyService> getServiceInterface() {
        return MyService.class;
    }

    @Override
    public Class<? extends ServiceProxy<MyService>> getProxyClass() {
        return null;
    }

    @Override
    public void init(ServiceDescriptor serviceDescriptor, ServerEnvironment serverEnvironment) {
        this.serviceDescriptor = serviceDescriptor;
        this.serverEnvironment = serverEnvironment;
    }

    @Override
    public void installed() {
        Logging.logInfo(serviceDescriptor.getDisplayName() + " installed!", getClass());
    }

    @Override
    public void uninstalling() {
        Logging.logInfo(serviceDescriptor.getDisplayName() + " uninstalled!", getClass());
    }

    @Override
    public void updated(String s) {
        Logging.logInfo(serviceDescriptor.getDisplayName() + " updated!", getClass());
    }

    @Override
    public String getFirstSpiritVersion() {
        final SpecialistsBroker broker = serverEnvironment.getBroker();
        final ServerInformationAgent serverInformationAgent = broker.requireSpecialist(ServerInformationAgent.TYPE);
        final ServerInformationAgent.VersionInfo serverVersion = serverInformationAgent.getServerVersion();
        return serverVersion.getMajor() + "." + serverVersion.getMinor() + "." + serverVersion.getBuild();
    }
}
