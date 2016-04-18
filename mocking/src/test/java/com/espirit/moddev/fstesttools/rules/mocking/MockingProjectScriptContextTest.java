package com.espirit.moddev.fstesttools.rules.mocking;

import de.espirit.firstspirit.access.BaseContext;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * The type Mocking project script context test.
 *
 * @param <C> the type parameter
 */
public class MockingProjectScriptContextTest<C extends MockingProjectScriptContext> extends MockingScriptContextTest<C> {

    /**
     * Instantiates a new Mocking project script context test.
     *
     * @param environment the environment
     */
    public MockingProjectScriptContextTest(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }

    @Override
    protected C createTestling(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        return (C) new MockingProjectScriptContext(TEST_LOCALE, enableServiceBrokerFake, environment);
    }

    /**
     * Test get user service.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetUserService() throws Exception {
        assertThat("Expect a non-null value", getTestling().getUserService(), is(notNullValue()));
    }

    /**
     * Test get project.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetProject() throws Exception {
        assertThat("Expect a non-null value", getTestling().getProject(), is(notNullValue()));
    }
}
