package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.Store.Type;
import de.espirit.firstspirit.client.plugin.toolbar.ToolbarContext;

import java.util.Locale;

/**
 * The Class MockingToolbarContext.
 */
public class MockingToolbarContext extends MockingBaseContext implements ToolbarContext {

    private final Type storeType;
    private final IDProvider element;
    private final String symbolicProjectName;


    /**
     * Instantiates a new mocking toolbar context.
     *
     * @param locale the locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param storeType the store type
     * @param element the element
     * @param symbolicProjectname the symbolic projectname
     * @param supportedEnvironments the supported environments
     */
    public MockingToolbarContext(Locale locale, boolean enableServiceBrokerFake, Type storeType, IDProvider element, String symbolicProjectname,
            Env... supportedEnvironments) {
        super(locale, enableServiceBrokerFake, supportedEnvironments);
        this.storeType = storeType;
        this.element = element;
        symbolicProjectName = symbolicProjectname;
    }

    /**
     * Instantiates a new mocking toolbar context.
     *
     * @param element the element
     * @param symbolicProjectname the symbolic projectname
     * @param supportedEnvironments the supported environments
     */
    public MockingToolbarContext(IDProvider element, String symbolicProjectname, Env... supportedEnvironments) {
        this(Locale.getDefault(), false, element, symbolicProjectname, supportedEnvironments);
    }

    /**
     * Instantiates a new mocking toolbar context.
     *
     * @param locale the locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param element the element
     * @param symbolicProjectname the symbolic projectname
     * @param supportedEnvironments the supported environments
     */
    public MockingToolbarContext(Locale locale, boolean enableServiceBrokerFake, IDProvider element, String symbolicProjectname,
            Env... supportedEnvironments) {
        this(locale, enableServiceBrokerFake, Type.SITESTORE, element, symbolicProjectname, supportedEnvironments);
    }

    /**
     * Instantiates a new mocking toolbar context.
     *
     * @param locale the locale
     * @param element the element
     * @param symbolicProjectname the symbolic projectname
     * @param supportedEnvironments the supported environments
     */
    public MockingToolbarContext(Locale locale, IDProvider element, String symbolicProjectname, Env... supportedEnvironments) {
        this(locale, false, element, symbolicProjectname, supportedEnvironments);
    }

    @Override
    public IDProvider getElement() {
        return element;
    }

    @Override
    public Type getStoreType() {
        return storeType;
    }

    @Override
    public String getSymbolicProjectName() {
        return symbolicProjectName;
    }
}
