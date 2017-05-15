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

package com.espirit.moddev.fstesttools.rules.firstspirit;

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.TestCmdParameters;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.TestCmdResult;
import com.espirit.moddev.fstesttools.rules.logging.InitLog4jLoggingRule;
import com.sun.corba.se.pept.broker.Broker;

import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.agency.BrokerAgent;
import de.espirit.firstspirit.agency.SpecialistsBroker;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by e-Spirit AG.
 */
public class FirstSpiritConnectionRuleTest {

    @ClassRule
    public static InitLog4jLoggingRule log4jLoggingRule = new InitLog4jLoggingRule();

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    @Rule
    public FirstSpiritConnectionRule testling = new FirstSpiritConnectionRule(){
        @Override
        protected Connection obtainConnection() {
            final Connection connection = mock(Connection.class);
            when(connection.isConnected()).thenReturn(Boolean.TRUE);
            return connection;
        }
    };

    @Mock
    private SpecialistsBroker broker;

    @Mock
    private BrokerAgent brokerAgent;

    @Mock
    private SpecialistsBroker projectBroker;

    @Before
    public void setUp() throws Exception {
        final Connection connection = testling.getConnection();
        when(connection.getBroker()).thenReturn(broker);
        when(broker.requireSpecialist(BrokerAgent.TYPE)).thenReturn(brokerAgent);
    }

    @Test
    public void testInvokeCommand() throws Exception {
        final TestCmdParameters parameters = new TestCmdParameters();
        when(brokerAgent.getBrokerByProjectName(parameters.getProjectName())).thenReturn(projectBroker);

        final TestCmdResult result = testling.invokeCommand("TEST", parameters);

        assertThat("Expect non null value", parameters.getConnection(), is(notNullValue()));
        assertThat("Expect non null value", parameters.getBroker(), is(notNullValue()));
        assertThat("Expect non null value", parameters.getContext(), is(notNullValue()));
        assertThat("Expect non null value", result, is(notNullValue()));
    }

    @Test
    public void testBroker() throws Exception {
        assertThat("Expect identity", testling.getBroker(), is(sameInstance(broker)));
    }


    @Test
    public void testRequestSpecialist() throws Exception {
        when(broker.requestSpecialist(BrokerAgent.TYPE)).thenReturn(brokerAgent);

        final BrokerAgent brokerAgent = testling.requestSpecialist(BrokerAgent.TYPE);

        assertThat("Expect non null value", brokerAgent, is(notNullValue()));
    }
}
