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
