package com.espirit.moddev.fstesttools.rules.firstspirit;

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.TestCmdParameters;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.TestCmdResult;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdResultBean;
import com.espirit.moddev.fstesttools.rules.logging.InitLog4jLoggingRule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

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
