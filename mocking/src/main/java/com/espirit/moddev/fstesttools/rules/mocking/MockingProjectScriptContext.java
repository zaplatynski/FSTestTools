package com.espirit.moddev.fstesttools.rules.mocking;

import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.project.ProjectScriptContext;

import java.util.Locale;


/**
 * The type Mocking project script context.
 */
public class MockingProjectScriptContext extends MockingScriptContext implements ProjectScriptContext {

    private final UserService userService = getMock(UserService.class);
    private final Project project = getMock(Project.class);

    /**
     * Instantiates a new Mocking project script context.
     *
     * @param supportedEnvironments the supported environments
     */
    public MockingProjectScriptContext(final Env... supportedEnvironments) {
        super(supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking project script context.
     *
     * @param locale                the locale
     * @param supportedEnvironments the supported environments
     */
    public MockingProjectScriptContext(final Locale locale, final Env... supportedEnvironments) {
        super(locale, supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking project script context.
     *
     * @param testLocale              the test locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param environment             the environment
     */
    public MockingProjectScriptContext(final Locale testLocale, final boolean enableServiceBrokerFake, final Env environment) {
        super(testLocale, enableServiceBrokerFake, environment);
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public Project getProject() {
        return project;
    }
}
