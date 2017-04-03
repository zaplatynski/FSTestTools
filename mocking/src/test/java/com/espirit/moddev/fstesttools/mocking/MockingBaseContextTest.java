package com.espirit.moddev.fstesttools.mocking;


import com.espirit.moddev.fstesttools.mocking.util.AbstractMockManager;
import com.espirit.moddev.fstesttools.rules.logging.InitLog4jLoggingRule;
import com.espirit.moddev.fstesttools.rules.logging.LogTestMethodNameRule;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.ServicesBroker;
import de.espirit.firstspirit.agency.LanguageAgent;
import de.espirit.firstspirit.agency.UIAgent;
import de.espirit.firstspirit.service.ppool.ServerPackageManager;
import de.espirit.firstspirit.webedit.WebeditUiAgent;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;


/**
 * The type Mocking context test.
 *
 * @param <C> the type parameter
 */
@RunWith(Parameterized.class)
public class MockingBaseContextTest<C extends MockingBaseContext> {

    /**
     * The constant TEST_LOCALE.
     */
    public static final Locale TEST_LOCALE = Locale.CANADA_FRENCH;
    private AppenderSkeleton testAppender;

    /**
     * Data collection.
     *
     * @return the collection
     */
    @Parameterized.Parameters(name = "{0} (enableServiceBrokerFake={1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(
            new Object[][]{{BaseContext.Env.WEBEDIT, false}, {BaseContext.Env.PREVIEW, false}, {BaseContext.Env.HEADLESS, false},
                           {BaseContext.Env.FS_BUTTON, false}, {BaseContext.Env.DROP, false},
                           {BaseContext.Env.WEBEDIT, true}, {BaseContext.Env.PREVIEW, true}, {BaseContext.Env.HEADLESS, true},
                           {BaseContext.Env.FS_BUTTON, true}, {BaseContext.Env.DROP, true}});
    }

    /**
     * The constant LOGGING_RULE.
     */
    @ClassRule
    public static InitLog4jLoggingRule LOGGING_RULE = new InitLog4jLoggingRule(Level.DEBUG);

    @Rule
    public LogTestMethodNameRule logTestNames = new LogTestMethodNameRule();

    private final BaseContext.Env environment;
    private final boolean enableServiceBrokerFake;

    private C testling;
    private String logMessage;

    /**
     * Instantiates a new Mocking base context test.
     *
     * @param environment the environment
     */
    public MockingBaseContextTest(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        this.environment = environment;
        this.enableServiceBrokerFake = enableServiceBrokerFake;
    }

    /**
     * Gets environment.
     *
     * @return the environment
     */
    public BaseContext.Env getEnvironment() {
        return environment;
    }

    /**
     * Gets testling.
     *
     * @return the testling
     */
    public C getTestling() {
        return testling;
    }

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

        testling = createTestling(environment, enableServiceBrokerFake);

        testAppender = new AppenderSkeleton() {
            @Override
            protected void append(final LoggingEvent loggingEvent) {
                System.out.println("**** APPEND ****");
                logMessage = loggingEvent.getMessage().toString();
            }

            @Override
            public void close() {

            }

            @Override
            public boolean requiresLayout() {
                return false;
            }
        };

        Logger.getLogger(testling.getClass()).setAdditivity(true);
        Logger.getLogger(testling.getClass()).addAppender(testAppender);
    }

    @After
    public void tearDown() throws Exception {
        Logger.getLogger(testling.getClass()).removeAppender(testAppender);
    }

    /**
     * Create testling.
     *
     * @param environment the environment
     * @return the c
     */
    protected C createTestling(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        return (C) new MockingBaseContext(TEST_LOCALE, enableServiceBrokerFake, environment);
    }

    /**
     * Test require method.
     *
     * @throws Exception the exception
     */
    @Test
    public void testRequire() throws Exception {
        final LanguageAgent languageAgent = testling.requireSpecialist(LanguageAgent.TYPE);
        assertThat("Expect a non null value", languageAgent, is(notNullValue()));

        final Language masterLanguage = languageAgent.getMasterLanguage();
        assertThat("Expect a certain locale", masterLanguage.getLocale(), is(TEST_LOCALE));
        assertThat("Expect ISO 2-letter code value", masterLanguage.getAbbreviation(), is(TEST_LOCALE.getLanguage()));
        assertThat("Expect display name in specific locale", masterLanguage.getDisplayName(masterLanguage),
                   is(TEST_LOCALE.getDisplayLanguage(TEST_LOCALE)));
    }

    /**
     * Test ui agents.
     *
     * @throws Exception the exception
     */
    @Test
    public void testUiAgents() throws Exception {

        if (testling.is(BaseContext.Env.WEBEDIT)) {
            final WebeditUiAgent uiAgent = testling.requestSpecialist(WebeditUiAgent.TYPE);
            assertThat("Expect a certain locale", uiAgent.getDisplayLanguage().getLocale(), is(TEST_LOCALE));
        }

        if (testling.is(BaseContext.Env.PREVIEW)) {
            final UIAgent uiAgent = testling.requestSpecialist(UIAgent.TYPE);
            assertThat("Expect a certain locale", uiAgent.getDisplayLanguage().getLocale(), is(TEST_LOCALE));
        }

        //All other environments shouldn't have UiAgents...
        if (!(testling.is(BaseContext.Env.WEBEDIT) || testling.is(BaseContext.Env.PREVIEW))) {
            final WebeditUiAgent webeditUiAgent = testling.requestSpecialist(WebeditUiAgent.TYPE);
            final UIAgent uiAgent = testling.requestSpecialist(UIAgent.TYPE);

            assertThat("Expect a certain locale", webeditUiAgent.getDisplayLanguage(), is(nullValue()));
            assertThat("Expect a certain locale", uiAgent.getDisplayLanguage(), is(nullValue()));
        }
    }

    /**
     * Test same instance of mocks.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSameInstance() throws Exception {
        final LanguageAgent languageAgent1 = testling.requireSpecialist(LanguageAgent.TYPE);
        final LanguageAgent languageAgent2 = testling.requireSpecialist(LanguageAgent.TYPE);

        assertThat(languageAgent1, is(sameInstance(languageAgent2)));
    }


    @Test
    public void testLogDebug() throws Exception {
        testling.logDebug("myDebugMessage");

        assertThat(logMessage, is("myDebugMessage"));
    }

    @Test
    public void testLogInfo() throws Exception {
        testling.logInfo("myInfoMessage");

        assertThat(logMessage, is("myInfoMessage"));
    }

    @Test
    public void testLogWarning() throws Exception {
        testling.logWarning("myWarningMessage");

        assertThat(logMessage, is("myWarningMessage"));
    }

    @Test
    public void testLogError() throws Exception {
        testling.logError("myErrorMessage");

        assertThat(logMessage, is("myErrorMessage"));
    }

    @Test
    public void testLogErrorWithException() throws Exception {
        testling.logError("myErrorExceptionMessage", new Exception("JUnit"));

        assertThat(logMessage, is("myErrorExceptionMessage"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullLocale() throws Exception {
        new MockingBaseContext((Locale) null, BaseContext.Env.WEBEDIT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoEnvironments() throws Exception {
        new MockingBaseContext(TEST_LOCALE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnvironment() throws Exception {
        final BaseContext.Env[] envs = null;
        new MockingBaseContext(envs);
    }

    @Test
    public void testServiceBroker() throws Exception {
        final ServicesBroker servicesBroker = testling.requireSpecialist(ServicesBroker.TYPE);

        final ServerPackageManager service = servicesBroker.getService(ServerPackageManager.class);

        if (enableServiceBrokerFake) {
            assertThat("Expect a non-null value", service, is(allOf(notNullValue(), hasToString(containsString("Mock for ServerPackageManager")))));
        } else {
            assertThat("Expect a null value", service, is(nullValue()));
        }
    }

    @Test
    public void testMultipleEnvironments() {
        final AbstractMockManager mockingBaseContext = new MockingBaseContext(Locale.ENGLISH, BaseContext.Env.PREVIEW, BaseContext.Env.FS_BUTTON);
        assertThat("Expect a non-null value", mockingBaseContext, notNullValue());
    }
}
