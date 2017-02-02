package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.BaseContext.Env;
import de.espirit.firstspirit.access.store.Store.Type;
import de.espirit.firstspirit.access.store.sitestore.PageRef;

import org.junit.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MockingContextMenuContextTest<C extends MockingContextMenuContext> extends MockingBaseContextTest<C> {

    public MockingContextMenuContextTest(Env environment, boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }


    @Override
    protected C createTestling(Env environment, boolean enableServiceBrokerFake) {
        return (C) new MockingContextMenuContext(TEST_LOCALE, enableServiceBrokerFake, environment);
    }


    @Test
    public void testGetStoreType() throws Exception {
        assertThat("Expect sepcific value", getTestling().getStoreType(), is(Type.SITESTORE));
    }


    @Test
    public void testGetElements() throws Exception {
        assertThat("Expect empty list", getTestling().getElements().toList().isEmpty(), is(Boolean.TRUE));

        getTestling().addElement(mock(PageRef.class));

        assertThat("Expect non empty list", getTestling().getElements().toList(), hasSize(1));
    }

}
