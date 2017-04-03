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

import de.espirit.firstspirit.access.UserService;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;


/**
 * The type Mocking service broker test.
 */
public class MockingServiceBrokerTest {

    private MockingServiceBroker testling;

    @Before
    public void setUp() throws Exception {
        testling = new MockingServiceBroker();
    }

    @Test
    public void testGetService() throws Exception {
        final UserService userService = testling.getService(UserService.class);
        final UserService userServiceCopy = testling.getService(UserService.class);

        assertThat("Expect identity", userService, is(sameInstance(userServiceCopy)));
    }

}
