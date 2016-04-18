package com.espirit.moddev.fstesttools.rules.mocking;

import de.espirit.firstspirit.access.BaseContext;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public abstract class AbstractMockingGenerationScriptContextTest<C extends AbstractMockingGenerationScriptContext>
    extends MockingProjectScriptContextTest<C> {

    /**
     * Instantiates a new Mocking project script context test.
     *
     * @param environment the environment
     */
    public AbstractMockingGenerationScriptContextTest(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }

    @Test
    public void testGetGenerationContext() throws Exception {
        assertThat("Expect a non-null value", getTestling().getGenerationContext(), is(notNullValue()));
    }

    @Test
    public void testGetLanguage() throws Exception {
        assertThat("Expect a non-null value", getTestling().getLanguage(), is(notNullValue()));
    }

    @Test
    public void testGetTemplateSet() throws Exception {
        assertThat("Expect a non-null value", getTestling().getTemplateSet(), is(notNullValue()));
    }

    @Test
    public void testIsRelease() throws Exception {
        assertThat("Expect false", getTestling().isRelease(), is(false));
    }

    @Test
    public void testIsPreview() throws Exception {
        assertThat("Expect false", getTestling().isPreview(), is(false));
    }

    @Test
    public void testToString() throws Exception {
        assertThat("Expect a non-null value", getTestling().toString("myTest"), is("myTest"));
    }
}
