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

package com.espirit.moddev.fstesttools.rules.logging;

import de.espirit.common.base.Logging;

import org.apache.log4j.spi.RootLogger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LoggingRulesTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingRulesTest.class);

    @ClassRule
    public static InitLog4jLoggingRule loggingRule = new InitLog4jLoggingRule();

    @Rule
    public LogTestMethodNameRule rule = new LogTestMethodNameRule();

    @Rule
    public ErrorCollector errors = new ErrorCollector();

    private static SingleMessageAppender appenderForTest = new SingleMessageAppender();

    @BeforeClass
    public static void setUp() throws Exception {
        RootLogger.getRootLogger().addAppender(appenderForTest);
    }

    @Test
    public void testLogging() throws Exception {
        errors.checkThat("Expect starting message", appenderForTest.getMessage(), is("Start of 'testLogging'..."));
        appenderForTest.setMessage("");

        LOGGER.info("Do some logging inside the test method...");
        errors.checkThat("Expect SLF4J via Log4J message", appenderForTest.getMessage(), is("Do some logging inside the test method..."));
        appenderForTest.setMessage("");

        Logging.logInfo("Test via FS-Logger", getClass());
        Logging.logInfo("Test via FS-Logger", getClass());
        Logging.logInfo("Test via FS-Logger", getClass());

        errors.checkThat("Expect FS-Logger message", appenderForTest.getMessage(), is("Test via FS-Logger"));
        appenderForTest.setMessage("");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        assertThat("Expect ending message", appenderForTest.getMessage(), is("Successful termination of 'testLogging'!"));
    }
}
