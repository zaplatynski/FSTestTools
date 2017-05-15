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

package com.espirit.moddev.fstesttools.rules.firstspirit.utils.context;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.agency.LanguageAgent;
import de.espirit.firstspirit.agency.SpecialistsBroker;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by zaplatynski on 15.05.2017.
 */
public class TestContextTest {

    private static AppenderForAsserts appenderForAsserts = new AppenderForAsserts();

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    @Mock
    protected SpecialistsBroker broker;

    @Mock
    private LanguageAgent languageAgent;

    protected TestContext testling;

    @BeforeClass
    public static void setUpClass() throws Exception {
        BasicConfigurator.configure();
        Logger logger = Logger.getLogger(TestContext.class);
        logger.addAppender(appenderForAsserts);
    }

    @Before
    public void setUp() throws Exception {
        testling = new TestContext(broker);
    }

    @Test
    public void logDebug() throws Exception {
        testling.logDebug("debug");

        assertThat("Expect specific value", appenderForAsserts.getMessage(), is("debug"));
    }

    @Test
    public void logInfo() throws Exception {
        testling.logInfo("info");

        assertThat("Expect specific value", appenderForAsserts.getMessage(), is("info"));
    }

    @Test
    public void logWarning() throws Exception {
        testling.logWarning("warn");

        assertThat("Expect specific value", appenderForAsserts.getMessage(), is("warn"));
    }

    @Test
    public void logError() throws Exception {
        testling.logError("error");

        assertThat("Expect specific value", appenderForAsserts.getMessage(), is("error"));
    }

    @Test
    public void logErrorException() throws Exception {
        testling.logError("error", new Exception("Test"));

        assertThat("Expect specific value", appenderForAsserts.getMessage(), is("error"));
    }

    @Test
    public void testIs() throws Exception {
        assertThat("Expect headless", testling.is(BaseContext.Env.HEADLESS), is(Boolean.TRUE));
        assertThat("Expect headless", testling.is(BaseContext.Env.PREVIEW), is(Boolean.TRUE));
        assertThat("Expect headless", testling.is(BaseContext.Env.DROP), is(Boolean.FALSE));
        assertThat("Expect headless", testling.is(BaseContext.Env.FS_BUTTON), is(Boolean.FALSE));
        assertThat("Expect headless", testling.is(BaseContext.Env.WEBEDIT), is(Boolean.FALSE));
    }

    @Test
    public void requestSpecialist() throws Exception {
        when(broker.requestSpecialist(LanguageAgent.TYPE)).thenReturn(languageAgent);

        final LanguageAgent agent = testling.requestSpecialist(LanguageAgent.TYPE);

        assertThat("Excpect same instance", agent, sameInstance(languageAgent));
        verify(broker, times(1)).requestSpecialist(LanguageAgent.TYPE);
    }

    @Test
    public void requireSpecialist() throws Exception {
        when(broker.requireSpecialist(LanguageAgent.TYPE)).thenReturn(languageAgent);

        final LanguageAgent agent = testling.requireSpecialist(LanguageAgent.TYPE);

        assertThat("Excpect same instance", agent, sameInstance(languageAgent));
        verify(broker, times(1)).requireSpecialist(LanguageAgent.TYPE);
    }

}
