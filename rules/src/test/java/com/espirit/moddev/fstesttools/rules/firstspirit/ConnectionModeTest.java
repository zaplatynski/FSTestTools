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

package com.espirit.moddev.fstesttools.rules.firstspirit;

import de.espirit.firstspirit.access.ConnectionManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * The type Connection mode test.
 */
@RunWith(Parameterized.class)
public class ConnectionModeTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
            new Object[][]{{ConnectionMode.HTTP, ConnectionManager.HTTP_MODE, 8000},
                           {ConnectionMode.SOCKET, ConnectionManager.SOCKET_MODE, 1088}});

    }

    private final ConnectionMode mode;
    private final int code;
    private final int port;

    /**
     * Instantiates a new Connection mode test.
     *
     * @param mode the mode
     * @param code the code
     * @param port the port
     */
    public ConnectionModeTest(final ConnectionMode mode, final int code, final int port) {
        this.mode = mode;
        this.code = code;
        this.port = port;
    }

    /**
     * Method: getCode()
     */
    @Test
    public void testGetCode() throws Exception {
        assertThat("FirstSpirit connection code", mode.getCode(), is(code));
    }

    /**
     * Method: getDefaultPort()
     */
    @Test
    public void testGetDefaultPort() throws Exception {
        assertThat("Default port", mode.getDefaultPort(), is(port));
    }

    /**
     * Method: getDefaultPortAsStr()
     */
    @Test
    public void testGetDefaultPortAsStr() throws Exception {
        assertThat("Default port as String", mode.getDefaultPortAsStr(), is(String.valueOf(port)));
    }

} 
