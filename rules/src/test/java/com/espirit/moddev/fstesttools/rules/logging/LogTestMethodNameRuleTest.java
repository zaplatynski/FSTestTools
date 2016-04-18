package com.espirit.moddev.fstesttools.rules.logging;

import de.espirit.common.base.Logging;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public class LogTestMethodNameRuleTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogTestMethodNameRuleTest.class);

    @ClassRule
    public static InitLog4jLoggingRule loggingRule = new InitLog4jLoggingRule();

    @Rule
    public LogTestMethodNameRule rule = new LogTestMethodNameRule();

    @Before
    public void setUp() throws Exception {
        LOGGER.info("Watch log output to verify...");
    }

    @Test
    public void testSomething() throws Exception {
        LOGGER.info("Do some logging inside the test method...");
        Logging.logInfo("Test via FS-Logger", getClass());
        assertTrue(true);
    }

    @Test
    public void testSomeOtherThing() throws Exception {
        LOGGER.info("Do some logging again inside the test method...");
        assertTrue(true);
    }

    @Test
    public void testIgnored() throws Exception {
        assumeTrue("Test if assumeTrue triggers logging", false);
        LOGGER.info("Do some logging again inside the test method...");
        fail("Should not be executed");
    }
}
