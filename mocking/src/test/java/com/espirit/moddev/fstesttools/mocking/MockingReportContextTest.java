package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.BaseContext;

import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


/**
 * The type Mocking report context test.
 *
 * @param <C> the type parameter
 */
public class MockingReportContextTest<C extends MockingReportContext> extends MockingBaseContextTest<C> {

    /**
     * Instantiates a new Mocking base context test.
     *
     * @param environment             the environment
     * @param enableServiceBrokerFake the enable service broker fake
     */
    public MockingReportContextTest(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }

    @Override
    protected C createTestling(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        return (C) new MockingReportContext(Locale.CANADA_FRENCH, enableServiceBrokerFake, environment);
    }

    /**
     * Test get object.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetObject() throws Exception {
        assertThat("Expect specific value", getTestling().getObject(), is(nullValue()));

        getTestling().setObject("test");

        assertThat("Expect specific value", getTestling().getObject(), is("test"));
    }

    /**
     * Test repaint.
     *
     * @throws Exception the exception
     */
    @Test
    public void testRepaint() throws Exception {
        assertThat("Expect false", getTestling().isRepainted(), is(false));

        getTestling().repaint();

        assertThat("Expect true", getTestling().isRepainted(), is(true));
    }
}
