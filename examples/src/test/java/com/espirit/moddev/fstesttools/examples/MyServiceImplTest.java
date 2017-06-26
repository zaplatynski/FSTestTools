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

import com.espirit.moddev.fstesttools.mocking.MockingBaseContext;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.agency.ServerInformationAgent;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.module.ServerEnvironment;
import de.espirit.firstspirit.module.ServiceProxy;
import de.espirit.firstspirit.module.descriptor.ServiceDescriptor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by zaplatynski on 12.06.2017.
 */
public class MyServiceImplTest {

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    @Mock
    private ServiceDescriptor description;

    @Mock
    private ServerEnvironment serverEnvironment;

    @Mock
    private ServerInformationAgent.VersionInfo versionInfo;

    private SpecialistsBroker specialistsBroker = new MockingBaseContext(Locale.ENGLISH, true, BaseContext.Env.HEADLESS);

    // unit under test
    private MyServiceImpl testling;

    @Before
    public void setUp() throws Exception {
        when(serverEnvironment.getBroker()).thenReturn(specialistsBroker);

        testling = new MyServiceImpl();
        testling.init(description, serverEnvironment);
    }

    @Test
    public void testStart() throws Exception {
        testling.start();

        final boolean running = testling.isRunning();

        assertThat("Expect that running state is true", running, is(Boolean.TRUE));
    }

    @Test
    public void testStop() throws Exception {
        testling.stop();

        final boolean running = testling.isRunning();

        assertThat("Expect that running state is true", running, is(Boolean.FALSE));
    }

    @Test
    public void testServiceInterface() throws Exception {
        final Class<? extends MyService> serviceInterface = testling.getServiceInterface();
        
        assertThat("Expect that class is of type MyService", serviceInterface, is(sameInstance(MyService.class)));
    }

    @Test
    public void testProxyClass() throws Exception {
        final Class<? extends ServiceProxy<MyService>> proxyClass = testling.getProxyClass();

        assertThat("Expect that class is null", proxyClass, is(nullValue()));
    }

    @Test
    public void testFirstSpiritVersion() throws Exception {

        final ServerInformationAgent serverInformationAgent = specialistsBroker.requireSpecialist(ServerInformationAgent.TYPE);
        when(serverInformationAgent.getServerVersion()).thenReturn(versionInfo);
        when(versionInfo.getMajor()).thenReturn(5);
        when(versionInfo.getMinor()).thenReturn(2);
        when(versionInfo.getBuild()).thenReturn(905);

        final String firstSpiritVersion = testling.getFirstSpiritVersion();

        assertThat("Expect a FirstSpirit version string", firstSpiritVersion, is("5.2.905"));
        verify(serverEnvironment, times(1)).getBroker();
    }

}
