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
