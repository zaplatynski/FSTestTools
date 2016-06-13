package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.schedule.ScheduleTask;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * The type Mocking schedule context test.
 *
 * @param <C> the type parameter
 */
public class MockingScheduleContextTest<C extends MockingScheduleContext> extends MockingScriptContextTest<C> {

    /**
     * Instantiates a new Mocking script context test.
     *
     * @param environment             the environment
     * @param enableServiceBrokerFake the enable service broker fake
     */
    public MockingScheduleContextTest(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }

    @Override
    protected C createTestling(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        return (C) new MockingScheduleContext(TEST_LOCALE, enableServiceBrokerFake, environment);
    }

    /**
     * Test get tasks.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetTasks() throws Exception {
        assertThat("Expect a non null value", getTestling().getTasks(), is(notNullValue()));
    }

    /**
     * Test get project.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetProject() throws Exception {
        assertThat("Expect a non null value", getTestling().getProject(), is(notNullValue()));
    }

    /**
     * Test get user service.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetUserService() throws Exception {
        assertThat("Expect a non null value", getTestling().getUserService(), is(notNullValue()));
    }

    /**
     * Test get path.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetPath() throws Exception {
        getTestling().setPath("my/path/to/uid");
        assertThat("Expect a specific value", getTestling().getPath(), is("my/path/to/uid"));
    }

    /**
     * Test get variable names.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetVariableNames() throws Exception {
        getTestling().setVariable("myVar1", "myValue");
        getTestling().setVariable("myVar2", "myValue");
        getTestling().setVariable("myVar3", "myValue");
        getTestling().setVariable("myVar4", "myValue");

        assertThat("Expect a specific values", getTestling().getVariableNames(), containsInAnyOrder("myVar1", "myVar2", "myVar3", "myVar4"));
    }

    /**
     * Test variable.
     *
     * @throws Exception the exception
     */
    @Test
    public void testVariable() throws Exception {
        getTestling().setVariable("myVar1", "myValue1");

        assertThat("Expect a specific value", getTestling().getVariable("myVar1").toString(), is("myValue1"));

        getTestling().removeVariable("myVar1");

        assertThat("Expect a specific value", getTestling().getVariable("myVar1"), is(nullValue()));
    }

    /**
     * Test fatal error count.
     *
     * @throws Exception the exception
     */
    @Test
    public void testFatalErrorCount() throws Exception {
        getTestling().setFatalErrorCount(123);

        assertThat("Expect a specific value", getTestling().getFatalErrorCount(), is(123));
    }

    /**
     * Test error count.
     *
     * @throws Exception the exception
     */
    @Test
    public void testErrorCount() throws Exception {
        getTestling().setErrorCount(456);

        assertThat("Expect a specific value", getTestling().getErrorCount(), is(456));
    }

    /**
     * Test warning count.
     *
     * @throws Exception the exception
     */
    @Test
    public void testWarningCount() throws Exception {
        getTestling().setWarningCount(789);

        assertThat("Expect a specific value", getTestling().getWarningCount(), is(789));
    }

    /**
     * Test comment.
     *
     * @throws Exception the exception
     */
    @Test
    public void testComment() throws Exception {
        getTestling().setComment("myComment");

        assertThat("Expect a specific value", getTestling().getComment(), is("myComment"));
    }


    /**
     * Test state success.
     *
     * @throws Exception the exception
     */
    @Test
    public void testStateSuccess() throws Exception {
        assertFalse("Expect false", getTestling().isStateSuccess());

        getTestling().setStateToSuccess();

        assertTrue("Expect true", getTestling().isStateSuccess());

        getTestling().setStateToFailed();

        assertFalse("Expect false", getTestling().isStateSuccess());
    }

    /**
     * Test start time.
     *
     * @throws Exception the exception
     */
    @Test
    public void testStartTime() throws Exception {
        final Date now = new Date();
        getTestling().setStartTime(now);

        assertThat("Expect a specific value", getTestling().getStartTime(), is(now));
    }

    /**
     * Test add all tasks.
     *
     * @throws Exception the exception
     */
    @Test
    public void testTasks() throws Exception {
        final ScheduleTask scheduleTask = mock(ScheduleTask.class);
        final Collection<ScheduleTask> tasks = Arrays.asList(scheduleTask);

        getTestling().addAllTasks(tasks);
        getTestling().setTaskIndex(0);

        final ScheduleTask task = getTestling().getTask();

        assertThat("", getTestling().getTaskIndex(), is(0));
        assertThat("Expect same instance", task, is(sameInstance(scheduleTask)));
        assertThat("", getTestling().getTasks(), hasSize(1));

        getTestling().removeTask(scheduleTask);
        assertThat("", getTestling().getTasks(), hasSize(0));

    }

}
