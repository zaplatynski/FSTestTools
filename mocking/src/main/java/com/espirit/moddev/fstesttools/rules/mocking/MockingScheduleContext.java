package com.espirit.moddev.fstesttools.rules.mocking;

import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.schedule.ScheduleContext;
import de.espirit.firstspirit.access.schedule.ScheduleTask;

import java.io.Closeable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The type Mocking schedule context.
 */
public class MockingScheduleContext extends MockingScriptContext implements ScheduleContext {

    private Map<String, Serializable> variables = new HashMap<String, Serializable>();
    private final UserService userService = getMock(UserService.class);
    private final Project project = getMock(Project.class);
    private boolean stateSuccess;
    private String comment;
    private Date startTime;
    private String path;
    private List<ScheduleTask> tasks = new LinkedList<ScheduleTask>();
    private int taskIndex;
    private int fatalErrorCount;
    private int errorCount;
    private int warningCount;

    /**
     * Instantiates a new Mocking schedule context.
     *
     * @param supportedEnvironments the supported environments
     */
    public MockingScheduleContext(final Env... supportedEnvironments) {
        super(supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking schedule context.
     *
     * @param locale the locale
     * @param supportedEnvironments the supported environments
     */
    public MockingScheduleContext(final Locale locale, final Env... supportedEnvironments) {
        super(locale, supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking schedule context.
     *
     * @param testLocale the test locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param environment the environment
     */
    public MockingScheduleContext(final Locale testLocale, final boolean enableServiceBrokerFake, final Env environment) {
        super(testLocale, enableServiceBrokerFake, environment);
    }

    @Override
    public ScheduleTask getTask() {
        return tasks.get(taskIndex);
    }

    @Override
    public int getTaskIndex() {
        return taskIndex;
    }

    @Override
    public List<? extends ScheduleTask> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Set<String> getVariableNames() {
        return variables.keySet();
    }

    @Override
    public Object getVariable(final String s) {
        return variables.get(s);
    }

    @Override
    public void setVariable(final String s, final Serializable serializable) {
        variables.put(s, serializable);
    }

    @Override
    public void removeVariable(final String s) {
        variables.remove(s);
    }

    @Override
    public int getFatalErrorCount() {
        return fatalErrorCount;
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public int getWarningCount() {
        return warningCount;
    }

    @Override
    public void setComment(final String s) {
        this.comment = s;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setStateToFailed() {
        stateSuccess = false;
    }

    @Override
    public void setStateToSuccess() {
        stateSuccess = true;
    }

    @Override
    public boolean isStateFailed() {
        return false;
    }

    @Override
    public boolean isStateSuccess() {
        return stateSuccess;
    }

    @Override
    public Date getStartTime() {
        return new Date(startTime.getTime());
    }

    @Override
    public void setStartTime(final Date date) {
        this.startTime = new Date(date.getTime());
    }

    @Override
    public void addCloseable(final Closeable closeable) {
        //keine ahnung was man hier implementieren soll...
    }

    /**
     * Sets warning count.
     *
     * @param warningCount the warning count
     */
    public void setWarningCount(final int warningCount) {
        this.warningCount = warningCount;
    }

    /**
     * Sets error count.
     *
     * @param errorCount the error count
     */
    public void setErrorCount(final int errorCount) {
        this.errorCount = errorCount;
    }

    /**
     * Sets fatal error count.
     *
     * @param fatalErrorCount the fatal error count
     */
    public void setFatalErrorCount(final int fatalErrorCount) {
        this.fatalErrorCount = fatalErrorCount;
    }

    /**
     * Sets task index.
     *
     * @param taskIndex the task index
     */
    public void setTaskIndex(final int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Sets path.
     *
     * @param path the path
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Add all tasks.
     *
     * @param scheduleTasks the scheduleTasks
     * @return the boolean
     */
    public boolean addAllTasks(final Collection<ScheduleTask> scheduleTasks) {
        return tasks.addAll(scheduleTasks);
    }

    /**
     * Add task.
     *
     * @param task the task
     * @return the boolean
     */
    public boolean addTask(final ScheduleTask task) {
        return tasks.add(task);
    }

    /**
     * Remove task.
     *
     * @param task the task
     * @return the boolean
     */
    public boolean removeTask(final ScheduleTask task) {
        return tasks.remove(task);
    }

    /**
     * Remove all tasks.
     *
     * @param scheduleTasks the scheduleTasks
     * @return the boolean
     */
    public boolean removeAllTasks(final Collection<ScheduleTask> scheduleTasks) {
        return tasks.removeAll(scheduleTasks);
    }
}
