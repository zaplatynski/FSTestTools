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

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by e-Spirit AG.
 */
public class FirstSpiritConnectionRuleTest {

    @ClassRule
    public static InitLog4jLoggingRule log4jLoggingRule = new InitLog4jLoggingRule();

    private FirstSpiritConnectionRule testling;

    @Before
    public void setUp() throws Exception {
        LoggerFactory.getLogger(getClass()).info("test...");
        testling = new FirstSpiritConnectionRule();
    }

    @Test
    public void testInvokeCommand() throws Exception {
        final TestCmdResult result = testling.invokeCommand("TEST", new TestCmdParameters());

        assertThat("Expect non null value", result, is(notNullValue()));
    }

}
