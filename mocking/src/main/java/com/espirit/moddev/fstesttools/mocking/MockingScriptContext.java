package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.ScriptContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * The type Mocking script context.
 */
public class MockingScriptContext extends MockingBaseContext implements ScriptContext {

    private final Map<String, Object> properties = new HashMap<String, Object>();
    private final Connection connection = getMock(Connection.class);

    /**
     * Instantiates a new Mocking script context.
     *
     * @param supportedEnvironments the supported environments
     */
    public MockingScriptContext(final Env... supportedEnvironments) {
        super(supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking script context.
     *
     * @param locale                the locale
     * @param supportedEnvironments the supported environments
     */
    public MockingScriptContext(final Locale locale, final Env... supportedEnvironments) {
        super(locale, supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking script context.
     *
     * @param canadaFrench            the canada french
     * @param enableServiceBrokerFake the enable service broker fake
     * @param environment             the environment
     */
    public MockingScriptContext(final Locale canadaFrench, final boolean enableServiceBrokerFake, final Env environment) {
        super(canadaFrench, enableServiceBrokerFake, environment);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Object getProperty(final String name) {
        return properties.get(name);
    }

    @Override
    public void setProperty(final String name, final Object value) {
        properties.put(name, value);
    }

    @Override
    public void removeProperty(final String name) {
        properties.remove(name);
    }

    @Override
    public String[] getProperties() {
        return properties.keySet().toArray(new String[properties.size()]);
    }
}
