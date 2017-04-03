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
