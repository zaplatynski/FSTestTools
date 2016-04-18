package com.espirit.moddev.fstesttools.rules.mocking;

import de.espirit.common.base.Logging;
import de.espirit.firstspirit.access.ServicesBroker;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;


/**
 * The type Mocking service broker is a service broker fake which creates service mocks.
 */
public class MockingServiceBroker implements ServicesBroker {

    private final Map<Class, Object> mocks;


    /**
     * Instantiates a new Mocking service broker.
     */
    public MockingServiceBroker() {
        mocks = new HashMap<Class, Object>();
    }

    @Override
    public <T> T getService(final Class<T> tClass) {
        if (!mocks.containsKey(tClass)) {
            Logging.logInfo("Create getMock for '" + tClass.getSimpleName() + "'...", getClass());
            T service = mock(tClass);
            mocks.put(tClass, service);
        }
        return (T) mocks.get(tClass);
    }
}
