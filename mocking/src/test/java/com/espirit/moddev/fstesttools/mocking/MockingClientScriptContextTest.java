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
import de.espirit.firstspirit.access.project.Group;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * The type Mocking client script context test.
 *
 * @param <C> the type parameter
 */
public class MockingClientScriptContextTest<C extends MockingClientScriptContext> extends MockingProjectScriptContextTest<C> {


    /**
     * Instantiates a new Mocking client script context test.
     *
     * @param environment the environment
     */
    public MockingClientScriptContextTest(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }

    @Override
    protected C createTestling(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        return (C) new MockingClientScriptContext(TEST_LOCALE, enableServiceBrokerFake, environment);
    }

    /**
     * Test get user.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetUser() throws Exception {
        assertThat("Expect a non-null value", getTestling().getUser(), is(notNullValue()));
    }

    /**
     * Test get user groups.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetUserGroups() throws Exception {
        assertThat("Expect a non-null value", getTestling().getUserGroups(), is(notNullValue()));

    }

    /**
     * Test get store element.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetStoreElement() throws Exception {
        assertThat("Expect a non-null value", getTestling().getStoreElement(), is(notNullValue()));

    }

    /**
     * Test get element.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetElement() throws Exception {
        assertThat("Expect a non-null value", getTestling().getElement(), is(notNullValue()));
    }

    @Test
    public void testGroups() throws Exception {
        final Group group = mock(Group.class);
        getTestling().addGroup(group);

        assertThat("Expect specific value", getTestling().getUserGroups(), arrayContaining(group));

        getTestling().removeGroup(group);

        assertThat("Expect specific value", getTestling().getUserGroups(), arrayWithSize(0));

        getTestling().addAllGroups(Arrays.asList(group));

        assertThat("Expect specific value", getTestling().getUserGroups(), arrayContaining(group));

        getTestling().removeAllGroups(Arrays.asList(group));

        assertThat("Expect specific value", getTestling().getUserGroups(), arrayWithSize(0));
    }
}
