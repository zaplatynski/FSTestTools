package com.espirit.moddev.fstesttools.rules.logging;

import de.espirit.common.base.Logging;

import org.apache.log4j.spi.RootLogger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
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

    private static SingleMessageAppender appenderForTest = new SingleMessageAppender();

    @BeforeClass
    public static void setUp() throws Exception {
        RootLogger.getRootLogger().addAppender(appenderForTest);
    }

    @Test
    public void testLogging() throws Exception {
        assertThat(appenderForTest.getMessage(), is("Start of 'testLogging'..."));
        appenderForTest.setMessage("");

        LOGGER.info("Do some logging inside the test method...");
        assertThat(appenderForTest.getMessage(), is("Do some logging inside the test method..."));
        appenderForTest.setMessage("");

        Logging.logInfo("Test via FS-Logger", getClass());
        assertThat(appenderForTest.getMessage(), is("Test via FS-Logger"));
        appenderForTest.setMessage("");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        assertThat(appenderForTest.getMessage(), is("Successful termination of 'testLogging'!"));
    }
}
