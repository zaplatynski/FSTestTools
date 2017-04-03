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

        assertThat("Expect specific value", getTestling().getObject().toString(), is("test"));
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
