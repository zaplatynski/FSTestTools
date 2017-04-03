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

/**
 * The enum Connection mode.
 */
public enum ConnectionMode {
    /**
     * The HTTP mode.
     */
    HTTP(ConnectionManager.HTTP_MODE, 8000),

    /**
     * The SOCKET mode.
     */
    SOCKET(ConnectionManager.SOCKET_MODE, 1088);


    private final int code;
    private final int defaultPort;

    ConnectionMode(int value, int defaultPort) {
        this.code = value;
        this.defaultPort = defaultPort;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets default port.
     *
     * @return the default port
     */
    public int getDefaultPort() {
        return defaultPort;
    }

    /**
     * Gets default port as string.
     *
     * @return the default port as str
     */
    public String getDefaultPortAsStr() {
        return String.valueOf(defaultPort);
    }
}
