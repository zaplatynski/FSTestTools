package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.BaseContext.Env;
import de.espirit.firstspirit.access.store.Store.Type;
import de.espirit.firstspirit.access.store.sitestore.PageRef;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MockingToolbarContextTest<C extends MockingToolbarContext> extends MockingBaseContextTest<C> {

    public MockingToolbarContextTest(Env environment, boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }

    @Override
    protected C createTestling(Env environment, boolean enableServiceBrokerFake) {
        return (C) new MockingToolbarContext(TEST_LOCALE, enableServiceBrokerFake, mock(PageRef.class), "MyProject", environment);
    }

    @Test
    public void testGetElement() throws Exception {
        assertThat("Expect sepcific value", getTestling().getElement(), is(notNullValue()));
    }

    @Test
    public void testGetStoreType() throws Exception {
        assertThat("Expect sepcific value", getTestling().getStoreType(), is(Type.SITESTORE));
    }

    @Test
    public void testGetSymbolicProjectName() throws Exception {
        assertThat("Expect sepcific value", getTestling().getSymbolicProjectName(), is("MyProject"));
    }

}
