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

import de.espirit.firstspirit.access.BaseContext;

import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * The type Mocking script context test.
 *
 * @param <C> the type parameter
 */
public class MockingScriptContextTest<C extends MockingScriptContext> extends MockingBaseContextTest<C> {

    /**
     * Instantiates a new Mocking script context test.
     *
     * @param environment the environment
     */
    public MockingScriptContextTest(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }

    @Override
    protected C createTestling(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        return (C) new MockingScriptContext(Locale.CANADA_FRENCH, enableServiceBrokerFake, environment);
    }

    /**
     * Test get connection.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetConnection() throws Exception {
        assertThat("Expect a non-null value", getTestling().getConnection(), is(notNullValue()));
    }

    /**
     * Test property.
     *
     * @throws Exception the exception
     */
    @Test
    public void testProperty() throws Exception {

        getTestling().setProperty("myProperty", "myValue");

        final String myPropertyValue = (String) getTestling().getProperty("myProperty");

        assertThat("Expect myValue", myPropertyValue, is("myValue"));
    }

    /**
     * Test get properties.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetProperties() throws Exception {
        getTestling().setProperty("myProperty1", "myValue");
        getTestling().setProperty("myProperty2", "myValue");

        assertThat("Expect an array of property names", getTestling().getProperties(), arrayContainingInAnyOrder("myProperty1", "myProperty2"));
    }
}
