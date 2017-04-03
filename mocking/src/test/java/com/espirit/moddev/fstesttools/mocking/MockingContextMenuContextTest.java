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
